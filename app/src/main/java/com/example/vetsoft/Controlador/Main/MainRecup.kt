package com.example.vetsoft.Controlador.Main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.Recuperacion.RecupContra
import com.example.vetsoft.Controlador.Recuperacion.RecupPreguntas
import com.example.vetsoft.Controlador.Recuperacion.RecupWha

lateinit var btnVolverR:ImageButton
lateinit var btnMailR:ImageButton
lateinit var btnPregR:ImageButton
lateinit var btnWhaR:ImageButton

@Suppress("UNREACHABLE_CODE")
class MainRecup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_recup)
        btnVolverR =findViewById(R.id.btnVolverR)
        btnMailR =findViewById(R.id.btnMailR)
        btnPregR =findViewById(R.id.btnPregR)
        btnWhaR =findViewById(R.id.btnWhaR)

        btnVolverR.setOnClickListener(){
            val scndAct1 = Intent(this, MainActivity::class.java)
            startActivity(scndAct1)
        }
        btnMailR.setOnClickListener(){
            val scndAct2 = Intent(this, RecupContra::class.java)
            startActivity(scndAct2)
        }
        btnPregR.setOnClickListener(){
            val scndAct3 = Intent(this, RecupPreguntas::class.java)
            startActivity(scndAct3)
        }
        btnWhaR.setOnClickListener(){
            val scndAct4 = Intent(this, RecupWha::class.java)
            startActivity(scndAct4)
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }
}