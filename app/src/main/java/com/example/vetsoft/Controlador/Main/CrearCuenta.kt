package com.example.vetsoft.Controlador.Main

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.Controlador.Cryptation.Crypto
import com.example.vetsoft.Controlador.ui.Perfil.txtNaciDP
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.validation.Validat
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

lateinit var txtUsuario2:EditText
lateinit var txtContraN2:EditText
lateinit var txtContraD2:EditText
lateinit var txtCorreo2:EditText
lateinit var txtNomb2:EditText
lateinit var txtApellidos2:EditText
lateinit var txtTel2:EditText
lateinit var txtDui2:EditText
lateinit var spinSexo2:Spinner
lateinit var txtNaci2:EditText
lateinit var btnNaci2:ImageButton
lateinit var btnVolver2:ImageButton
lateinit var txvCont2:TextView
lateinit var txvUs2:TextView
lateinit var btnConfirm2:Button
lateinit var btnMirar2:ImageButton

class CrearCuenta : AppCompatActivity() {

    private var conx = conx()
    private var vali= Validat()
    private var crypt= Crypto()
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var fechaSql: String = ""
    var contraVisible = false
    val sexo = listOf("Femenino", "Masculino")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        txtUsuario2 =findViewById(R.id.txtUsuario2)
        txtContraN2 =findViewById(R.id.txtContraN2)
        txtContraD2 =findViewById(R.id.txtContraD2)
        txtCorreo2 =findViewById(R.id.txtCorreo2)
        txtNomb2 =findViewById(R.id.txtNomb2)
        txtApellidos2 =findViewById(R.id.txtApellidos2)
        txtTel2 =findViewById(R.id.txtTel2)
        txtDui2 =findViewById(R.id.txtDui2)
        spinSexo2 =findViewById(R.id.spinSexo2)
        txtNaci2 =findViewById(R.id.txtNaci2)
        btnNaci2 =findViewById(R.id.btnNaci2)
        btnVolver2 =findViewById(R.id.btnVolver2)
        txvCont2 =findViewById(R.id.txvCont2)
        txvUs2 =findViewById(R.id.txvUs2)
        btnConfirm2 =findViewById(R.id.btnConfirm2)
        btnMirar2 =findViewById(R.id.btnMirar2)

        LLenarSpin()
        txvUs2.isVisible=false
        txvCont2.isVisible=false//Advertencias
        txtNaci2.isEnabled=false

        vali.configEditText(txtUsuario2,15,"^[a-zA-Z0-9]+$")
        vali.configEditText(txtContraN2,20,"^[a-zA-Z0-9]+$")
        vali.configEditText(txtContraD2,20,"^[a-zA-Z0-9]+$")
        vali.configEditText(txtNomb2,30,"[a-zA-Z\\s]+")
        vali.configEditText(txtApellidos2,30,"[a-zA-Z\\s]+")
        vali.configEditText(txtTel2,8,"[0-9]+")
        vali.configEditText(txtDui2,10,"[0-9]+")

        btnConfirm2.setOnClickListener(){
            val editTextList = listOf(
                txtUsuario2, txtContraN2,
                txtContraD2, txtCorreo2, txtNomb2, txtApellidos2, txtTel2, txtDui2
            )
            val areFieldsValid  = vali.areFieldsNotEmpty(editTextList)
            if(areFieldsValid){
                createUs()
                selectUs()
                createCl()
                Toast.makeText(applicationContext, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                val scndAct = Intent(this, MainActivity::class.java)
                startActivity(scndAct)
            }
            else{
                Toast.makeText(applicationContext, "Campos vacíos", Toast.LENGTH_SHORT).show()
            }
        }

        btnNaci2.setOnClickListener(){
            val Calendario =
                DatePickerFragment { year, month, day -> verResultado(year, month, day) }
            Calendario.show(supportFragmentManager, "DatePicker")
        }

//CADA VEZ QUE ESCRIBA SE MANDA A LLAMAR LA FUNCION
        txtUsuario2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifUs()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtCorreo2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vali.validateEmail(txtCorreo2, btnConfirm2)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtContraN2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifContra()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtContraD2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifContra()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        btnMirar2.setOnClickListener{
            contraVisible = !contraVisible
            if (contraVisible) {
                txtContraN2.inputType = InputType.TYPE_CLASS_TEXT
                txtContraD2.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                txtContraN2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                txtContraD2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            txtContraN2.setSelection(txtContraN2.text.length)
            txtContraD2.setSelection(txtContraD2.text.length)
        }

    }
    fun verifUs() {
        try {
            val cadena: String = "EXEC selectUsN ?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, txtUsuario2.text.toString())
            st = ps.executeQuery()
            st.next()

            val found = st.row
            if (found == 1) {
                txvUs2.isVisible = true
                btnConfirm2.isEnabled = false
                Toast.makeText(applicationContext, "Ya existe usuario", Toast.LENGTH_SHORT).show()

            } else {
                txvUs2.isVisible = false
                btnConfirm2.isEnabled = true
            }
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Error interno", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
//CAMBIAR
@RequiresApi(Build.VERSION_CODES.O)
fun createUs() {
        try {
            val cadena: String = "EXEC insertUs ?,?,?,?,?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, 3)
            ps.setString(2, txtUsuario2.text.toString())
            ps.setString(3, crypt.encrypt(txtContraN2.text.toString(),"key"))
            ps.setString(4, txtCorreo2.text.toString())
            ps.setString(5, txtTel2.text.toString())
            ps.executeUpdate()

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Errorsito", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()

    }
    fun selectUs(){
        try {
            val cadena: String = "EXEC selectUsB ?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, txtUsuario2.text.toString())
            st = ps.executeQuery()
            st.next()

            val found = st.row
            if (found == 1) {
                idUs = st.getInt("idUsuario")

            } else {
                Toast.makeText(applicationContext, "Error de inserción", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Error interno", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }

    fun createCl() {

        try {
            val cadena: String =
                "EXEC insertClientes ?,?,?,?,?,?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, idUs)
            ps.setString(2, txtNomb2.text.toString())
            ps.setString(3, txtApellidos2.text.toString())
            ps.setString(4, txtDui2.text.toString())
            ps.setString(5, fechaSql)
            ps.setString(6, spinSexo2.selectedItem.toString())
            ps.executeUpdate()

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Errorsito", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()

    }
    fun LLenarSpin() {
        val adaptadorSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexo)
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = findViewById<Spinner>(R.id.spinSexo2)
        spinner.adapter = adaptadorSpinner
    }
    private fun verResultado(year: Int, month: Int, day: Int) {
        val mes = month + 1
        fechaSql = "$year-$mes-$day"
        txtNaci2?.setText("$day-$mes-$year")

    }
    class DatePickerFragment(val listener: (year: Int, month: Int, day: Int) -> Unit) :
        DialogFragment(),
        DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
            listener(year, month, day)
        }
    }
    fun verifContra() {
        if (txtContraN2.text.toString() != txtContraD2.text.toString()) {
            txvCont2.isVisible=true
            btnConfirm2.isEnabled=false
        }
        else {
            txvCont2.isVisible=false
            btnConfirm2.isEnabled=true
        }
    }
    override fun onBackPressed() {
        // Deja vacío este método
    }

}