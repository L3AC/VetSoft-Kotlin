package com.example.vetsoft.Controlador.Recuperacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.ui.Home.houseCliente
import java.sql.PreparedStatement
import java.sql.SQLException

lateinit var txtCodigo: EditText
lateinit var btnVerificar: Button


class RecuContra_dos : AppCompatActivity() {

    private var conx = conx()
    private lateinit var codigoDB: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recu_contra_dos)

        txtCodigo = findViewById(R.id.txtCodigoRecu)
        btnVerificar = findViewById(R.id.btnCodigoRecu)

        btnVerificar.setOnClickListener {
            val usuarioIngresado = intent.extras?.getString("usuarioIngresado").orEmpty()


            try{
                val traerCorreo: PreparedStatement = conx.dbConn()?.prepareStatement("select codigoVerif from tbUsuarios where usuario = ?")!!
                traerCorreo.setString(1, usuarioIngresado)
                val rs = traerCorreo.executeQuery()

                while (rs.next()){
                    codigoDB = rs.getString("codigoVerif")
                }
            }catch (ex: SQLException){
                Toast.makeText(this, "error${ex.toString()}", Toast.LENGTH_SHORT).show()
            }

            if (txtCodigo.text.toString() == codigoDB){
                val intent = Intent(this, houseCliente::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "El codigo ingresado no coincide", Toast.LENGTH_SHORT).show()
            }
        }

    }
}