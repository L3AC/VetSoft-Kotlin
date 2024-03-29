package com.example.vetsoft.Controlador.Main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.Recuperacion.RecupContra
import com.example.vetsoft.Controlador.Recuperacion.RecupPreguntas

lateinit var btnVolverR:ImageButton
lateinit var btnMailR:ImageButton
lateinit var btnPregR:ImageButton
lateinit var btnSmsR:ImageButton
lateinit var btnMail2: TextView
lateinit var btnMail3: TextView
lateinit var btnPreg2: TextView
lateinit var btnPreg3: TextView
lateinit var btnPin2: TextView
lateinit var btnPin3: TextView

@Suppress("UNREACHABLE_CODE")
class MainRecup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_recup)
        btnVolverR =findViewById(R.id.btnVolverR)
        btnMailR =findViewById(R.id.btnMailR)
        btnPregR =findViewById(R.id.btnPregR)
        btnSmsR =findViewById(R.id.btnSmsR)
        btnMail2 =findViewById(R.id.textView37)
        btnMail3 =findViewById(R.id.txvCitasP10)
        btnPreg3 =findViewById(R.id.txvCitasP13)
        btnPin3 =findViewById(R.id.txvCitasP14)
        btnPreg2 =findViewById(R.id.textView38)
        btnPin2 =findViewById(R.id.textView40)

        //MENU
        btnVolverR.setOnClickListener(){
            val scndAct1 = Intent(this, MainActivity::class.java)
            startActivity(scndAct1)
        }
        btnPregR.setOnClickListener(){
            val scndAct1 = Intent(this, RecupPreguntas::class.java)
            scndAct1.putExtra("forma", 1)
            startActivity(scndAct1)
        }
        btnPreg2.setOnClickListener(){
            val scndAct1 = Intent(this, RecupPreguntas::class.java)
            scndAct1.putExtra("forma", 1)
            startActivity(scndAct1)
        }
        btnPreg3.setOnClickListener(){
            val scndAct1 = Intent(this, RecupPreguntas::class.java)
            scndAct1.putExtra("forma", 1)
            startActivity(scndAct1)
        }
        btnMailR.setOnClickListener(){
            val scndAct2 = Intent(this, RecupContra::class.java)
            scndAct2.putExtra("forma", 2)
            startActivity(scndAct2)
        }
        btnMail2.setOnClickListener(){
            val scndAct2 = Intent(this, RecupContra::class.java)
            scndAct2.putExtra("forma", 2)
            startActivity(scndAct2)
        }
        btnMail3.setOnClickListener(){
            val scndAct2 = Intent(this, RecupContra::class.java)
            scndAct2.putExtra("forma", 2)
            startActivity(scndAct2)
        }
        btnSmsR.setOnClickListener(){
            val scndAct3 = Intent(this, RecupContra::class.java)
            scndAct3.putExtra("forma", 3)
            startActivity(scndAct3)
        }
        btnPin3.setOnClickListener(){
            val scndAct3 = Intent(this, RecupContra::class.java)
            scndAct3.putExtra("forma", 3)
            startActivity(scndAct3)
        }
        btnPin2.setOnClickListener(){
            val scndAct3 = Intent(this, RecupContra::class.java)
            scndAct3.putExtra("forma", 3)
            startActivity(scndAct3)
        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }
}