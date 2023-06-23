package com.example.vetsoft.Recuperacion

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.vetsoft.AMain.txtContra1
import com.example.vetsoft.AMain.txtUsuario1
import com.example.vetsoft.Conex.conx
import com.example.vetsoft.Cryptation.Crypto
import com.example.vetsoft.R
import com.example.vetsoft.Validation.Validat
import com.example.vetsoft.ui.house.txvAniF5
import com.example.vetsoft.ui.house.txvEdadF5
import com.example.vetsoft.ui.house.txvNombF5
import com.example.vetsoft.ui.house.txvPesoF5
import com.example.vetsoft.ui.house.txvRazaF5
import com.example.vetsoft.ui.house.txvSexoF5
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
lateinit var btnVolverPS:ImageButton
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
    private var pasw:String=""
    private var conx = conx()
    private var crypt= Crypto()
    private var vali = Validat()
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recup_preguntas)
        btnVolverPS=findViewById(R.id.btnVolverPS)
        btnVerifPS=findViewById(R.id.btnVerifPS)
        btnConfirmPS=findViewById(R.id.btnConfirmPS)
        txtUsuarioPS=findViewById(R.id.txtUsuarioPS)
        txvPreg1=findViewById(R.id.txvPreg1)
        txvPreg2=findViewById(R.id.txvPreg2)
        txvPreg3=findViewById(R.id.txvPreg3)
        txtResp1=findViewById(R.id.txtResp1)
        txtResp2=findViewById(R.id.txtResp2)
        txtResp3=findViewById(R.id.txtResp3)


        Habilit(false)
        btnVerifPS.setOnClickListener(){
            VerifUs()
        }
        btnConfirmPS.setOnClickListener(){
            if(verifResp(txtResp1,1) && verifResp(txtResp2,2)&&verifResp(txtResp3,3)){
                Toast.makeText(applicationContext, "Su contraseña es:", Toast.LENGTH_SHORT).show()
            }
            else{

            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun VerifUs() {
        try {
            val cadena: String = "EXEC selectUsB ?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, txtUsuarioPS.text.toString())

            st = ps.executeQuery()
            st.next()

            val found = st.row

            if (found == 1) {
                idUs = st.getInt("idUsuario")
                pasw=crypt.decrypt(st.getString("contraseña"),"key")
                Log.i("contra",pasw)
                Habilit(true)
                cargarPreg(txvPreg1,1);cargarPreg(txvPreg2,2);cargarPreg(txvPreg3,3);
            } else {
                Toast.makeText(applicationContext, "Usuario incorrecto", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: SQLException) {
            Log.e("Error L010 ", ex.message!!)
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    fun cargarPreg(textV:TextView,idPreg:Int) {
        try {
            var st: ResultSet
            val cadena ="EXEC selectPreg ?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idPreg)
            st = ps.executeQuery()
            st.next()
            textV.setText(st.getString("enunciado"))

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Error al cargar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    fun verifResp(resp:EditText,idPreg:Int) :Boolean{
        try {
            var st: ResultSet
            val cadena ="EXEC selectPregUs ?,?,?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idUs)
            ps.setInt(2, idPreg)
            ps.setString(3, resp.text.toString())
            st = ps.executeQuery()
            st.next()
            val found = st.row

            if (found == 1) {
                return true
            } else {
                Toast.makeText(applicationContext, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()
                return false
            }

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Error al cargar", Toast.LENGTH_SHORT).show()
            return false
        }
        conx.dbConn()!!.close()
    }
    fun Habilit(tf: Boolean) {
        txvPreg1.isVisible = tf
        txvPreg2.isVisible= tf
        txvPreg3.isVisible = tf
        txtResp1.isVisible = tf
        txtResp2.isVisible = tf
        txtResp3.isVisible = tf
        btnConfirmPS.isVisible = tf
    }
}