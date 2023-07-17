package com.example.vetsoft.Controlador.Recuperacion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.vetsoft.Controlador.Cryptation.Crypto
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.random.Random

lateinit var btnEnviarRecu: Button
lateinit var txtUsuarioRecu: EditText

class RecupContra : AppCompatActivity() {

    private var conx = conx()
    private var vali = Validat()
    private var idUs: Int = 0
    private var pasw: String = ""
    private var correo: String = ""
    private var crypt = Crypto()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recup_contra)

        btnEnviarRecu = findViewById(R.id.btnEnviarRecu)
        txtUsuarioRecu = findViewById(R.id.txtUsuarioPs)

        btnEnviarRecu.setOnClickListener {

            if (VerifUs()) {
                //codigo aleatorio
                val codigoAleatorio = vali.GenerC(8)
                //se manda el correo con numero aleatorio
                var mandarCorreo = MandarCorreo(correo, "Codigo de recuperacion", codigoAleatorio!!)
                mandarCorreo.execute()

                try {
                    //ingresa el numero aletorio a la tabla
                    val addCodigo: PreparedStatement = conx.dbConn()
                        ?.prepareStatement("update tbUsuarios set codigoVerif = ? where usuario = ?")!!
                    addCodigo.setString(1, codigoAleatorio)
                    addCodigo.setString(2, txtUsuarioRecu.text.toString())
                    addCodigo.executeUpdate()
                } catch (ex: SQLException) {
                    Toast.makeText(this, "Error ${ex.toString()}", Toast.LENGTH_SHORT).show()
                }

                val intent = Intent(this, RecuContra_dos::class.java)
                intent.putExtra("usuarioIngresado", txtUsuarioRecu.text.toString())
                intent.putExtra("pasw", pasw)
                intent.putExtra("idUs", idUs)
                startActivity(intent)
            }


        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun VerifUs(): Boolean {
        try {
            val cadena: String = "SELECT *FROM tbUsuarios" +
                    "    WHERE usuario = ? COLLATE SQL_Latin1_General_CP1_CS_AS;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, txtUsuarioRecu.text.toString())

            st = ps.executeQuery()
            st.next()

            val found = st.row

            if (found == 1) {
                idUs = st.getInt("idUsuario")
                pasw = crypt.decrypt(st.getString("contraseña"), "key")
                correo = st.getString("correo")
                Log.i("contra", pasw)
                return true
            } else {
                Toast.makeText(applicationContext, "Usuario incorrecto", Toast.LENGTH_SHORT).show()
                Habilit(false)
                return false
            }
        } catch (ex: SQLException) {
            Log.e("Error L010 ", ex.message!!)
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
        return false
    }

    fun Habilit(tf: Boolean) {
        txvPreg1.isVisible = tf
        txvPreg2.isVisible = tf
        txvPreg3.isVisible = tf
        txtResp1.isVisible = tf
        txtResp2.isVisible = tf
        txtResp3.isVisible = tf
        btnConfirmPS.isVisible = tf
    }

    override fun onBackPressed() {
        // Deja vacío este método
    }
}