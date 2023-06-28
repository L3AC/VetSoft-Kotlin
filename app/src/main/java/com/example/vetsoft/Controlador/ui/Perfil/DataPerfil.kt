package com.example.vetsoft.Controlador.ui.Perfil

import android.os.Bundle
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
import com.example.vetsoft.Controlador.Main.btnConfirm2
import com.example.vetsoft.Controlador.Main.btnMirar2
import com.example.vetsoft.Controlador.Main.btnVolver2
import com.example.vetsoft.Controlador.Main.txtNaci2
import com.example.vetsoft.Controlador.Main.txvCont2
import com.example.vetsoft.Controlador.Main.txvUs2
import com.example.vetsoft.Controlador.ui.Home.txvAniF5
import com.example.vetsoft.Controlador.ui.Home.txvEdadF5
import com.example.vetsoft.Controlador.ui.Home.txvNombF5
import com.example.vetsoft.Controlador.ui.Home.txvPesoF5
import com.example.vetsoft.Controlador.ui.Home.txvRazaF5
import com.example.vetsoft.Controlador.ui.Home.txvSexoF5
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

lateinit var txtUsDP: EditText
lateinit var txtNombDP: EditText
lateinit var txtApellDP: EditText
lateinit var txtCorreoDP: EditText
lateinit var txtTelDP: EditText
lateinit var spSexoDP: Spinner
lateinit var txtNaciDP: EditText
lateinit var txtDuiDP: EditText
lateinit var btnNaciDP: ImageButton
lateinit var btnVolverDP: ImageButton
lateinit var txvUsDP: TextView
lateinit var btnActDP: Button
lateinit var btnGuardarDP: Button
lateinit var selectedDate: Calendar
class DataPerfil : Fragment(), DatePickerDialog.OnDateSetListener {
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var conx = conx()
    private var vali = Validat()
    val sexo = listOf("Femenino", "Masculino")
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
        btnActDP =requireView().findViewById(R.id.btnActDP)
        btnGuardarDP =requireView().findViewById(R.id.btnGuardarDP)

        txvUsDP.isVisible=false
        btnGuardarDP.isVisible=false

        cargarData()
        Habilit(false)
        btnActDP.setOnClickListener(){
            if (btnGuardarDP.isVisible) {
                btnActDP.text = "Editar"
                Habilit(false)
            } else {
                btnActDP.text = "Cancelar"
                Habilit(true)
            }
        }
        btnNaciDP.setOnClickListener(){
            showDatePicker()
        }
        btnGuardarDP.setOnClickListener(){
           // updateData()
        }


    }
    fun cargarData() {
        try {
            val adpt = LLenarSpin()
            var st: ResultSet
            val cadena =
                "EXEC loadUsCl ?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idUs)
            st = ps.executeQuery()
            st.next()
            txtUsDP.setText(st.getString("usuario"))
            txtNombDP.setText(st.getString("nombre"))
            txtApellDP.setText(st.getString("apellido"))
            txtCorreoDP.setText(st.getString("correo"))
            txtTelDP.setText(st.getString("apellido"))
            spSexoDP.setSelection(adpt.getPosition(st.getString("sexo")))
            txtNaciDP.setText(st.getString("nacimiento"))
            txtDuiDP.setText(st.getString("DUI"))


        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "Error al cargar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    fun updateData() {
        try {
            val cadena =
                "EXEC updtUs ?,?,?,?;"+
                "EXEC updtCl ?,?,?,?,?,?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1,idUs)
            ps.setString(2, txtUsDP.text.toString())
            ps.setString(3, txtCorreoDP.text.toString())
            ps.setString(4, txtTelDP.text.toString())
            ps.setInt(5, idCl)
            ps.setString(6, txtNombDP.text.toString())
            ps.setString(7, txtApellDP.text.toString())
            ps.setString(8, txtDuiDP.text.toString())
            ps.setString(9, txtNaciDP.text.toString())
            ps.setString(10, spSexoDP.selectedItem.toString())
//
            ps.executeUpdate()
            Toast.makeText(context, "Campos actualizados", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "No se pudo actualizar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    fun LLenarSpin(): ArrayAdapter<String> {
        val adaptadorSpinner =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sexo)
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = requireView().findViewById<Spinner>(R.id.spSexoDP)
        spinner.adapter = adaptadorSpinner
        return adaptadorSpinner
    }
    private fun showDatePicker() {
        val currentDate = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog.newInstance(
            this,
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )

        // Configurar la fecha mínima y máxima permitida (opcional)
        // datePickerDialog.minDate = Calendar.getInstance() // Fecha mínima: hoy
        datePickerDialog.maxDate = currentDate // Fecha máxima: fecha actual

        datePickerDialog.show(requireFragmentManager(), "DatePickerDialog")
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year, monthOfYear, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)
        txtNaciDP.setText(formattedDate)
    }
    fun Habilit(tf: Boolean) {
        txtUsDP.isEnabled = tf
        txtNombDP.isEnabled=tf
        txtApellDP.isEnabled = tf
        txtCorreoDP.isEnabled = tf
        txtNaciDP.isEnabled = tf
        spSexoDP.isEnabled = tf
        txtTelDP.isEnabled = tf
        txtDuiDP.isEnabled = tf
        btnNaciDP.isEnabled = tf
        btnGuardarDP.isVisible = tf
    }

}