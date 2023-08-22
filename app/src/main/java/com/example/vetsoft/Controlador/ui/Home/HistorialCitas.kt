package com.example.vetsoft.Controlador.ui.Home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R


val regHC = mutableListOf<HistorialCitas.filaHC>()
val myDataHC = mutableListOf<String>()
class HistorialCitas : Fragment() {
    lateinit var btnVolverHC: ImageButton
    lateinit var spBusqHC: Spinner
    lateinit var spTimeHC: Spinner
    lateinit var txtNombHC: EditText
    lateinit var rcMainHC: RecyclerView

    class filaHC(
        val idC: Int, val idD: Int, val nMasc: String,
        val fecha: String, val nDoc: String, val estado: String
    )


    private var idUs: Int = 0
    private var idCl: Int = 0
    private var idCit: Int = 0
    private var idDoc: Int = 0
    private var conx = conx()
    private var vali = Validat()
    val busque = listOf("Tiempo", "Nombre")
    val time = listOf("6 meses", "2 meses", "2 semanas")

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
        return inflater.inflate(R.layout.fragment_historial_citas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverHC = requireView().findViewById(R.id.btnVolverHC)
        spBusqHC = requireView().findViewById(R.id.spBusqHC)
        spTimeHC = requireView().findViewById(R.id.spTimeHC)
        txtNombHC = requireView().findViewById(R.id.txtNombHC)
        rcMainHC = requireView().findViewById(R.id.rcMainHC)
        rcMainHC.layoutManager = LinearLayoutManager(context)

        val miAdapter = CitasPendientes.citaCard(myDataHC)
        rcMainHC.adapter = miAdapter

    }
    class citaCardH(private val Datos: MutableList<String>/*,private val btnClick:(Int)->Unit*/) :
        RecyclerView.Adapter<citaCardH.MyViewHolder>() {

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txvMascota: TextView = view.findViewById(R.id.txvMascota)
            val txvFecha: TextView = view.findViewById(R.id.txvFecha)
            val txvDoc: TextView = view.findViewById(R.id.txvDoc)
            val txvEstado: TextView = view.findViewById(R.id.txvEstadoC)
            ///val btn:Button=view.findViewById<Button>(R.id.txCarta)
            //   val imageView: ImageView = view.findViewById(R.id.image_view)
        }

        @SuppressLint("MissingInflatedId")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val vista =
                LayoutInflater.from(parent.context).inflate(R.layout.citas, parent, false)
            return MyViewHolder(vista)
        }

        override fun getItemCount() = Datos.size
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            if (regHC.isEmpty()) {
                holder.txvMascota.isVisible = false
                holder.txvFecha.isVisible = false
                holder.txvDoc.isVisible = false
                holder.txvEstado.isVisible = false
            } else {
                val itmg = regHC[position]
                holder.txvMascota.text = "Mascota: " + Datos[position]
                holder.txvFecha.text = "Fecha: " + itmg.fecha
                holder.txvDoc.text = "Doctor: " + itmg.nDoc
                holder.txvEstado.text = "Estado: " + itmg.estado
                //Reemplazamos la imagen
                //  holder.imageView.setImageResource(Imagenes[position])
            }
        }
    }

    class citasHCRecycler(
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