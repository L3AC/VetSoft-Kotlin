package com.example.vetsoft.Controlador.Recuperacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.vetsoft.Controlador.Main.MainRecup
import com.example.vetsoft.R
import com.twilio.*
import com.twilio.rest.api.v2010.account.Message

class recusms : AppCompatActivity() {
    lateinit var btnSms:Button
    lateinit var btnUs:Button
    lateinit var txtUs:EditText
    lateinit var txtCod:EditText
    lateinit var btnBack:ImageButton

    private val ACCOUNT_SID = "ACfb0b56fe70356e0a7d5445a49cbb233b"
    private val AUTH_TOKEN = "120668d9764bde64c286bdae580930c9"

    // El número de teléfono de Twilio que se te proporcionó
    private val TWILIO_PHONE_NUMBER = "+16067140725"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recusms)

        btnSms=findViewById(R.id.btnSms)
        btnBack=findViewById(R.id.btnBacksms)
        btnUs=findViewById(R.id.btnVerifUs)
        txtCod=findViewById(R.id.txtCod)
        txtUs=findViewById(R.id.txtUs)


        btnBack.setOnClickListener(){
            val scndAct = Intent(this, MainRecup::class.java)
            startActivity(scndAct)
        }

        btnSms.setOnClickListener(){
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN)
            val message: Message = Message.creator(
                com.twilio.type.PhoneNumber("+50371928947"),
                com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                "Where's Wallace?"
            )
                .create()
            System.out.println(message.getSid())
        }
    }
    fun Verif(){

    }

}