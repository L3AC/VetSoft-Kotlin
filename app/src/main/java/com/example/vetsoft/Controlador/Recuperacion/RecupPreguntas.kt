package com.example.vetsoft.Controlador.Recuperacion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.vetsoft.Controlador.Main.MainRecup
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.Controlador.Cryptation.Crypto
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.validation.Validat
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
lateinit var txvAdvPS:TextView
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
        btnVolverPS =findViewById(R.id.btnVolverRP)
        btnVerifPS =findViewById(R.id.btnVerifRP)
        btnConfirmPS =findViewById(R.id.btnConfirmRP)
        txtUsuarioPS =findViewById(R.id.txtUsuarioRP)
        txvPreg1 =findViewById(R.id.txvPreg1)
        txvPreg2 =findViewById(R.id.txvPreg2)
        txvPreg3 =findViewById(R.id.txvPreg3)
        txtResp1 =findViewById(R.id.txtResp1RP)
        txtResp2 =findViewById(R.id.txtResp2RP)
        txtResp3 =findViewById(R.id.txtResp3RP)
        txvAdvPS =findViewById(R.id.txvAdvRP)

        txvAdvPS.isVisible=false

        Habilit(false)
        btnVolverPS.setOnClickListener(){
            val scndAct = Intent(this, MainRecup::class.java)
            startActivity(scndAct)
        }
        btnVerifPS.setOnClickListener(){
            VerifUs()
        }
        btnConfirmPS.setOnClickListener(){
            if(verifResp(txtResp1,1) && verifResp(txtResp2,2)&&verifResp(txtResp3,3)){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Recuperación")
                builder.setMessage("Su contraseña actual es: $pasw ." +
                        " ¿Desea cambiarla?")
                builder.setPositiveButton("Si") { dialog, which ->
                    val scndAct = Intent(this, CambioContra::class.java)
                    scndAct.putExtra("idUs", idUs)
                    scndAct.putExtra("pasw", pasw)
                    scndAct.putExtra("met", 2)
                    startActivity(scndAct)
                }
                builder.setNegativeButton("No", null)
                val dialog = builder.create()
                dialog.show()

            }
            else{

            }
        }
        txtUsuarioPS.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Habilit(false)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    fun verifExist() {
        try {
            var st: ResultSet
            val cadena ="select * from tbPreguntasUsuarios where idUsuario=?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idUs)
            st = ps.executeQuery()
            var found=0
            while (st.next()) {
                found++
            }
            //val found = st.row
            Log.i("found",found.toString())
            if (found == 3) {
                txvAdvPS.isVisible=false
                Habilit(true)
                cargarPreg(txvPreg1,1);cargarPreg(txvPreg2,2);cargarPreg(txvPreg3,3);
            } else {
                Toast.makeText(applicationContext, "No se encontraron respuestas",
                    Toast.LENGTH_SHORT).show()
                Habilit(false)
                txvAdvPS.isVisible=true
            }
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Error al cargar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun VerifUs() {
        try {
            val cadena: String = "SELECT *FROM tbUsuarios" +
                    "    WHERE usuario = ? COLLATE SQL_Latin1_General_CP1_CS_AS;"
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
                verifExist()
            } else {
                Toast.makeText(applicationContext, "Usuario incorrecto", Toast.LENGTH_SHORT).show()
                Habilit(false)
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
            val cadena ="select * from tbPreguntas where idPregunta=?;"
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
            val cadena ="select pu.respuesta,pu.idUsuario,pu.idPregunta from tbPreguntasUsuarios pu,tbPreguntas p, tbUsuarios u where pu.idUsuario=u.idUsuario and\n" +
                    "pu.idPregunta=p.idPregunta and pu.idUsuario=? and pu.idPregunta=? and respuesta=? ;"
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
    override fun onBackPressed() {
        // Deja vacío este método
    }
}