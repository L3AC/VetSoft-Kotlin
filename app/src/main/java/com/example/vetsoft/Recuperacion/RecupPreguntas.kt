package com.example.vetsoft.Recuperacion

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.vetsoft.AMain.txtContra1
import com.example.vetsoft.AMain.txtUsuario1
import com.example.vetsoft.Conex.conx
import com.example.vetsoft.R
import com.example.vetsoft.Validation.Validat
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
lateinit var btnVolverPS:Button
lateinit var btnVerifPS:Button
lateinit var btnConfirmPS:Button
lateinit var txtUsuarioPS:EditText
lateinit var txvPreg1:TextView
lateinit var txtResp1:EditText
lateinit var txvPreg2:TextView
lateinit var txtResp2:EditText
lateinit var txvPreg3:TextView
lateinit var txtResp3:EditText
class RecupPreguntas : AppCompatActivity() {
    private var idUs: Int = 0
    private var idCl:Int=0
    private var idAni:Int=0
    private var conx = conx()
    private var vali = Validat()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recup_preguntas)
        btnVolverPS=findViewById(R.id.btnVolverPS)
        txvPreg1=findViewById(R.id.txvPreg1)
        txvPreg2=findViewById(R.id.txvPreg2)
        txvPreg3=findViewById(R.id.txvPreg3)
        txtResp1=findViewById(R.id.txtResp1)
        txtResp2=findViewById(R.id.txtResp2)
        txtResp3=findViewById(R.id.txtResp3)
btnConfirmPS;

    }
    fun VerifUs() {
        try {
            val cadena: String = "EXEC selectUsB ?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, txtUsuario1.text.toString())
            //ps.setString(2, crypt.encrypt(txtContra1.text.toString(),"key"))//ENCRIPTADO

            st = ps.executeQuery()
            st.next()

            val found = st.row

            if (found == 1) {
                idUs = st.getInt("idUsuario")
            } else {
                Toast.makeText(applicationContext, "Datos incorrectos", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: SQLException) {
            Log.e("Error L010 ", ex.message!!)
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
}