package com.example.vetsoft.Controlador.Main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Controlador.Cryptation.Crypto
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var txtUsuario1: EditText
lateinit var txtContra1: EditText
lateinit var xvRecup1: TextView
lateinit var btnIngresar1: Button
lateinit var xvCuenta1: TextView
lateinit var btnMirar1:ImageButton

class MainActivity : AppCompatActivity() {
    private var conx = conx()
    private var lol= Int
    private var vali = Validat()
    private var crypt= Crypto()
    private var idUs: Int = 0
    private var idCl: Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    var contraVisible = false

    //conect.dbConn()
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtUsuario1 = findViewById(R.id.txtUsuario1)
        txtContra1 = findViewById(R.id.txtContra1)
        xvRecup1 = findViewById(R.id.xvRecup1)
        btnIngresar1 = findViewById(R.id.btnIngresar1)
        xvCuenta1 = findViewById(R.id.xvCuenta1)
        btnMirar1 =findViewById(R.id.btnMirar1)

        //DIFERENT

        vali.configEditText(txtUsuario1,15,"^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ´]+$")
        vali.configEditText(txtContra1,20,"^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ´]+$")


        btnIngresar1.setOnClickListener() {
        val editTextList = listOf(txtUsuario1, txtContra1)
            val areFieldsValid = vali.areFieldsNotEmpty(editTextList)
            if (areFieldsValid) {
                VerifUs()
                verifCliente()

            } else {
                Toast.makeText(applicationContext, "Campos vacíos", Toast.LENGTH_SHORT).show()
            }
        }
        xvRecup1.setOnClickListener() {
            val scndAct = Intent(this, MainRecup::class.java)
            startActivity(scndAct)
        }
        xvCuenta1.setOnClickListener() {
            val scndAct = Intent(this, CrearCuenta::class.java)
            startActivity(scndAct)
        }
        btnMirar1.setOnClickListener(){
            contraVisible = !contraVisible
            if (contraVisible) {
                txtContra1.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                txtContra1.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            txtContra1.setSelection(txtContra1.text.length)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    //VERIFICAR QUE LAS CREDENCIALES SEAN CORRECTAS
    fun VerifUs() {
        try {
            val cadena: String = "SELECT *FROM tbUsuarios " +
                    " WHERE usuario = ? COLLATE SQL_Latin1_General_CP1_CS_AS" +
                    " and contraseña = ? COLLATE SQL_Latin1_General_CP1_CS_AS and idTipoUsuario=3; "
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, txtUsuario1.text.toString())
            ps.setString(2, crypt.encrypt(txtContra1.text.toString(),"key"))//ENCRIPTADO

            st = ps.executeQuery()
            st.next()

            val found = st.row

            if (found == 1) {
                idUs = st.getInt("idUsuario")
            } else {
                Toast.makeText(applicationContext, "Datos incorrectos", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: SQLException) {
            Log.e("Error L010 ", ex.message!!)
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }

    //VERIFICAR SI EXISTE CLIENTE Y USUARIO PARA EL INICIO DE SESION
    fun verifCliente() {
        try {
            val cadena: String = "select idCliente from tbClientes inner join " +
                    "   tbUsuarios on tbClientes.idUsuario=tbUsuarios.idUsuario " +
                    "   where tbClientes.idUsuario=?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, idUs.toString())
            st = ps.executeQuery()
            st.next()

            val found = st.row
            if (found == 1) {
                idCl = st.getInt("idCliente")
                val scndAct = Intent(this, BarraNavegar::class.java)
                scndAct.putExtra("idCl", idCl)
                scndAct.putExtra("idUs", idUs)
                scndAct.putExtra("idUs", idUs)
                startActivity(scndAct)
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
    override fun onBackPressed() {
        // Deja vacío este método
    }

}