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
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Controlador.ui.Perfil.btnActDP
import com.example.vetsoft.Controlador.ui.Perfil.btnGuardarDP
import com.example.vetsoft.Controlador.ui.Perfil.btnNaciDP
import com.example.vetsoft.Controlador.ui.Perfil.btnVolverDP
import com.example.vetsoft.Controlador.ui.Perfil.spSexoDP
import com.example.vetsoft.Controlador.ui.Perfil.txtApellDP
import com.example.vetsoft.Controlador.ui.Perfil.txtCorreoDP
import com.example.vetsoft.Controlador.ui.Perfil.txtDuiDP
import com.example.vetsoft.Controlador.ui.Perfil.txtNaciDP
import com.example.vetsoft.Controlador.ui.Perfil.txtNombDP
import com.example.vetsoft.Controlador.ui.Perfil.txtTelDP
import com.example.vetsoft.Controlador.ui.Perfil.txtUsDP
import com.example.vetsoft.Controlador.ui.Perfil.txvUsDP
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

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
    private var conx = conx()
    private var vali = Validat()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs = arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
            idCit = arguments?.getInt("idCit")!!
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

        Habilit(false)
        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
            putInt("idCit", idCit)
        }

        btnVolverFC.setOnClickListener(){
            findNavController().navigate(R.id.action_infoCita_to_citasPendientes, bundle)
        }
        btnInfoDocFC.setOnClickListener(){
            findNavController().navigate(R.id.action_infoCita_to_citasPendientes, bundle)
        }
        btnEliminarFC.setOnClickListener(){
            EliminarCit();
        }
    }
    fun CargarDatos() {
        try {
            var cadena: String =
                "select ci.idCliente,CONCAT(d.nombres,' ',d.apellidos) as Doctor,e.especialidad,FORMAT(fechahora,'dd-MM-yyyy') AS Fecha,\n" +
                        "FORMAT(fechahora,'hh:mm tt') as Hora, CONCAT(c.nombres,' ',c.apellidos) as Paciente from tbCitas ci,\n" +
                        "tbClientes c,tbDoctores d,tbEspecialidades e where ci.idCliente=c.idCliente and e.idEspecialidad=d.idEspecialidad \n" +
                        "and  ci.idDoctor=d.idDoctor and idCita=?;"
            var st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            /*ps.setInt(1, idCita)

            st = ps.executeQuery()
            st.next()
            txtMedico.setText(st.getString("Doctor"))
            txtEsp.setText(st.getString("especialidad"))
            txtFecha2.setText(st.getString("Fecha"))
            txtHora2.setText(st.getString("Hora"))
            txtNombre.setText(st.getString("Paciente"))
            idCliente = st.getInt("idCliente")*/


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
    fun Habilit(tf: Boolean) {
        txtMascFC.isEnabled =tf
        txtFechaFC.isEnabled =tf
        txtMotivoFC.isEnabled =tf
        txtDoctorFC.isEnabled =tf
        txtNDFC.isEnabled =tf
        txtNCFC.isEnabled =tf
    }

}