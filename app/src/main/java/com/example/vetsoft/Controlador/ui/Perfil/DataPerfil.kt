package com.example.vetsoft.Controlador.ui.Perfil

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Controlador.Main.*
import com.example.vetsoft.Controlador.ui.Home.*
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.google.android.material.switchmaterial.SwitchMaterial


lateinit var txtUsDP: EditText
lateinit var txtNombDP: EditText
lateinit var txtApellDP: EditText
lateinit var txtCorreoDP: EditText
lateinit var txtTelDP: EditText
lateinit var spSexoDP: Spinner
lateinit var txtNaciDP: EditText
lateinit var txtDuiDP: EditText
lateinit var txtDirDP: EditText
lateinit var btnNaciDP: ImageButton
lateinit var btnVolverDP: ImageButton
lateinit var txvUsDP: TextView
lateinit var btnActDP: Button
lateinit var btnGuardarDP: Button
lateinit var btnCerrarSesion: Button
lateinit var selectedDate: Calendar

class DataPerfil : Fragment(){
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var user:String=""
    private var conx = conx()
    private var vali = Validat()
    val sexo = listOf("Femenino", "Masculino")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs = arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!

            /*swDarkMode.setOnCheckedChangeListener {_, isSelected ->
                if (isSelected){
                    enableDarkMode()
                }
                else{
                    disableDarkMode()
                }
            }*/
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_perfil, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtUsDP =requireView().findViewById(R.id.txtUsDP)
        txtNombDP =requireView().findViewById(R.id.txtNombDP)
        txtApellDP =requireView().findViewById(R.id.txtApellDP)
        txtCorreoDP =requireView().findViewById(R.id.txtCorreoDP)
        txtTelDP =requireView().findViewById(R.id.txtTelDP)
        spSexoDP=requireView().findViewById(R.id.spSexoDP)
        txtNaciDP =requireView().findViewById(R.id.txtNaciDP)
        btnNaciDP =requireView().findViewById(R.id.btnNaciDP)
        btnVolverDP =requireView().findViewById(R.id.btnVolverDP)
        txvUsDP =requireView().findViewById(R.id.txvUsDP)
        txtDuiDP =requireView().findViewById(R.id.txtDuiDP)
        txtDirDP =requireView().findViewById(R.id.txtDirDP)
        btnActDP =requireView().findViewById(R.id.btnActDP)
        btnGuardarDP =requireView().findViewById(R.id.btnGuardarDP)
        txvUsDP.isVisible=false
        btnGuardarDP.isVisible=false

        cargarData()
        //HABILI DESABILIT
        val lista= listOf(txtUsDP,txtNombDP,txtApellDP,txtCorreoDP,txtNaciDP,spSexoDP,txtTelDP,txtDuiDP,btnNaciDP)
        vali.Habilit(lista,false)

        vali.configEditText(txtUsDP,15,"^[a-zA-Z0-9]+$")
        vali.configEditText(txtNombDP,30,"[a-zA-Z\\s]+")
        vali.configEditText(txtApellDP,30,"[a-zA-Z\\s]+")
        vali.configEditText(txtTelDP,8,"[0-9]+")
        vali.configEditText(txtDuiDP,10,"[0-9-]+")
        vali.configEditText(txtDirDP,300,"[a-zA-Z\\s]+")



