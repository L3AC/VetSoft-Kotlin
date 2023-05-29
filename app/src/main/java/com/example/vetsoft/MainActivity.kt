package com.example.vetsoft

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.vetsoft.Conex.conx
import org.w3c.dom.Text
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var txtUsuario1: EditText
lateinit var txtContra1: EditText
lateinit var xvRecup1: TextView
lateinit var btnIngresar1: Button
lateinit var xvCuenta1: TextView

class MainActivity : AppCompatActivity() {
    private var conx = conx()
    private var idUs: Int = 0
    private var idCl: Int = 0

    //conect.dbConn()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtUsuario1 = findViewById(R.id.txtUsuario1)
        txtContra1 = findViewById(R.id.txtContra1)
        xvRecup1= findViewById(R.id.xvRecup1)
        btnIngresar1= findViewById(R.id.btnIngresar1)
        xvCuenta1= findViewById(R.id.xvCuenta1)

        validTxt(txtUsuario1)
        validTxt(txtContra1)
        btnIngresar1.setOnClickListener(){
            val scndAct = Intent(this, BarraNavegar::class.java)
            startActivity(scndAct)
        }
        xvRecup1.setOnClickListener(){

        }
        xvCuenta1.setOnClickListener(){

        }
    }

    fun VerifUs() {
        try {
            val cadena: String = "select * from tbUsuarios where usuario=? " +
                    "COLLATE SQL_Latin1_General_CP1_CS_AS and contra=? COLLATE SQL_Latin1_General_CP1_CS_AS"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, txtUsuario1.text.toString())
            ps.setString(2, txtContra1.text.toString())
            st = ps.executeQuery()
            st.next()

            val found = st.row
            if (found == 1) {
                idUs = st.getInt("idUsuario")

                //Toast.makeText(applicationContext,"Acceso completado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Datos incorrectos", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Errorsito", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }

    fun verifCliente() {
        //val scndAct = Intent(this, MainInside::class.java)
        try {
            val cadena: String = "select idCliente from tbClientes inner join \n" +
                    "tbUsuarios on tbClientes.idUsuario=tbUsuarios.idUsuario \n" +
                    "where tbClientes.idUsuario=?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, idUs.toString())
            st = ps.executeQuery()
            st.next()

            val found = st.row
            if (found == 1) {
                idCl = st.getInt("idCliente")
                /*scndAct.putExtra("idCuenta", idCuenta)
                scndAct.putExtra("idus", idUs)
                startActivity(scndAct)*/
                overridePendingTransition(0, 0)
                Toast.makeText(applicationContext, "Acceso completado", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "Datos incorrectos", Toast.LENGTH_SHORT)
                    .show()
            }

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Errorsito", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    //VERIFICAR QUE SOLO INGRESE NUMEROS Y LETRAS
    fun validTxt(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            val pattern = Regex("^[a-zA-Z0-9]+\$") // Expresión regular para letras y numeros
            if (pattern.matches(source)) {
                source
            } else {
                "" // Si no coincide con la expresión regular, se rechaza el carácter
            }
        }
        editText.filters = arrayOf(filter)
    }

    fun validEmpty(editTextList: List<EditText>): Boolean {
        for (editText in editTextList) {
            val text = editText.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(applicationContext, "Campos vacíos", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}