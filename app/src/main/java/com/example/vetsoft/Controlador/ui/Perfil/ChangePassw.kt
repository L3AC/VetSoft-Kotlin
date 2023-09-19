package com.example.vetsoft.Controlador.ui.Perfil

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Controlador.Cryptation.Crypto
import com.example.vetsoft.Controlador.Main.MainActivity
import com.example.vetsoft.Controlador.Recuperacion.btnConfirmCC
import com.example.vetsoft.Controlador.Recuperacion.btnMirarCC
import com.example.vetsoft.Controlador.Recuperacion.btnVerifCC
import com.example.vetsoft.Controlador.Recuperacion.btnVolverCC
import com.example.vetsoft.Controlador.Recuperacion.txtContra1CC
import com.example.vetsoft.Controlador.Recuperacion.txtContra2CC
import com.example.vetsoft.Controlador.Recuperacion.txtContraCC
import com.example.vetsoft.Controlador.Recuperacion.txtUsuarioPS
import com.example.vetsoft.Controlador.Recuperacion.txvAdvCC
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var btnVolverCP2: ImageButton
lateinit var txtContraCP2: EditText
lateinit var txtContra1CP2: EditText
lateinit var txtContra2CP2: EditText
lateinit var btnVerifCP2: Button
lateinit var btnConfirmCP2: Button
lateinit var txvAdvCP2: TextView
lateinit var txvPIN: TextView
lateinit var btnMirarCP2: ImageButton
class ChangePassw : Fragment() {
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var pasw=""
    private var conx = conx()
    private var crypt= Crypto()
    private var vali = Validat()
    private var contraVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs= arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_passw, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverCP2 =requireView().findViewById(R.id.btnVolverCP2)
        btnVerifCP2 =requireView().findViewById(R.id.btnVerifCP2)
        btnConfirmCP2=requireView().findViewById(R.id.btnConfirmCP2)
        txtContraCP2 =requireView().findViewById(R.id.txtContraCP2)
        txtContra1CP2 =requireView().findViewById(R.id.txtContra1CP2)
        txtContra2CP2 =requireView().findViewById(R.id.txtContra2CP2)
        txvAdvCP2 =requireView().findViewById(R.id.txvAdvCP2)
        txvPIN =requireView().findViewById(R.id.txvPIN)
        btnMirarCP2 =requireView().findViewById(R.id.btnMirarCP2)

        txvAdvCP2.isVisible=false
        val lista= listOf(txtContra1CP2,txtContra2CP2,btnConfirmCP2,btnMirarCP2)
        vali.Visib(lista,false)

        btnVerifCP2.setOnClickListener(){
            verifUsc()
            if(txtContraCP2.text.toString()==pasw){
                vali.Visib(lista,true)
            }
            else{
                vali.Visib(lista,false)
                Toast.makeText(requireContext(), "Contraseña incorrecta",Toast.LENGTH_SHORT).show()
            }
        }
        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnConfirmCP2.setOnClickListener(){
            updateC()
        }
        btnVolverCP2.setOnClickListener(){
            findNavController().navigate(R.id.action_changePassw_to_mainSecurity, bundle)
        }
        txtContra1CP2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifContra()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtContra2CP2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifContra()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        btnMirarCP2.setOnClickListener{
            contraVisible = !contraVisible
            if (contraVisible) {
                txtContra1CP2.inputType = InputType.TYPE_CLASS_TEXT
                txtContra2CP2.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                txtContra1CP2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                txtContra2CP2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            txtContra1CP2.setSelection(txtContra1CP2.text.length)
            txtContra2CP2.setSelection(txtContra2CP2.text.length)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun verifUsc() {
        try {
            val cadena: String = "SELECT *FROM tbUsuarios" +
                    "    WHERE idUsuario = ? ;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1,idUs)

            st = ps.executeQuery()
            st.next()

            val found = st.row

            if (found == 1) {
                pasw=crypt.decrypt(st.getString("contraseña"),"key")
                txvPIN.text="PIN "+st.getString("codigoVerif")
                Log.i("contra",pasw)
            } else {
                Toast.makeText(requireContext(), "Error al cargar", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: SQLException) {
            Log.e("Error L010 ", ex.message!!)
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateC() {
        try {
            val cadena: String = "update tbUsuarios set contraseña=? where idUsuario=?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(2, idUs)
            ps.setString(1, crypt.encrypt(txtContra1CP2.text.toString(),"key"))
            ps.executeUpdate()
            Toast.makeText(requireContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply {
                putInt("idUs", idUs)
                putInt("idCl", idCl)
            }
            findNavController().navigate(R.id.action_changePassw_to_mainSecurity, bundle)
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    fun verifContra() {
        if (txtContra1CP2.text.toString() != txtContra2CP2.text.toString()) {
            txvAdvCP2.isVisible=true
            btnConfirmCP2.isEnabled=false
        }
        else {
            txvAdvCP2.isVisible=false
            btnConfirmCP2.isEnabled=true
        }
    }


}