package com.example.vetsoft

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.vetsoft.Conex.conx
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
lateinit var txtUsuario2:EditText
lateinit var txtContraN2:EditText
lateinit var txtContraD2:EditText
lateinit var txtCorreo2:EditText
lateinit var txtNomb2:EditText
lateinit var txtApellidos2:EditText
lateinit var txtTel2:EditText
lateinit var txtDui2:EditText
lateinit var spinSexo2:Spinner
lateinit var btnNaci2:ImageButton
lateinit var btnVolver2:ImageButton
lateinit var txvCont2:TextView
lateinit var txvUs2:TextView
lateinit var btnConfirm2:Button

class CrearCuenta : AppCompatActivity() {
    private var conx = conx()
    private var idUs: Int = 0
    private var idCl: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        txtUsuario2=findViewById(R.id.txtUsuario2)
        txtContraN2=findViewById(R.id.txtContraN2)
        txtContraD2=findViewById(R.id.txtContraD2)
        txtNomb2=findViewById(R.id.txtNomb2)
        txtApellidos2=findViewById(R.id.txtApellidos2)
        txtTel2=findViewById(R.id.txtTel2)
        txtDui2=findViewById(R.id.txtDui2)
        spinSexo2=findViewById(R.id.spinSexo2)
        btnNaci2=findViewById(R.id.btnNaci2)
        btnVolver2=findViewById(R.id.btnVolver2)
        txvCont2=findViewById(R.id.txvCont2)
        txvUs2=findViewById(R.id.txvUs2)
        btnConfirm2=findViewById(R.id.btnConfirm2)

        btnConfirm2.setOnClickListener(){

        }
    }
//CAMBIAR
    fun createUs() {

        try {
            val cadena: String = "EXEC insertUs ?,?,?,?,?,?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            /*ps.setString(1, usu.text.toString())
            ps.setString(2, contra2.text.toString())
            ps.setString(3, correo.text.toString())*/

            ps.executeUpdate()

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Errorsito", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()

    }
    fun selectUs(){
        try {
            val cadena: String = "EXEC selectUsB ?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

           // ps.setString(1, usu.text.toString())
            st = ps.executeQuery()
            st.next()

            val found = st.row
            if (found == 1) {
                idUs = st.getInt("idUsuario")

            } else {
                Toast.makeText(applicationContext, "Error de inserción", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Error interno", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }

    fun createCl() {

        try {
            val cadena: String =
                "insert into tbClientes(idUsuario,nombres,apellidos,tipodocum,numdocum," +
                        "nacimiento,sexo,telefono,tipoSangre,patologias) " +
                        "values (?,?,?,?,?,?,?,?,?,?);"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, idUs.toString())
           /* ps.setString(2, nomb.text.toString())
            ps.setString(3, apell.text.toString())
            ps.setString(4, tpdoc.selectedItem.toString())
            ps.setString(5, ndoc.text.toString())
            ps.setString(6, fechaSql)
            ps.setString(7, tpsexo.selectedItem.toString())
            ps.setString(8, tel.text.toString())
            ps.setString(9, tpsangre.text.toString())
            if(patol.text.toString()==""){
                ps.setString(10, "Ninguna")
            }
            else{
                ps.setString(10, patol.text.toString())
            }*/
            ps.executeUpdate()

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Errorsito", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()

    }

    fun verifUs() {
        try {
            val cadena: String = "select * from tbUsuarios where usuario=?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            //ps.setString(1, usu.text.toString())
            st = ps.executeQuery()
            st.next()

            val found = st.row
            /*if (found == 1) {
                textAdv.isVisible = true
                Toast.makeText(applicationContext, "Ya existe usuario", Toast.LENGTH_SHORT).show()

            } else {
                textAdv.isVisible = false
                bingresar.isEnabled = false
            }*/
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(applicationContext, "Error interno", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
}