        btnActDP.setOnClickListener(){
            if (btnGuardarDP.isVisible) {
                btnActDP.text = "Editar"
                vali.Habilit(lista,false)
                btnGuardarDP.isVisible=false
            } else {
                btnActDP.text = "Cancelar"
                vali.Habilit(lista,true)
                btnGuardarDP.isVisible=true
            }
        }
        btnNaciDP.setOnClickListener(){
            showDatePickerDialog()
        }
        btnGuardarDP.setOnClickListener(){
            val allFieldsValid2 = txtUsDP.text.length >= 4 &&
                    txtNombDP.text.length >= 5 &&
                    txtDirDP.text.length >= 5 &&
                    txtApellDP.text.length >= 5 &&
                    txtDuiDP.text.length >= 10 &&
                    txtTelDP.text.length >= 8 &&
                    txtCorreoDP.text.length >= 10

            val editTextList = listOf(
                txtUsDP, txtCorreoDP, txtNombDP, txtApellDP, txtTelDP, txtDuiDP
            )
            val areFieldsValid  = vali.areFieldsNotEmpty(editTextList)
            if(areFieldsValid && allFieldsValid2){
                updateData()
            }
            else{
                Toast.makeText(requireContext(), "Campos vacios o incompletos", Toast.LENGTH_SHORT).show()
            }
        }
        txtDuiDP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (txtDuiDP.text.length >= 10) {
                } else {
                    txtDuiDP.error = "10 caracteres minimo"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtDirDP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (txtDirDP.text.length >= 5) {
                } else {
                    txtDirDP.error = "5 caracteres minimo"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtTelDP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (txtTelDP.text.length >= 8) {
                } else {
                    txtTelDP.error = "8 caracteres minimo"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtNombDP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (txtNombDP.text.length >= 5) {
                } else {
                    txtNombDP.error = "5 caracteres minimo"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtApellDP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (txtApellDP.text.length >= 5) {
                } else {
                    txtApellDP.error = "5 caracteres minimo"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtUsDP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //VERIFICAR QUE EL USUARIO QUE ESTA CARGADO NO HAYA PROBLEMA SI ENCUENTRA EL MISMO EN LA CONSULTA
                if(user==txtUsDP.text.toString()){
                    txvUsDP.isVisible = false
                    btnGuardarDP.isEnabled = true
                }
                else{
                    verifUs()
                }
                if (txtUsDP.text.length >= 4) {
                } else {
                    txtUsDP.error = "4 caracteres minimo"
                }

            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtCorreoDP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vali.validateEmail(txtCorreoDP, btnGuardarDP)
                if (txtCorreoDP.text.length >= 10) {
                } else {
                    txtCorreoDP.error = "10 caracteres minimo"
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnVolverDP.setOnClickListener(){
            findNavController().navigate(R.id.action_dataPerfil_to_perfilMain, bundle)
        }
    }

    //CARGAR INFORMACION DE LA TABLA DE USUARIOS Y CLIENTES DEPENDIENDO DEL INICIO DE SESION
    fun cargarData() {
        try {
            val adpt = LLenarSpin()
            var st: ResultSet
            val cadena =
                "select * from tbUsuarios u, tbClientes c where c.idUsuario=u.idUsuario \n" +
                        "and c.idUsuario=?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idUs)
            st = ps.executeQuery()
            st.next()
            txtUsDP.setText(st.getString("usuario"))
            user=st.getString("usuario")
            txtNombDP.setText(st.getString("nombre"))
            txtApellDP.setText(st.getString("apellido"))
            txtCorreoDP.setText(st.getString("correo"))
            txtTelDP.setText(st.getString("telefono"))
            spSexoDP.setSelection(adpt.getPosition(st.getString("sexo")))
            txtNaciDP.setText(st.getString("nacimiento"))
            txtDuiDP.setText(st.getString("DUI"))
            txtDirDP.setText(st.getString("direccion"))


        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "Error al cargar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
        //VERIFICAR SI EXISTA YA EL USUARIO
    fun verifUs() {
        try {
            val cadena: String = " SELECT *FROM tbUsuarios " +
                    "    WHERE usuario = ?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, txtUsDP.text.toString())
            st = ps.executeQuery()
            st.next()
            val found = st.row
            if (found == 1) {
                txvUsDP.isVisible = true
                btnGuardarDP.isEnabled = false

            } else {
                txvUsDP.isVisible = false
                btnGuardarDP.isEnabled = true
            }
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(requireContext(), "Error interno", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    //ACTUALIZAR LA INFO
    fun updateData() {
        try {
            val cadena =
                "update tbUsuarios set  usuario=?,correo=?,telefono=? where idUsuario=?;"+
                "update tbClientes set nombre= ?,apellido= ?,DUI= ?," +
                "nacimiento= ?,sexo= ?, direccion=? where idCliente=?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!


            ps.setString(1, txtUsDP.text.toString())
            ps.setString(2, txtCorreoDP.text.toString())
            ps.setString(3, txtTelDP.text.toString())
            ps.setInt(4,idUs)
            ps.setString(5, txtNombDP.text.toString())
            ps.setString(6, txtApellDP.text.toString())
            ps.setString(7, txtDuiDP.text.toString())
            ps.setString(8, txtNaciDP.text.toString())
            ps.setString(9, spSexoDP.selectedItem.toString())
            ps.setString(10, txtDirDP.text.toString())
            ps.setInt(11, idCl)

//
            ps.executeUpdate()
            Toast.makeText(context, "Campos actualizados", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "No se pudo actualizar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }

        //CARGAR LA LISTA
    fun LLenarSpin(): ArrayAdapter<String> {
        val adaptadorSpinner =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sexo)
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = requireView().findViewById<Spinner>(R.id.spSexoDP)
        spinner.adapter = adaptadorSpinner
        return adaptadorSpinner
    }
    //CREAR LA PANTALLA DEL CALENDARIO
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear un DatePickerDialog con la fecha actual y la fecha mínima permitida
        val datePickerDialog = android.app.DatePickerDialog(
            requireContext(),
            android.app.DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // Aquí puedes hacer algo con la fecha seleccionada, como guardarla en una variable
                val mes = month + 1
                txtNaciDP.setText("$year-$mes-$day")

                // También puedes mostrarla en un TextView, etc.
            },
            year, month, dayOfMonth
        )

        // 18 años=6570 dias
        calendar.add(Calendar.DAY_OF_MONTH,-6570)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }


}