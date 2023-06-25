package com.example.vetsoft.ui.house

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsoft.Conex.conx
import com.example.vetsoft.R
import com.example.vetsoft.Validation.Validat
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var btnVolverCP:ImageButton
lateinit var spBusqCP:Spinner
lateinit var spTimeCP:Spinner
lateinit var txtNombCP:EditText
lateinit var rcMainCP:RecyclerView

class filaCP(val id: Int, val nMasc:String,val fecha:String,val nDoc:String)
val regCP = mutableListOf<filaCP>()
val myDataCP = mutableListOf<String>()
class CitasPendientes : Fragment() {
    private var idUs: Int = 0
    private var idCl:Int=0
    private var conx = conx()
    private var vali = Validat()
    val busque = listOf("Por tiempo","Por nombre")
    val time = listOf("2 semanas","2 meses","6 meses")
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

        LlenarSpin()
        spBusqCP.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,view: View?, position: Int,id: Long
            ) {
                when (position) {
                    0 -> {
                        spTimeCP.isVisible=false
                        txtNombCP.isVisible=true
                    }
                    1 -> {
                        spTimeCP.isVisible=true
                        txtNombCP.isVisible=false
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        spTimeCP.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

               /* val esp = espL[position]
                // = esp.nombre
                idEsp = esp.id
                SpinDoc(spinDoc5)
                if (txtFecha5.text.isEmpty()) {

                } else {
                    verifCita()
                }*/
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }
    fun CargarByN() {
        //myData1.clear()
        regCP.clear()
        try {
            var st: ResultSet
            var cadena: String ="SET LANGUAGE Spanish EXEC selectCitaN ?,'%?%';"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idCl)
            ps.setString(1, txtNombCP.text.toString())
            st = ps.executeQuery()

            while (st?.next() == true) {

                val col1 = st.getInt("idCita")
                val col2 = st.getString("nombre")
                val col3 = st.getString("fecha")
                val col4 = st.getString("Doctor")

                regCP.add(filaCP(col1,col2,col3,col4))

                /*val newElement = "Nombre: $col2"
                myData1.add(newElement)*/
            }
        } catch (ex: SQLException) {
            Log.i("ol",ex.message.toString())
            Toast.makeText(context, "Error al mostrar animal", Toast.LENGTH_SHORT).show()
        }
    }
    fun CargarByF() {
        //myData1.clear()
        regCP.clear()
        try {
            var st: ResultSet
            var cadena: String ="SET LANGUAGE Spanish EXEC selectCitaN ?,'%?%';"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idCl)
            ps.setString(1, txtNombCP.text.toString())
            st = ps.executeQuery()

            while (st?.next() == true) {

                val col1 = st.getInt("idAnimal")
                val col2 = st.getString("nombre")

                //regCP.add(fila(col1))

                /*val newElement = "Nombre: $col2"
                myData1.add(newElement)*/
            }
        } catch (ex: SQLException) {
            Log.i("ol",ex.message.toString())
            Toast.makeText(context, "Error al mostrar animal", Toast.LENGTH_SHORT).show()
        }
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
    class citaCard(/*private val Datos: MutableList<String>,private val btnClick:(Int)->Unit*/) :
        RecyclerView.Adapter<citaCard.MyViewHolder>() {
        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txvMascota: TextView = view.findViewById(R.id.txvMascota)
            val txvFecha: TextView = view.findViewById(R.id.txvFecha)
            val txvDoc: TextView = view.findViewById(R.id.txvDoc)
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

       override fun getItemCount(): Int /*= Datos.size*/ {
           return TODO("Provide the return value")
       }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val itm = regCP[position]
            holder.txvMascota.setText(itm.nMasc)
            holder.txvFecha.setText(itm.fecha)
            holder.txvDoc.setText(itm.nDoc)
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