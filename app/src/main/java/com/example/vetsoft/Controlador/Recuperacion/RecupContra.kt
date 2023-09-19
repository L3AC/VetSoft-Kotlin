package com.example.vetsoft.Controlador.Recuperacion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.vetsoft.Controlador.Cryptation.Crypto
import com.example.vetsoft.Controlador.Main.MainRecup
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.random.Random

lateinit var btnEnviarRecu: Button
lateinit var txtUsuarioRecu: EditText
lateinit var btnVolver: ImageButton

class RecupContra : AppCompatActivity() {

    private var conx = conx()
    private var vali = Validat()
    private var idUs: Int = 0
    private var pasw: String = ""
    private var correo: String = ""
    private var tel: String = ""
    private var codigo: String = ""
    private var crypt = Crypto()
    private var sends=sendSms()

    private var forma: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recup_contra)
        val extras = intent.extras
        forma = extras?.getInt("forma")!!
        btnEnviarRecu = findViewById(R.id.btnEnviarRecu)
        txtUsuarioRecu = findViewById(R.id.txtUsuarioPs)
        btnVolver = findViewById(R.id.btnVolverDPe)

        btnVolver.setOnClickListener {
            val scndAct = Intent(this, MainRecup::class.java)
            startActivity(scndAct)
        }

        btnEnviarRecu.setOnClickListener {

            if (VerifUs()) {
                //codigo aleatorio
                val codigoAleatorio = vali.GenerC(8)

                if (forma == 2) {//METODO DE CORREO
                    //se manda el correo con numero aleatorio
                    var mandarCorreo = MandarCorreo(
                        correo, "Codigo de recuperacion", "<!DOCTYPE html>\n" +
                                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                                "\n" +
                                "<head>\n" +
                                "    <meta charset=\"utf-8\">\n" +
                                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                                "    <title>Correo</title>\n" +
                                "\n" +
                                "    <style>\n" +
                                "        @import url('https://fonts.googleapis.com/css2?family=Karla:wght@300&family=Montserrat:wght@300&family=Poppins:wght@300&family=Roboto+Mono:ital@0;1&display=swap');\n" +
                                "\n" +
                                "        *, *:before, *:after {\n" +
                                "            -moz-box-sizing: border-box;\n" +
                                "            -webkit-box-sizing: border-box;\n" +
                                "            box-sizing: border-box;\n" +
                                "        }\n" +
                                "\n" +
                                "        body {\n" +
                                "            background-color: #1B4965;\n" +
                                "            margin-top: 55px;\n" +
                                "            margin-bottom: 55px;\n" +
                                "        }\n" +
                                "\n" +
                                "        form {\n" +
                                "            max-width: 300px;\n" +
                                "            margin: 10px auto;\n" +
                                "            padding: 10px 20px;\n" +
                                "            background: #CAE9FF;\n" +
                                "            border-radius: 5px;\n" +
                                "        }\n" +
                                "\n" +
                                "        h1 {\n" +
                                "            text-align: center;\n" +
                                "            font-family: Brush Script MT;\n" +
                                "            font-family: 'Roboto Mono', monospace;\n" +
                                "            font-family: 'Montserrat', sans-serif;\n" +
                                "            font-size: 35px;\n" +
                                "            color: black;\n" +
                                "        }\n" +
                                "\n" +
                                "        h3 {\n" +
                                "            text-align: center;\n" +
                                "            font-family: 'Karla', sans-serif;\n" +
                                "            color: black;\n" +
                                "        }\n" +
                                "\n" +
                                "        select {\n" +
                                "            padding: 6px;\n" +
                                "            height: 32px;\n" +
                                "            border-radius: 2px;\n" +
                                "        }\n" +
                                "\n" +
                                "        fieldset {\n" +
                                "            margin-bottom: 30px;\n" +
                                "            border: none;\n" +
                                "        }\n" +
                                "\n" +
                                "        legend {\n" +
                                "            font-family: Candara;\n" +
                                "            font-size: 1.5em;\n" +
                                "            margin-bottom: 10px;\n" +
                                "            font-family: 'Poppins', sans-serif;\n" +
                                "        }\n" +
                                "\n" +
                                "        label {\n" +
                                "            display: block;\n" +
                                "            margin-bottom: 8px;\n" +
                                "            font-family: Times New Roman;\n" +
                                "            font-family: 'Karla', sans-serif;\n" +
                                "        }\n" +
                                "\n" +
                                "            label.light {\n" +
                                "                font-weight: 300;\n" +
                                "                display: inline;\n" +
                                "            }\n" +
                                "\n" +
                                "        .img {\n" +
                                "            padding-top: 10px;\n" +
                                "        }\n" +
                                "\n" +
                                "        @media screen and (min-width: 480px) {\n" +
                                "\n" +
                                "            form {\n" +
                                "                max-width: 480px;\n" +
                                "            }\n" +
                                "        }\n" +
                                "\n" +
                                "        @media screen and (max-width: 480px) {\n" +
                                "\n" +
                                "            form {\n" +
                                "                max-width: 480px;\n" +
                                "            }\n" +
                                "\n" +
                                "            .img {\n" +
                                "                width: 300px;\n" +
                                "                padding-top: 10px;\n" +
                                "            }\n" +
                                "\n" +
                                "            .logo {\n" +
                                "                width: 40px;\n" +
                                "            }\n" +
                                "\n" +
                                "            @media screen and (max-width: 388px) {\n" +
                                "\n" +
                                "                form {\n" +
                                "                    max-width: 388px;\n" +
                                "                }\n" +
                                "\n" +
                                "                .img {\n" +
                                "                    width: 280px;\n" +
                                "                    padding-top: 10px;\n" +
                                "                }\n" +
                                "\n" +
                                "                .logo {\n" +
                                "                    width: 30px;\n" +
                                "                }\n" +
                                "            }\n" +
                                "\n" +
                                "            @media screen and (max-width: 374px) {\n" +
                                "\n" +
                                "                form {\n" +
                                "                    max-width: 374px;\n" +
                                "                }\n" +
                                "\n" +
                                "                .img {\n" +
                                "                    width: 250px;\n" +
                                "                    padding-top: 10px;\n" +
                                "                }\n" +
                                "\n" +
                                "                .logo {\n" +
                                "                    width: 20px;\n" +
                                "                }\n" +
                                "            }\n" +
                                "\n" +
                                "            @media screen and (max-width: 330px) {\n" +
                                "\n" +
                                "                form {\n" +
                                "                    max-width: 330px;\n" +
                                "                }\n" +
                                "\n" +
                                "                .img {\n" +
                                "                    width: 210px;\n" +
                                "                    padding-top: 10px;\n" +
                                "                }\n" +
                                "\n" +
                                "                .logo {\n" +
                                "                    width: 20px;\n" +
                                "                }\n" +
                                "            }\n" +
                                "        }\n" +
                                "    </style>\n" +
                                "</head>\n" +
                                "\n" +
                                "<body>\n" +
                                "    <div>\n" +
                                "        <form action=\"index.html\" method=\"post\">\n" +
                                "          <p style=\"text-align: center; \">\n" +
                                "            <img class=\"img\" src=\"https://cdn-icons-png.flaticon.com/512/6363/6363425.png\" width=\"100px\">\n" +
                                "        </a>\n" +
                                "    </p>\n" +
                                "            <h1> Vetsoft </h1>\n" +
                                "            <hr style: width=\"50%\" color=\"black\">\n" +
                                "            <br>\n" +
                                "            <fieldset>\n" +
                                "\n" +
                                "                <legend>Hola: " + txtUsuarioRecu.text.toString() + " <b> </b></legend>\n" +
                                "\n" +
                                "                <label>\n" +
                                "                   Antes de completar el proceso de recuperación de contraseña debes de verificar tu código de verificación dento del programa Vetsoft\n" +
                                "                </label>\n" +
                                "            </fieldset>\n" +
                                "            <fieldset>\n" +
                                "\n" +
                                "                <legend>Tu código de verificación es: " + codigoAleatorio + "<br> <b></b></legend>\n" +
                                "\n" +
                                "                <label>A continuación ingresa en el programa de Vetsoft y digita el código antes mencionado en el área de código de verificación.</label>\n" +
                                "\n" +
                                "                <p style=\"text-align: center; \">\n" +
                                "                        <img class=\"img\" src=\"https://i.gifer.com/origin/4f/4f5d1807ba2d22d9de3f1abb925cab9c_w200.gif\" width=\"300px\">\n" +
                                "                    </a>\n" +
                                "                </p>\n" +
                                "            </fieldset>\n" +
                                "        </form>\n" +
                                "    </div>\n" +
                                "</body>\n" +
                                "\n" +
                                "</html>"!!
                    )
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
                }
                if (forma == 3) {//metodo de pin
                    try {

                    } catch (e: Exception) {
                        Log.i("ERROR ", e.toString())
                    }
                }

                val intent = Intent(this, RecuContra_dos::class.java)
                intent.putExtra("usuarioIngresado", txtUsuarioRecu.text.toString())
                intent.putExtra("pasw", pasw)
                intent.putExtra("idUs", idUs)
                intent.putExtra("forma", forma)
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
                tel = st.getString("telefono")
                Log.i("contra", pasw + " " + tel)
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