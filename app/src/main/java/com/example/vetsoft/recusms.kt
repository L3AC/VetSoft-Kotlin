package com.example.vetsoft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.vetsoft.Controlador.Main.MainActivity
import com.twilio.*
import com.twilio.rest.api.v2010.account.Message
import com.twilio.rest.lookups.v1.PhoneNumber

class recusms : AppCompatActivity() {
    lateinit var btnSms:Button
    lateinit var btnBack:ImageButton
    private val ACCOUNT_SID = "ACfb0b56fe70356e0a7d5445a49cbb233b"
    private val AUTH_TOKEN = "120668d9764bde64c286bdae580930c9"

    // El número de teléfono de Twilio que se te proporcionó
    private val TWILIO_PHONE_NUMBER = "+16067140725"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recusms)

        btnSms=findViewById(R.id.btnSMS)
        btnBack=findViewById(R.id.btnBacksms)

        btnBack.setOnClickListener(){
            val scndAct = Intent(this, MainActivity::class.java)
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


}