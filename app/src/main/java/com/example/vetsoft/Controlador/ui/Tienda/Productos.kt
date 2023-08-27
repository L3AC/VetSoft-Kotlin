package com.example.vetsoft.Controlador.ui.Tienda

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
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
import com.example.vetsoft.Controlador.ui.Home.CitasPendientes
import com.example.vetsoft.Controlador.ui.Home.rcMainCP
import com.example.vetsoft.Controlador.ui.Home.regCP
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class classProd(
    val id: Int, val nProd: String, val prov: String, val precio: Float, val img: ByteArray
)

val regProd2 = mutableListOf<classProd>()
val myDataProd2 = mutableListOf<String>()

class Productos : Fragment() {
    lateinit var btnVolver: ImageButton
    lateinit var txtNProd: EditText
    lateinit var rcProd: RecyclerView
    private var idProd: Int = 0
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var idTipoP: Int = 0
    private var conx = conx()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs = arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
            idTipoP = arguments?.getInt("idTipoP")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_productos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolver = requireView().findViewById(R.id.btnVolverProd)
        txtNProd = requireView().findViewById(R.id.txtProdP)
        rcProd = requireView().findViewById(R.id.rcProduct)
        rcProd.layoutManager = LinearLayoutManager(context)

        val miAdapter = prodCard(myDataProd2)
        rcProd.adapter = miAdapter

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        CargarByN()

        btnVolver.setOnClickListener() {
            findNavController().navigate(R.id.action_catalogoProd_to_mainTienda, bundle)
        }
        txtNProd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CargarByN()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        rcProd.addOnItemTouchListener(
            citasPRecycler(requireContext(), rcProd,
                object : citasPRecycler.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // Acciones a realizar cuando se hace clic en un elemento del RecyclerView
                        val itm = regProd2[position]
                        idProd = itm.id
                        val bundle = Bundle().apply {
                            putInt("idUs", idUs)
                            putInt("idCl", idCl)
                            putInt("idProd", idProd)
                        }
                        Log.i("IDE: ", idProd.toString())
                        findNavController().navigate(
                            R.id.action_productos_to_ejemMain,
                            bundle
                        )
                    }
                })
        )

    }

    fun CargarByN() {
        myDataProd2.clear()
        regProd2.clear()
        try {
            var st: ResultSet
            var cadena: String = "select * from tbProductos p, tbTipoProductos tp " +
                    "where p.idTipoProducto=tp.idTipoProducto and Nombre like ? and p.idTipoProducto=?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setString(1, '%' + txtNProd.text.toString() + '%')
            ps.setInt(2, idTipoP)
            Log.i("sda", '%' + txtNProd.text.toString() + '%')
            st = ps.executeQuery()

            while (st?.next() == true) {
                val col1 = st.getInt("idProducto")
                val col2 = st.getString("Nombre")
                val col3 = st.getString("Proveedor")
                val col4 = st.getFloat("Precio")
                val col5 = st.getBytes("img")

                regProd2.add(classProd(col1, col2, col3, col4, col5))
                val newElement = "$col2"
                myDataProd2.add(newElement)

                val miAdapter = prodCard(myDataProd2)
                rcProd.adapter = miAdapter
            }

        } catch (ex: SQLException) {
            Log.i("ol", ex.message.toString())
            Toast.makeText(context, "Error al cargar cita N", Toast.LENGTH_SHORT).show()
        }
    }

    class prodCard(private val Datos: MutableList<String>/*,private val btnClick:(Int)->Unit*/) :
        RecyclerView.Adapter<prodCard.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txvNomb: TextView = view.findViewById(R.id.txvNomb)
            val txvMarca: TextView = view.findViewById(R.id.txvMarca)
            val txvPrecio: TextView = view.findViewById(R.id.txvPrecio)
            val imgP: ImageView = view.findViewById(R.id.imgP)
        }

        @SuppressLint("MissingInflatedId")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val vista =
                LayoutInflater.from(parent.context).inflate(R.layout.products, parent, false)
            return MyViewHolder(vista)
        }

        override fun getItemCount() = Datos.size
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val itm = regProd2[position]
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

    class citasPRecycler(
        context: Context,
        recyclerView: RecyclerView,
        private val listener: OnItemClickListener?
    ) : RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }
                })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val childView = rv.findChildViewUnder(e.x, e.y)
            if (childView != null && gestureDetector.onTouchEvent(e)) {
                listener?.onItemClick(childView, rv.getChildAdapterPosition(childView))
                return true
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        interface OnItemClickListener {
            fun onItemClick(view: View, position: Int)
        }

    }

}