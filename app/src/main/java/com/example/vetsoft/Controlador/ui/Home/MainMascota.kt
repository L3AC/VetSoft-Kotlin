package com.example.vetsoft.Controlador.ui.Home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.validation.Validat
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var btnVolverM5:ImageButton
lateinit var rcMainMasc:RecyclerView
lateinit var btnAddM5:ImageView

class fila(val id: Int)
val reg1 = mutableListOf<fila>()
val myData1 = mutableListOf<String>()
class MainMascota : Fragment() {
    private var idUs: Int = 0
    private var idCl:Int=0
    private var idAni:Int=0
    private var conx = conx()
    private var vali = Validat()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs = arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
            Log.i("IDCl: ", idCl.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_mascota, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverM5 =requireView().findViewById(R.id.btnVolverM5)
        rcMainMasc =requireView().findViewById(R.id.rcMainMasc)
        btnAddM5 =requireView().findViewById(R.id.btnAddM5)
        rcMainMasc.layoutManager = LinearLayoutManager(context)

        CargarDatos()
        val bundle2 = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
            putInt("idAni", idAni)
        }
        btnVolverM5.setOnClickListener(){
            findNavController().navigate(R.id.action_mainMascota_to_houseCliente, bundle2)
        }
        btnAddM5.setOnClickListener(){
            findNavController().navigate(R.id.action_mainMascota_to_agregarMascota, bundle2)
        }

        rcMainMasc.addOnItemTouchListener(
            RecyclerItemClickListener(requireContext(), rcMainMasc,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // Acciones a realizar cuando se hace clic en un elemento del RecyclerView
                        val itm = reg1[position]
                        idAni = itm.id
                        val bundle = Bundle().apply {
                            putInt("idUs", idUs)
                            putInt("idCl", idCl)
                            putInt("idAni", idAni)
                        }
                        Log.i("IDE: ", idAni.toString())
                        findNavController().navigate(R.id.action_mainMascota_to_infoMascota, bundle)
                    }
                })
        )
        val miAdapter = misCard(myData1)
        rcMainMasc.adapter = miAdapter
    }
    fun CargarDatos() {
        myData1.clear()
        reg1.clear()
        try {

            var st: ResultSet
            var cadena: String ="EXEC selectAnCl ?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idCl)
            st = ps.executeQuery()

            while (st?.next() == true) {

                val col1 = st.getInt("idAnimal")
                val col2 = st.getString("nombre")

                reg1.add(fila(col1))

                val newElement = "$col2"
                myData1.add(newElement)
            }
        } catch (ex: SQLException) {
            Log.i("ol",ex.message.toString())
            Toast.makeText(context, "Error al mostrar animal", Toast.LENGTH_SHORT).show()
        }
    }
    class misCard(private val Datos: MutableList<String>/*,private val btnClick:(Int)->Unit*/) :
        RecyclerView.Adapter<misCard.MyViewHolder>() {
        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.txvMascota)
            ///val btn:Button=view.findViewById<Button>(R.id.txCarta)
            //   val imageView: ImageView = view.findViewById(R.id.image_view)
        }
        @SuppressLint("MissingInflatedId")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val vista =
                LayoutInflater.from(parent.context).inflate(R.layout.mascotas, parent, false)
            /*val lol=vista.findViewById<TextView>(R.id.txCarta)
            lol.setOnClickListener(){
                Toast.makeText(parent.context, "PARA UN POCO NENE", Toast.LENGTH_SHORT).show()
            }*/
            return MyViewHolder(vista)
        }

        override fun getItemCount() = Datos.size
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.setText(Datos[position])
            holder.textView.setOnClickListener(){
                //btnClick(position)
            }
            //Reemplazamos la imagen
            //  holder.imageView.setImageResource(Imagenes[position])
        }

    }
    class RecyclerItemClickListener(
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