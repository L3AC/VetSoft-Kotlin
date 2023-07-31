package com.example.vetsoft.Controlador.ui.Tienda

import android.annotation.SuppressLint
import android.content.Context
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

class filaProd(
    val id: Int, val nTipoProd: String
)

val regProd = mutableListOf<filaProd>()
val myDataProd = mutableListOf<String>()

class CatalogoProd : Fragment() {
    lateinit var btnVolverCat: ImageButton
    lateinit var txtNProd: EditText
    lateinit var rcCataProd: RecyclerView
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
        return inflater.inflate(R.layout.fragment_catalogo_prod, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverCat = requireView().findViewById(R.id.btnVolverCat)
        txtNProd = requireView().findViewById(R.id.txtNProd)
        rcCataProd = requireView().findViewById(R.id.rcCataProd)
        rcCataProd.layoutManager = LinearLayoutManager(context)

        val miAdapter = catCard(myDataProd)
        rcCataProd.adapter = miAdapter

        CargarByN()

        rcCataProd.addOnItemTouchListener(
            CatalogoProd.citasPRecycler(requireContext(), rcCataProd,
                object : citasPRecycler.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // Acciones a realizar cuando se hace clic en un elemento del RecyclerView
                        val itm = regProd[position]
                        idTipoP = itm.id
                        val bundle = Bundle().apply {
                            putInt("idUs", idUs)
                            putInt("idCl", idCl)
                            putInt("idTipoP", idTipoP)
                        }
                        Log.i("IDTIPO: ", idTipoP.toString())
                        findNavController().navigate(
                            R.id.action_catalogoProd_to_productos,
                            bundle
                        )
                    }
                })
        )
        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnVolverCat.setOnClickListener(){
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
    }

    fun CargarByN() {
        myDataProd.clear()
        regProd.clear()
        try {
            var st: ResultSet
            var cadena: String = "select * from tbTipoProductos where tipo like ?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setString(1, '%'+txtNProd.text.toString()+'%')
            Log.i("sda",'%'+txtNProd.text.toString()+'%')
            st = ps.executeQuery()

            while (st?.next() == true) {
                val col1 = st.getInt("idTipoProducto")
                val col2 = st.getString("tipo")

                regProd.add(filaProd(col1, col2))
                val newElement = "$col2"
                myDataProd.add(newElement)

                val miAdapter = catCard(myDataProd)
                rcCataProd.adapter = miAdapter
            }

        } catch (ex: SQLException) {
            Log.i("ol", ex.message.toString())
            Toast.makeText(context, "Error al cargar cita N", Toast.LENGTH_SHORT).show()
        }
    }

    class catCard(private val Datos: MutableList<String>/*,private val btnClick:(Int)->Unit*/) :
        RecyclerView.Adapter<catCard.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txvTipoP: TextView = view.findViewById(R.id.txvTPN)
        }

        @SuppressLint("MissingInflatedId")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val vista =
                LayoutInflater.from(parent.context).inflate(R.layout.tipoprod, parent, false)
            return MyViewHolder(vista)
        }

        override fun getItemCount() = Datos.size
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val itm = regProd[position]
            holder.txvTipoP.text = itm.nTipoProd
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

