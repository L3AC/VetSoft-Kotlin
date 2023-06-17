package com.example.vetsoft

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.vetsoft.Conex.conx
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
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

class CrearCuenta : AppCompatActivity() {
    fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun validateEmail(editText: EditText): Boolean {
        val email = editText.text.toString().trim()
        val isValid = isEmailValid(email)
        if (!isValid) {
            editText.error = "Correo electrónico inválido"

        } else {
            editText.error = null
        }
        return isValid
    }
    fun setupET(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            val pattern = Regex("[a-zA-Z\\s]*") // Expresión regular para letras y espacios
            if (pattern.matches(source)) {
                source
            } else {
                "" // Si no coincide con la expresión regular, se rechaza el carácter
            }
        }
        editText.filters = arrayOf(filter)
    }
    fun setupUC(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            val pattern = Regex("^[a-zA-Z0-9]+$") // Expresión regular para letras y numeros
            if (pattern.matches(source)) {
                source
            } else {
                "" // Si no coincide con la expresión regular, se rechaza el carácter
            }
        }
        editText.filters = arrayOf(filter)
    }
    fun setupNumb(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            val pattern = Regex("[0-9]+") // Expresión regular para numeros
            if (pattern.matches(source)) {
                source
            } else {
                "" // Si no coincide con la expresión regular, se rechaza el carácter
            }
        }
        editText.filters = arrayOf(filter)
    }
    fun areFieldsNotEmpty(editTextList: List<EditText>): Boolean {
        for (editText in editTextList) {
            val text = editText.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(applicationContext, "Campos vacíos", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
    private var conx = conx()
    private var idUs: Int = 0
    private var idCl: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        txtUsuario2=findViewById(R.id.txtUsuario2)
        txtContraN2=findViewById(R.id.txtContraN2)
        txtContraD2=findViewById(R.id.txtContraD2)
        txtCorreo2=findViewById(R.id.txtCorreo2)
        txtNomb2=findViewById(R.id.txtNomb2)
        txtApellidos2=findViewById(R.id.txtApellidos2)
        txtTel2=findViewById(R.id.txtTel2)
        txtDui2=findViewById(R.id.txtDui2)
        spinSexo2=findViewById(R.id.spinSexo2)
        txtNaci2=findViewById(R.id.txtNaci2)
        btnNaci2=findViewById(R.id.btnNaci2)
        btnVolver2=findViewById(R.id.btnVolver2)
        txvCont2=findViewById(R.id.txvCont2)
        txvUs2=findViewById(R.id.txvUs2)
        btnConfirm2=findViewById(R.id.btnConfirm2)

        txvUs2.isVisible=false
        txvCont2.isVisible=false//Advertencias
        txtNaci2.isEnabled=false

        setupUC(txtUsuario2);setupUC(txtContraN2);setupUC(txtContraD2);
        setupET(txtNomb2);setupET(txtApellidos2);
        setupNumb(txtTel2);setupNumb(txtDui2)

        btnConfirm2.setOnClickListener(){

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
                validateEmail(txtCorreo2)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

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
                Toast.makeText(applicationContext, "Ya existe usuario", Toast.LENGTH_SHORT).show()

            } else {
                txvUs2.isVisible = false
                btnConfirm2.isEnabled = false
            }
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Error interno", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
//CAMBIAR
    fun createUs() {

        try {
            val cadena: String = "EXEC insertUs ?,?,?,?,?,?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            /*ps.setString(1, usu.text.toString())
            ps.setString(2, contra2.text.toString())
            ps.setString(3, correo.text.toString())*/

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

           // ps.setString(1, usu.text.toString())
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

            ps.setString(1, idUs.toString())
           /* ps.setString(2, nomb.text.toString())
            ps.setString(3, apell.text.toString())
            ps.setString(4, tpdoc.selectedItem.toString())
            ps.setString(5, ndoc.text.toString())
            ps.setString(6, fechaSql)
            ps.setString(7, tpsexo.selectedItem.toString())
            ps.setString(8, tel.text.toString())
            ps.setString(9, tpsangre.text.toString())
            if(patol.text.toString()==""){
                ps.setString(10, "Ninguna")
            }
            else{
                ps.setString(10, patol.text.toString())
            }*/
            ps.executeUpdate()

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Errorsito", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()

    }


}