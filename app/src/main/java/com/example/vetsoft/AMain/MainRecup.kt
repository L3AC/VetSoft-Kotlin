package com.example.vetsoft.AMain

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.example.vetsoft.R
import com.example.vetsoft.Recuperacion.RecupContra
import com.example.vetsoft.Recuperacion.RecupPreguntas
import com.example.vetsoft.Recuperacion.RecupWha

lateinit var btnVolverR:ImageButton
lateinit var imgMailR:ImageView
lateinit var imgPregR:ImageView
lateinit var imgWhaR:ImageView

@Suppress("UNREACHABLE_CODE")
class MainRecup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_recup)
        btnVolverR=findViewById(R.id.btnVolverR)
        imgMailR=findViewById(R.id.imgMailR)
        imgPregR=findViewById(R.id.imgPregR)
        imgWhaR=findViewById(R.id.imgWhaR)

        btnVolverR.setOnClickListener(){
            val scndAct1 = Intent(this, MainActivity::class.java)
            startActivity(scndAct1)
        }
        imgMailR.setOnClickListener(){
            val scndAct2 = Intent(this, RecupContra::class.java)
            startActivity(scndAct2)
        }
        imgPregR.setOnClickListener(){
            val scndAct3 = Intent(this, RecupPreguntas::class.java)
            startActivity(scndAct3)
        }
        imgWhaR.setOnClickListener(){
            val scndAct4 = Intent(this, RecupWha::class.java)
            startActivity(scndAct4)
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }
}