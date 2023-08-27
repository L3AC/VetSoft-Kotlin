package com.example.vetsoft.Controlador.ui.Tienda

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetsoft.Controlador.ui.Home.btnVolverCP
import com.example.vetsoft.Controlador.ui.Home.rcMainCP
import com.example.vetsoft.Controlador.ui.Home.spBusqCP
import com.example.vetsoft.Controlador.ui.Home.spTimeCP
import com.example.vetsoft.Controlador.ui.Home.txtNombCP
import com.example.vetsoft.Controlador.ui.Perfil.spSexoDP
import com.example.vetsoft.Controlador.ui.Perfil.txtApellDP
import com.example.vetsoft.Controlador.ui.Perfil.txtCorreoDP
import com.example.vetsoft.Controlador.ui.Perfil.txtDirDP
import com.example.vetsoft.Controlador.ui.Perfil.txtDuiDP
import com.example.vetsoft.Controlador.ui.Perfil.txtNaciDP
import com.example.vetsoft.Controlador.ui.Perfil.txtNombDP
import com.example.vetsoft.Controlador.ui.Perfil.txtTelDP
import com.example.vetsoft.Controlador.ui.Perfil.txtUsDP
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var txvCant: TextView
lateinit var txvProducP: TextView
lateinit var txvPre: TextView
lateinit var btnVolverE: ImageButton
lateinit var btnImg: ImageButton
lateinit var txvDisp: TextView
lateinit var txvMarcaE: TextView
lateinit var btnL: ImageButton
lateinit var btnM: ImageButton
lateinit var btnConfirmE:Button
class ejemMain : Fragment() {

    private var cont:Int=1
    private var idProd: Int = 0
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var conx = conx()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs = arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
            idProd = arguments?.getInt("idProd")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ejem_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverE = requireView().findViewById(R.id.btnVolverE)
        txvCant = requireView().findViewById(R.id.txvCant)
        txvProducP = requireView().findViewById(R.id.txvProducP)
        txvDisp = requireView().findViewById(R.id.txvDisp)
        txvPre = requireView().findViewById(R.id.txvPre)
        txvMarcaE = requireView().findViewById(R.id.txvMarcaE)
        btnL = requireView().findViewById(R.id.btnL)
        btnM = requireView().findViewById(R.id.btnM)
        btnConfirmE = requireView().findViewById(R.id.btnConfirmE)

        cargarData()
        dispE()
    }
    fun cargarData() {
        try {
            var st: ResultSet
            val cadena =
                "select * from tbProductos where idProducto=?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idProd)
            st = ps.executeQuery()
            st.next()
            txvPre.setText(st.getString("precio"))
            txvProducP.setText(st.getString("nombre"))
            txvMarcaE.setText(st.getString("proveedor"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(st.getBytes("img"), 0, st.getBytes("img").size)
            btnImg.setImageBitmap(bitmap)

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "Error al cargar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }
    fun dispE() {
        try {
            var st: ResultSet
            val cadena =
                "select * from tbEjemplares where estado='Disponible' and  idProducto=?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idProd)
            st = ps.executeQuery()
            st.next()
            Log.i("filas",st.row.toString())
            if(st.row>0){
                txvCant.setText(st.row.toString())
            }
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "Error al cargar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }



}