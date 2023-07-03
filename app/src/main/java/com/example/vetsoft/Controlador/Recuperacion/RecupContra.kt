package com.example.vetsoft.Controlador.Recuperacion

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.SQLException
import kotlin.random.Random

lateinit var btnEnviarRecu: Button
lateinit var txtUsuarioRecu: EditText

class RecupContra : AppCompatActivity() {

    private var conx = conx()
    private var vali= Validat()
    private lateinit var correo: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recup_contra)

        btnEnviarRecu = findViewById(R.id.btnEnviarRecu)
        txtUsuarioRecu = findViewById(R.id.txtUsuarioRecu)

        btnEnviarRecu.setOnClickListener {

            //codigo aleatorio
            val codigoAleatorio = vali.GenerC(8)

            //trae el correo
            try{
                val traerCorreo: PreparedStatement = conx.dbConn()?.prepareStatement("Select correo from tbUsuarios where usuario = ?")!!
                traerCorreo.setString(1, txtUsuarioRecu.text.toString())
                val rs = traerCorreo.executeQuery()

                while (rs.next()){
                    //correo del usuario ingresado
                    correo = rs.getString("correo")
                }
            }catch (ex: SQLException){
                Toast.makeText(this, "error ${ex.toString()}", Toast.LENGTH_SHORT).show()
            }

            //se manda el correo con numero aleatorio
            val mandarCorreo = MandarCorreo(correo, "Codigo de recuperacion", codigoAleatorio!!)
            mandarCorreo.execute()

            try {
                //ingresa el numero aletorio a la tabla
                val addCodigo: PreparedStatement = conx.dbConn()?.prepareStatement("update tbUsuarios set codigoVerif = ? where usuario = ?")!!
                addCodigo.setString(1, codigoAleatorio)
                addCodigo.setString(2, txtUsuarioRecu.text.toString())
                addCodigo.executeUpdate()
            }catch (ex: SQLException){
                Toast.makeText(this, "Error ${ex.toString()}", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, RecuContra_dos::class.java)
            intent.putExtra("usuarioIngresado", txtUsuarioRecu.text.toString())
            startActivity(intent)


        }

    }
}