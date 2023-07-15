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
    private var usuarioIngresado:String=""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recu_contra_dos)

        txtCodigo = findViewById(R.id.txtCodigoRecu)
        btnVerificar = findViewById(R.id.btnCodigoRecu)
        val extras = intent.extras
        usuarioIngresado = extras?.getString("usuarioIngresado")!!
        pasw= extras?.getString("pasw")!!

        btnVerificar.setOnClickListener {
            try{
                val traerCorreo: PreparedStatement = conx.dbConn()?.prepareStatement("select codigoVerif from tbUsuarios where usuario = ?")!!
                traerCorreo.setString(1, usuarioIngresado)
                val rs = traerCorreo.executeQuery()

                while (rs.next()){
                    codigoDB = rs.getString("codigoVerif")
                }
            }catch (ex: SQLException){
                Toast.makeText(this, "error${ex.toString()}", Toast.LENGTH_SHORT).show()
            }

            if (txtCodigo.text.toString() == codigoDB){
                val builder = AlertDialog.Builder(this)
                Log.i("k",pasw)
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
    override fun onBackPressed() {
        // Deja vacío este método
    }
}