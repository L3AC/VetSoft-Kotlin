package com.example.vetsoft.Controlador.ui.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

lateinit var txtEspFD: EditText
lateinit var txtNombFD: EditText
lateinit var txtApellFD: EditText
lateinit var txtSexoFD: EditText
lateinit var txtEdadFD: EditText
lateinit var txtCorreoFD: EditText
lateinit var txtTelFD: EditText
lateinit var btnVolverFD: ImageButton

class infoDoc : Fragment() {
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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_doc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtEspFD =requireView().findViewById(R.id.txtEspFD)
        txtNombFD =requireView().findViewById(R.id.txtNombFD)
        txtApellFD =requireView().findViewById(R.id.txtApellFD)
        txtSexoFD =requireView().findViewById(R.id.txtSexoFD)
        txtEdadFD =requireView().findViewById(R.id.txtEdadFD)
        txtCorreoFD =requireView().findViewById(R.id.txtCorreoFD)
        txtTelFD =requireView().findViewById(R.id.txtTelFD)
        btnVolverFD =requireView().findViewById(R.id.btnVolverFD)

        CargarDatos()
        val lista= listOf(txtEspFD,txtNombFD, txtApellFD, txtSexoFD, txtEdadFD, txtCorreoFD,
            txtEdadFD)
        vali.Habilit(lista,false)

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
            putInt("idCit", idCit)
            putInt("idDoc", idDoc)
        }
        btnVolverFD.setOnClickListener(){
            findNavController().navigate(R.id.action_infoDoc_to_infoCita, bundle)
        }
    }
    fun CargarDatos() {
        try {
            var cadena: String =
                "select especialidad,nombre,apellido,telefono,correo,DATEDIFF(YEAR, nacimiento, GETDATE()) AS edad, sexo\n" +
                "from tbDoctores d,tbEspecialidades e,tbUsuarios u where d.idEspecialidad=e.idEspecialidad \n" +
                "and d.idUsuario=u.idUsuario and idDoctor=?"
            var st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, idDoc)

            st = ps.executeQuery()
            st.next()
            txtEspFD.setText(st.getString("especialidad"))
            txtNombFD.setText(st.getString("nombre"))
            txtApellFD.setText(st.getString("apellido"))
            txtSexoFD.setText(st.getString("sexo"))
            txtEdadFD.setText(st.getString("edad"))
            txtCorreoFD.setText(st.getString("correo"))
            txtTelFD.setText(st.getString("telefono"))


        } catch (ex: SQLException) {
            Toast.makeText(context, "Error al mostrar", Toast.LENGTH_SHORT).show()
        }
    }
}