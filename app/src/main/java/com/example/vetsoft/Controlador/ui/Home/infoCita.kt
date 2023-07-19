package com.example.vetsoft.Controlador.ui.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Objects

lateinit var txtMascFC: EditText
lateinit var txtFechaFC: EditText
lateinit var txtMotivoFC: EditText
lateinit var txtDoctorFC: EditText
lateinit var txtNDFC: EditText
lateinit var txtNCFC: EditText
lateinit var btnVolverFC: ImageButton
lateinit var btnInfoDocFC: Button
lateinit var btnEliminarFC: Button
class infoCita : Fragment() {

    private var idUs: Int = 0
    private var idCl: Int = 0
    private var idCit: Int = 0
    private var idDoc: Int = 0
    private var conx = conx()
    private var vali = Validat()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs = arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
            idCit = arguments?.getInt("idCit")!!
            idDoc = arguments?.getInt("idDoc")!!
            Log.i("idoc",idDoc.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_cita, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtMascFC =requireView().findViewById(R.id.txtMascFC)
        txtFechaFC =requireView().findViewById(R.id.txtFechaFC)
        txtMotivoFC =requireView().findViewById(R.id.txtMotivoFC)
        txtDoctorFC =requireView().findViewById(R.id.txtDoctorFC)
        txtNDFC =requireView().findViewById(R.id.txtNDFC)
        txtNCFC =requireView().findViewById(R.id.txtNCFC)
        btnVolverFC =requireView().findViewById(R.id.btnVolverFC)
        btnInfoDocFC =requireView().findViewById(R.id.btnInfoDocFC)
        btnEliminarFC =requireView().findViewById(R.id.btnEliminarFC)

//DESABILITAR Y HABILITAR
        val lista= listOf(txtMascFC,txtFechaFC,txtMotivoFC,txtDoctorFC,txtNDFC,txtNCFC,)
        vali.Habilit(lista,false)

        CargarDatos()
        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
            putInt("idCit", idCit)
            putInt("idDoc", idDoc)
        }

        btnVolverFC.setOnClickListener(){
            findNavController().navigate(R.id.action_infoCita_to_citasPendientes, bundle)
        }
        btnInfoDocFC.setOnClickListener(){
            findNavController().navigate(R.id.action_infoCita_to_infoDoc, bundle)
        }
        btnEliminarFC.setOnClickListener(){
            EliminarCit();
            findNavController().navigate(R.id.action_infoCita_to_citasPendientes, bundle)
        }
    }
    fun CargarDatos() {
        try {
            var cadena: String =
            "select idCita,ts.nombre as 'Servicio',notaDelCliente as 'NC',notaDelDoctor as 'ND', CONCAT(CONVERT(varchar, c.fecha, 100),' ',CONVERT(varchar,c.hora, 100)) as fecha, \n" +
            "CONCAT(d.nombre, ' ', d.apellido) as 'Doctor' from tbCitas c,tbAnimales a, tbDoctores d,tbTipoServicio ts \n" +
            "where c.idAnimal=a.idAnimal and d.idDoctor=c.idDoctor and ts.idTipoServicio=c.idTipoServicio\n" +
            "and idCita=? and estado='Pendiente';"
            var st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, idCit)

            st = ps.executeQuery()
            st.next()
            txtMascFC.setText(st.getString("nombre"))
            txtFechaFC.setText(st.getString("fecha"))
            txtMotivoFC.setText(st.getString("Servicio"))
            txtDoctorFC.setText(st.getString("Doctor"))
            txtNDFC.setText(st.getString("ND"))
            txtNCFC.setText(st.getString("NC"))


        } catch (ex: SQLException) {
            Toast.makeText(context, "Error al mostrar", Toast.LENGTH_SHORT).show()
        }
    }
    fun EliminarCit() {
        try {
            var cadena: String =
                "delete tbCitas  where idCita=?;"
            var st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            Log.i("elim", idCit.toString())
            ps.setInt(1, idCit)

            ps.executeUpdate()
            Toast.makeText(context, "Cita eliminada", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException) {
            Log.i("elim", ex.message.toString())
            Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
        }
    }


}