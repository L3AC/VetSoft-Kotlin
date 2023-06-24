package com.example.vetsoft.Recuperacion

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.vetsoft.AMain.MainActivity
import com.example.vetsoft.AMain.MainRecup
import com.example.vetsoft.AMain.btnConfirm2
import com.example.vetsoft.AMain.btnMirar2
import com.example.vetsoft.AMain.spinSexo2
import com.example.vetsoft.AMain.txtApellidos2
import com.example.vetsoft.AMain.txtContraD2
import com.example.vetsoft.AMain.txtContraN2
import com.example.vetsoft.AMain.txtDui2
import com.example.vetsoft.AMain.txtNomb2
import com.example.vetsoft.AMain.txvCont2
import com.example.vetsoft.Conex.conx
import com.example.vetsoft.Cryptation.Crypto
import com.example.vetsoft.R
import com.example.vetsoft.Validation.Validat
import java.sql.PreparedStatement
import java.sql.SQLException

lateinit var btnVolverCC:ImageButton
lateinit var txtContraCC:EditText
lateinit var txtContra1CC:EditText
lateinit var txtContra2CC:EditText
lateinit var btnVerifCC:Button
lateinit var btnConfirmCC:Button
lateinit var txvAdvCC:TextView
lateinit var btnMirarCC:ImageButton
class CambioContra : AppCompatActivity() {
    private var idUs: Int = 0
    private var pasw=""
    private var conx = conx()
    private var crypt= Crypto()
    private var vali = Validat()
    private var contraVisible = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        idUs= extras?.getInt("idUs")!!
        pasw = extras?.getString("pasw")!!
        idUs= extras?.getInt("met")!!

        setContentView(R.layout.activity_cambio_contra)
        btnVolverCC=findViewById(R.id.btnVolverCC)
        btnVerifCC=findViewById(R.id.btnVerifCC)
        btnConfirmCC=findViewById(R.id.btnConfirmCC)
        txtContraCC=findViewById(R.id.txtContraCC)
        txtContra1CC=findViewById(R.id.txtContra1CC)
        txtContra2CC=findViewById(R.id.txtContra2CC)
        txvAdvCC=findViewById(R.id.txvAdvCC)
        btnMirarCC=findViewById(R.id.btnMirarCC)

        txvAdvCC.isVisible=false
        Habilit(false)
        btnVolverCC.setOnClickListener(){
            val scndAct = Intent(this, RecupPreguntas::class.java)
            startActivity(scndAct)
        }
        btnVerifCC.setOnClickListener(){
            if (txtContraCC.text.toString()==pasw){
                Habilit(true)
            }
            else{
                Habilit(false)
                Toast.makeText(applicationContext, "Contraseña incorrecta",Toast.LENGTH_SHORT).show()
            }
        }
        btnConfirmCC.setOnClickListener(){
            updateC()
        }
        txtContra1CC.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifContra()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        txtContra2CC.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                verifContra()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        btnMirarCC.setOnClickListener{
            contraVisible = !contraVisible
            if (contraVisible) {
                txtContra1CC.inputType = InputType.TYPE_CLASS_TEXT
                txtContra2CC.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                txtContra1CC.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                txtContra2CC.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            txtContra1CC.setSelection(txtContra1CC.text.length)
            txtContra2CC.setSelection(txtContra2CC.text.length)
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateC() {
        try {
            val cadena: String = "EXEC updtContra ?,?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, idUs)
            ps.setString(2, crypt.encrypt(txtContra1CC.text.toString(),"key"))
            ps.executeUpdate()
            Toast.makeText(applicationContext, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
            val scndAct = Intent(this, MainActivity::class.java)
            startActivity(scndAct)
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()

    }
    fun verifContra() {
        if (txtContra1CC.text.toString() != txtContra2CC.text.toString()) {
            txvAdvCC.isVisible=true
            btnConfirmCC.isEnabled=false
        }
        else {
            txvAdvCC.isVisible=false
            btnConfirmCC.isEnabled=true
        }
    }
    fun Habilit(tf: Boolean) {
        txtContra1CC.isVisible = tf
        txtContra2CC.isVisible= tf
        btnConfirmCC.isVisible= tf
        btnMirarCC.isVisible=tf
    }
    override fun onBackPressed() {
        // Deja vacío este método
    }
}