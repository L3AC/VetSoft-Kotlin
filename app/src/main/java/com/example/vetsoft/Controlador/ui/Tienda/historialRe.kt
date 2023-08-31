package com.example.vetsoft.Controlador.ui.Tienda

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class filaHis(
    val idR: Int,
    val idE: Int,
    val nProd: String,
    val prov: String,
    val precio: Float,
    val img: ByteArray
)

val regHis = mutableListOf<filaHis>()
val myDataHis = mutableListOf<String>()
class historialRe : Fragment() {
    lateinit var btnVolverCat2: ImageButton
    lateinit var txtNProd2: EditText
    lateinit var rcHistProd: RecyclerView
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var idTipoP: Int = 0
    private var conx = conx()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs = arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial_re, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverCat2 = requireView().findViewById(R.id.btnVolverCat2)
        txtNProd2 = requireView().findViewById(R.id.txtNProd2)
        rcHistProd = requireView().findViewById(R.id.rcHistProd)
        rcHistProd.layoutManager = LinearLayoutManager(context)

        val miAdapter =hisCard(myDataHis)
        rcHistProd.adapter = miAdapter

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnVolverCat2.setOnClickListener() {
            findNavController().navigate(R.id.action_historialRe_to_mainReserva, bundle)
        }
        txtNProd2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CargarByN()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    fun CargarByN() {
        myDataRe.clear()
        regRe.clear()
        try {
            var st: ResultSet
            var cadena: String =
                "select rp.idEjemplar,idReservaProducto,Nombre,Proveedor,Precio,img from tbReservaProductos rp,tbProductos p,tbEjemplares e\n" +
                        "where rp.idEjemplar=e.idEjemplar and p.idProducto=e.idProducto and e.Estado='Inactivo' and  rp.idCliente=? and Nombre like ?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idCl)
            ps.setString(2, '%' + txtNProd2.text.toString() + '%')
            st = ps.executeQuery()

            var rowCount = 0


            while (st?.next() == true) {
                val col1 = st.getInt("idReservaProducto")
                val col2 = st.getInt("idEjemplar")
                val col3 = st.getString("Nombre")
                val col4 = st.getString("Proveedor")
                val col5 = st.getFloat("Precio")
                val col6 = st.getBytes("img")
                rowCount++

                regHis.add(filaHis(col1, col2, col3, col4, col5, col6))
                val newElement = "$col3"
                myDataHis.add(newElement)

                val miAdapter = hisCard(myDataHis)
                rcHistProd.adapter = miAdapter
                miAdapter.notifyDataSetChanged()

                Log.i("tago",rowCount.toString())
                if (rowCount == 0) {
                    Log.i("tago","entro")
                    myDataHis.clear()
                    regHis.clear()
                    val miAdapter = hisCard(myDataHis)
                    rcHistProd.adapter = miAdapter
                    miAdapter.notifyDataSetChanged()
                }
            }


        } catch (ex: SQLException) {
            Log.i("ol", ex.message.toString())
            Toast.makeText(context, "Error al cargar cita N", Toast.LENGTH_SHORT).show()
        }
    }
    class hisCard(
        private val Datos: MutableList<String>/*,private val btnClick:(Int)->Unit*/
    ) :
        RecyclerView.Adapter<hisCard.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txvNomb: TextView = view.findViewById(R.id.txvNombH)
            val txvMarca: TextView = view.findViewById(R.id.txvMarcaH)
            val txvPrecio: TextView = view.findViewById(R.id.txvPrecioH)
            val imgP: ImageView = view.findViewById(R.id.imgPH)
        }

        @SuppressLint("MissingInflatedId")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val vista =
                LayoutInflater.from(parent.context).inflate(R.layout.hiscard, parent, false)
            return MyViewHolder(vista)
        }

        override fun getItemCount() = Datos.size
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val itm = regHis[position]
            holder.txvNomb.text = itm.nProd
            holder.txvMarca.text = itm.prov
            holder.txvPrecio.text = "$ " + itm.precio.toString()
            if (itm.img != null) {
                val bitmap: Bitmap = BitmapFactory.decodeByteArray(itm.img, 0, itm.img.size)
                holder.imgP.setImageBitmap(bitmap)
            }
            //Reemplazamos la imagen
            //  holder.imageView.setImageResource(Imagenes[position])
        }
    }
}