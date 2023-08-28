package com.example.vetsoft.Controlador.ui.Tienda

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Controlador.ui.Home.spinEnt5
import com.example.vetsoft.Controlador.ui.Home.txtNota5
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var txvCant: TextView
lateinit var txvProducP: TextView
lateinit var txvPre: TextView
lateinit var btnVolverE: ImageButton
lateinit var btnImg: ImageView
lateinit var txvDisp: TextView
lateinit var txvMarcaE: TextView
lateinit var btnL: ImageButton
lateinit var btnM: ImageButton
lateinit var btnConfirmE: Button

class ejemMain : Fragment() {

    private var cont: Int = 1
    private var lista: MutableList<Int> = mutableListOf()
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
        btnImg = requireView().findViewById(R.id.btnImg)
        btnConfirmE = requireView().findViewById(R.id.btnConfirmE)

        cargarData()
        dispE()

        btnL.setOnClickListener() {
            val disp = Integer.parseInt(txvDisp.text.toString())
            val cant = Integer.parseInt(txvCant.text.toString())
            if (cant > 1) {
                cont -= 1
                txvCant.text = cont.toString()
                Log.i("contador", cont.toString())
            }

        }
        btnM.setOnClickListener() {
            val disp = Integer.parseInt(txvDisp.text.toString())
            val cant = Integer.parseInt(txvCant.text.toString())
            if (cant < 10) {
                if (cont < disp) {
                    cont += 1
                    txvCant.text = cont.toString()
                    Log.i("contador", cont.toString())
                }
            }
        }
        btnConfirmE.setOnClickListener() {

            for (i in 1..cont) {

            }
        }
    }

        fun cargarData() {
            try {
                var st: ResultSet
                val cadena =
                    "select Nombre,ROUND(Precio, 2) as Precio,Proveedor,img from tbProductos where idProducto=?;"
                val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
                ps.setInt(1, idProd)
                st = ps.executeQuery()
                st.next()
                txvPre.setText("$" + st.getString("precio"))
                txvProducP.setText(st.getString("nombre"))
                txvMarcaE.setText(st.getString("proveedor"))
                val bitmap: Bitmap =
                    BitmapFactory.decodeByteArray(st.getBytes("img"), 0, st.getBytes("img").size)
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
                var rowCount = 0
                while (st.next()) {
                    rowCount++
                }
                Log.i("filas", rowCount.toString())
                if (rowCount > 0) {
                    txvDisp.text = rowCount.toString()
                    txvCant.text = cont.toString()
                    //txvCant.setText(rowCount.toString())
                } else {
                    txvDisp.text = "Agotado"
                    txvCant.setText("--")
                    btnL.isEnabled = false
                    btnM.isEnabled = false
                }
            } catch (ex: SQLException) {
                Log.e("Error: ", ex.message!!)
                Toast.makeText(context, "Error al cargar", Toast.LENGTH_SHORT).show()
            }
            conx.dbConn()!!.close()
        }
    fun insertE() {
        try {
            val cadena: String =
                "insert into tbEjemplares values(?,'Disponible',getdate())"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, idProd)
            ps.executeUpdate()

            val bundle = Bundle().apply {
                putInt("idUs", idUs)
                putInt("idCl", idCl)
                //putInt("idAni", idAni)
            }
            Toast.makeText(context, "Cita agendada correctamente", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_agendarCIta_to_infoMascota, bundle)
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(
                context,
                "No se pudo agendar",
                Toast.LENGTH_SHORT
            ).show()
        }
        conx.dbConn()!!.close()
    }

    }