package com.example.vetsoft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.vetsoft.Conex.conx
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class CrearCuenta : AppCompatActivity() {
    private var conx = conx()
    private var idUs: Int = 0
    private var idCl: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
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