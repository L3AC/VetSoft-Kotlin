package com.example.vetsoft.Controlador.Recuperacion

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.vetsoft.Controlador.Cryptation.Crypto
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.ui.Home.houseCliente
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var txtCodigo: EditText
lateinit var btnVerificar: Button


class RecuContra_dos : AppCompatActivity() {

    private var conx = conx()
    private var crypt= Crypto()
    private lateinit var codigoDB: String
    private var idUs: Int = 0
    private var pasw:String=""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recu_contra_dos)

        val extras = intent.extras
        idUs= extras?.getInt("idUs")!!
        txtCodigo = findViewById(R.id.txtCodigoRecu)
        btnVerificar = findViewById(R.id.btnCodigoRecu)

        btnVerificar.setOnClickListener {
            //val usuarioIngresado = intent.extras?.getString("usuarioIngresado").orEmpty()


            try{
                val traerCorreo: PreparedStatement = conx.dbConn()?.prepareStatement("select codigoVerif from tbUsuarios where idUsuario = ?")!!
                traerCorreo.setInt(1, idUs)
                val rs = traerCorreo.executeQuery()

                while (rs.next()){
                    codigoDB = rs.getString("codigoVerif")
                }
            }catch (ex: SQLException){
                Toast.makeText(this, "error${ex.toString()}", Toast.LENGTH_SHORT).show()
            }

            if (txtCodigo.text.toString() == codigoDB){
                contra()
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

            }else{
                Toast.makeText(this, "El codigo ingresado no coincide", Toast.LENGTH_SHORT).show()
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun contra() {
        try {
            val cadena: String = "SELECT *FROM tbUsuarios" +
                    "    WHERE idUsuario = ? ;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, idUs)

            st = ps.executeQuery()
            st.next()

            val found = st.row

            if (found == 1) {
                idUs = st.getInt("idUsuario")
                pasw=crypt.decrypt(st.getString("contraseña"),"key")

                Log.i("contra",pasw)
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