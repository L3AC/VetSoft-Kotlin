package com.example.vetsoft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.vetsoft.Conex.conx
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
        txtContra1 = findViewById(R.id.txtContraN2)
        xvRecup1= findViewById(R.id.xvRecup1)
        btnIngresar1= findViewById(R.id.btnIngresar1)
        xvCuenta1= findViewById(R.id.xvCuenta1)

        /*validTxt(txtUsuario1)
        validTxt(txtContra1)*/
        btnIngresar1.setOnClickListener(){
            val scndAct = Intent(this, BarraNavegar::class.java)
            startActivity(scndAct)
        }
        xvRecup1.setOnClickListener(){

        }
        xvCuenta1.setOnClickListener(){
            val scndAct = Intent(this,CrearCuenta::class.java)
            startActivity(scndAct)
        }
    }

    fun VerifUs() {
        try {
            val cadena: String = "EXEC LoginUs ?,?; "
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, txtUsuario1.text.toString())
            ps.setString(2, txtContra1.text.toString())
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

    fun verifCliente() {
        //val scndAct = Intent(this, MainInside::class.java)
        try {
            val cadena: String = "EXEC UsCliente ?;"
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

}