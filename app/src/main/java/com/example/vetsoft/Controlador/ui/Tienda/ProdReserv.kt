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
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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

class filaRe(
    val idR: Int,
    val idE: Int,
    val nProd: String,
    val prov: String,
    val precio: Float,
    val img: ByteArray
)

val regRe = mutableListOf<filaRe>()
val myDataRe = mutableListOf<String>()

class prodReserv : Fragment() {
    lateinit var btnVolverR: ImageButton
    lateinit var txtNProdR: EditText
    lateinit var rcReservProdR: RecyclerView
    private var idUs: Int = 0
    private var idCl: Int = 0
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
        return inflater.inflate(R.layout.fragment_prod_reserv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverR = requireView().findViewById(R.id.btnVolverR)
        txtNProdR = requireView().findViewById(R.id.txtNProdR)
        rcReservProdR = requireView().findViewById(R.id.rcReservProdR)
        rcReservProdR.layoutManager = LinearLayoutManager(context)

        val miAdapter = reCard(this, myDataRe)
        rcReservProdR.adapter = miAdapter

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        CargarByN()

        btnVolverR.setOnClickListener() {
            findNavController().navigate(R.id.action_prodReserv_to_mainReserva, bundle)
        }
        txtNProdR.addTextChangedListener(object : TextWatcher {
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
                        "where rp.idEjemplar=e.idEjemplar and p.idProducto=e.idProducto and  rp.idCliente=? and Nombre like ?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idCl)
            ps.setString(2, '%' + txtNProdR.text.toString() + '%')
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

                    regRe.add(filaRe(col1, col2, col3, col4, col5, col6))
                    val newElement = "$col3"
                    myDataRe.add(newElement)

                    val miAdapter = reCard(this, myDataRe)
                    rcReservProdR.adapter = miAdapter
                    miAdapter.notifyDataSetChanged()

                    Log.i("tago",rowCount.toString())
                    if (rowCount == 0) {
                        Log.i("tago","entro")
                        myDataRe.clear()
                        regRe.clear()
                        val miAdapter = reCard(this, myDataRe)
                        rcReservProdR.adapter = miAdapter
                        miAdapter.notifyDataSetChanged()
                    }
                }


        } catch (ex: SQLException) {
            Log.i("ol", ex.message.toString())
            Toast.makeText(context, "Error al cargar cita N", Toast.LENGTH_SHORT).show()
        }
    }

    fun delRe(idRP: Int, idEj: Int) {
        try {
            var cadena: String =
                "delete tbReservaProductos where idReservaProducto=?;" +
                        "update tbEjemplares set Estado='Disponible' where idEjemplar=?;"
            var st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            Log.i("elim", idRP.toString())
            ps.setInt(1, idRP)
            ps.setInt(2, idEj)
            ps.executeUpdate()
            Toast.makeText(context, "Campos actualizados", Toast.LENGTH_SHORT).show()
        } catch (ex: SQLException) {
            Log.i("elim", ex.message.toString())
            Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
        }
    }

    class reCard(
        private val fragment: prodReserv,
        private val Datos: MutableList<String>/*,private val btnClick:(Int)->Unit*/
    ) :
        RecyclerView.Adapter<reCard.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txvNomb: TextView = view.findViewById(R.id.txvNomb2)
            val txvMarca: TextView = view.findViewById(R.id.txvMarca2)
            val txvPrecio: TextView = view.findViewById(R.id.txvPrecio2)
            val imgP: ImageView = view.findViewById(R.id.imgP2)
            val btnElim: ImageButton = view.findViewById(R.id.btnElim)
        }

        @SuppressLint("MissingInflatedId")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val vista =
                LayoutInflater.from(parent.context).inflate(R.layout.prodre, parent, false)
            return MyViewHolder(vista)
        }

        override fun getItemCount() = Datos.size
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val itm = regRe[position]
            holder.txvNomb.text = itm.nProd
            holder.txvMarca.text = itm.prov
            holder.txvPrecio.text = "$ " + itm.precio.toString()
            if (itm.img != null) {
                val bitmap: Bitmap = BitmapFactory.decodeByteArray(itm.img, 0, itm.img.size)
                holder.imgP.setImageBitmap(bitmap)
            }
            holder.btnElim.setOnClickListener() {
                fragment.delRe(itm.idR, itm.idE)
                fragment.CargarByN()
                regRe.removeAt(position)
                notifyItemRemoved(position)
            }
            //Reemplazamos la imagen
            //  holder.imageView.setImageResource(Imagenes[position])
        }
    }

}