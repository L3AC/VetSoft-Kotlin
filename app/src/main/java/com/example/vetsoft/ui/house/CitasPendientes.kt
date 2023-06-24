package com.example.vetsoft.ui.house

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsoft.Conex.conx
import com.example.vetsoft.R
import com.example.vetsoft.Validation.Validat
lateinit var btnVolverCP:ImageButton
lateinit var spBusqCP:Spinner
lateinit var spTimeCP:Spinner
lateinit var txtNombCP:EditText
lateinit var rcMainCP:RecyclerView

class filaCP(val id: Int)
val regCP = mutableListOf<filaCP>()
val myDataCP = mutableListOf<String>()
class CitasPendientes : Fragment() {
    private var idUs: Int = 0
    private var idCl:Int=0
    private var conx = conx()
    private var vali = Validat()
    val busque = listOf("Por tiempo","Por nombre")
    val time = listOf("15 dias","2 meses","6 meses")
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
        return inflater.inflate(R.layout.fragment_citas_pendientes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverCP=requireView().findViewById(R.id.btnVolverCP)
        spBusqCP=requireView().findViewById(R.id.spBusqCP)
        spTimeCP=requireView().findViewById(R.id.spTimeCP)
        txtNombCP=requireView().findViewById(R.id.txtNombCP)
        rcMainCP=requireView().findViewById(R.id.rcMainCP)

        spTimeCP.isVisible=false
        LlenarSpin()
    }
    fun LlenarSpin() {
        val adaptadorSpinner =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, busque)
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = requireView().findViewById<Spinner>(R.id.spBusqCP)
        spinner.adapter = adaptadorSpinner

        val adaptadorSpinner2 =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, time)
        adaptadorSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner2 = requireView().findViewById<Spinner>(R.id.spTimeCP)
        spinner2.adapter = adaptadorSpinner2
    }
    class citaCard(private val Datos: MutableList<String>/*,private val btnClick:(Int)->Unit*/) :
        RecyclerView.Adapter<citaCard.MyViewHolder>() {
        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.txvMascota)
            ///val btn:Button=view.findViewById<Button>(R.id.txCarta)
            //   val imageView: ImageView = view.findViewById(R.id.image_view)
        }
        @SuppressLint("MissingInflatedId")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val vista =
                LayoutInflater.from(parent.context).inflate(R.layout.citas, parent, false)
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