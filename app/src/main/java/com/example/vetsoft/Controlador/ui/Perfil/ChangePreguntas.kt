package com.example.vetsoft.Controlador.ui.Perfil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Controlador.Cryptation.Crypto
import com.example.vetsoft.Controlador.Recuperacion.txtResp1
import com.example.vetsoft.Controlador.Recuperacion.txtResp2
import com.example.vetsoft.Controlador.Recuperacion.txtResp3
import com.example.vetsoft.Controlador.Recuperacion.txtUsuarioPS
import com.example.vetsoft.Controlador.Recuperacion.txvAdvPS
import com.example.vetsoft.Controlador.Recuperacion.txvPreg1
import com.example.vetsoft.Controlador.Recuperacion.txvPreg2
import com.example.vetsoft.Controlador.Recuperacion.txvPreg3
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var btnVolverPS2: ImageButton
lateinit var btnGuardarPS2: Button
lateinit var btnEditarPS2: Button
lateinit var txtUsuarioPS2: EditText
lateinit var txvPreg1PS2: TextView
lateinit var txtResp1PS2: EditText
lateinit var txvPreg2PS2: TextView
lateinit var txtResp2PS2: EditText
lateinit var txvPreg3PS2: TextView
lateinit var txtResp3PS2: EditText
lateinit var txvAdvPS2: TextView
class ChangePreguntas : Fragment() {

    private var idUs: Int = 0
    private var idCl: Int = 0
    private var pasw:String=""
    private var conx = conx()
    private var crypt= Crypto()
    private var vali = Validat()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs = arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_preguntas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverPS2 =requireView().findViewById(R.id.btnVolverPS2)
        btnEditarPS2 =requireView().findViewById(R.id.btnEditarPS2)
        btnGuardarPS2 =requireView().findViewById(R.id.btnGuardarPS2)
        txvPreg1PS2 =requireView().findViewById(R.id.txvPreg1PS)
        txvPreg2PS2 =requireView().findViewById(R.id.txvPreg2PS)
        txvPreg3PS2 =requireView().findViewById(R.id.txvPreg3PS)
        txtResp1PS2 =requireView().findViewById(R.id.txtResp1PS)
        txtResp2PS2 =requireView().findViewById(R.id.txtResp2PS)
        txtResp3PS2 =requireView().findViewById(R.id.txtResp3PS)

        cargarPreg(txvPreg1PS2,1);cargarPreg(txvPreg2PS2,2)
        cargarPreg(txvPreg3PS2,3)
        cargarResp(txtResp1PS2,1);cargarResp(txtResp2PS2,2)
        cargarResp(txtResp3PS2,3)

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnVolverPS2.setOnClickListener(){
            findNavController().navigate(R.id.action_changePreguntas_to_mainSecurity, bundle)
        }
        val lista= listOf(txvPreg1PS2,txvPreg2PS2,txvPreg3PS2,
            txtResp1PS2,txtResp2PS2,txtResp3PS2)
        btnGuardarPS2.isVisible=false
        vali.Habilit(lista,false)

        btnEditarPS2.setOnClickListener(){
            if (btnGuardarPS2.isVisible) {
                btnEditarPS2.text = "Editar"
                vali.Habilit(lista,false)
                btnGuardarPS2.isVisible=false
            } else {
                btnEditarPS2.text = "Cancelar"
                vali.Habilit(lista,true)
                btnGuardarPS2.isVisible=true
            }
        }
        btnGuardarPS2.setOnClickListener(){
            val editTextList = listOf(
                txtResp1PS2, txtResp2PS2, txtResp3PS2
            )
            val areFieldsValid  = vali.areFieldsNotEmpty(editTextList)
            if(areFieldsValid){
                if(verifExist(1)&&verifExist(2)&&verifExist(3)){
                    updatePreg(txtResp1PS2,1);updatePreg(txtResp2PS2,2)
                    updatePreg(txtResp3PS2,3);
                }
                else{
                    insertPreg(txtResp1PS2,1);insertPreg(txtResp2PS2,2)
                    insertPreg(txtResp3PS2,3);
                }

            }
            else{
                Toast.makeText(requireContext(), "Campos vacios", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun cargarPreg(textV:TextView,idPreg:Int) {
        try {
            var st: ResultSet
            val cadena ="select * from tbPreguntas where idPregunta=?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idPreg)
            st = ps.executeQuery()
            st.next()
            textV.setText(st.getString("enunciado"))

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(requireContext(), "Error al cargar1", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    fun cargarResp(textV:EditText,idPreg:Int) {
        try {
            var st: ResultSet
            val cadena ="select * from tbPreguntasUsuarios where idUsuario=? and idPregunta=?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idUs)
            ps.setInt(2,idPreg)
            st = ps.executeQuery()

            if(st.next()){
                textV.setText(st.getString("respuesta"))
            }
            else{

                textV.setHint("No hay respuesta ingresada")
            }

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(requireContext(), "Error al cargar2", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    fun verifExist(idPreg:Int):Boolean {
        try {
            var st: ResultSet
            val cadena ="select * from tbPreguntasUsuarios where idUsuario=? and idPregunta=?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idUs)
            ps.setInt(2,idPreg)
            st = ps.executeQuery()

            if(st.next()){
                return true
            }
            else{
                return false
            }

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(requireContext(), "Error al cargar", Toast.LENGTH_SHORT).show()
            return false
        }
        conx.dbConn()!!.close()
    }
    fun updatePreg(txt:EditText,idPreg: Int) {
        try {
            val cadena =
                "update tbPreguntasUsuarios set respuesta=? " +
                        "where idUsuario=? and idPregunta=?"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!


            ps.setString(1, txt.text.toString())
            ps.setInt(2, idUs)
            ps.setInt(3, idPreg)
//
            ps.executeUpdate()
            Toast.makeText(context, "Campos actualizados", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "No se pudo actualizar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    fun insertPreg(txt:EditText,idPreg: Int) {
        try {
            val cadena =
                "insert into tbPreguntasUsuarios values(?,?,?);"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, idPreg)
            ps.setString(2, txt.text.toString())
            ps.setInt(3, idUs)
            ps.executeUpdate()
            //Toast.makeText(context, "Campos actualizados", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "No se pudo actualizar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }


}