package com.example.vetsoft.Controlador.ui.Home

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.validation.Validat
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
@SuppressLint("StaticFieldLeak")
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
    private var idCit:Int=0
    private var conx = conx()
    private var vali = Validat()
    val busque = listOf("Nombre","Tiempo")
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
        btnVolverCP =requireView().findViewById(R.id.btnVolverCP)
        spBusqCP =requireView().findViewById(R.id.spBusqCP)
        spTimeCP =requireView().findViewById(R.id.spTimeCP)
        txtNombCP =requireView().findViewById(R.id.txtNombCP)
        rcMainCP =requireView().findViewById(R.id.rcMainCP)
        rcMainCP.layoutManager = LinearLayoutManager(context)

        LlenarSpin()
        CargarByN()
        val bundle2 = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnVolverCP.setOnClickListener(){
            findNavController().navigate(R.id.action_citasPendientes_to_houseCliente, bundle2)
        }

        /*spBusqCP.onItemSelectedListener = object :
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
                when (position) {
                    0 -> {
                        CargarByF(15)
                    }
                    1 -> {
                        CargarByF(60)
                    }
                    2 -> {
                        CargarByF(180)
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }*/
        rcMainCP.addOnItemTouchListener(
            citasPRecycler(requireContext(), rcMainCP,
                object : citasPRecycler.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // Acciones a realizar cuando se hace clic en un elemento del RecyclerView
                        val itm = regCP[position]
                        idCit = itm.id
                        val bundle = Bundle().apply {
                            putInt("idUs", idUs)
                            putInt("idCl", idCl)
                            putInt("idCit", idCit)
                        }
                        Log.i("IDE: ", idCit.toString())
                        findNavController().navigate(R.id.action_citasPendientes_to_infoCita, bundle)
                    }
                })
        )

        txtNombCP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CargarByN()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        val miAdapter2 = citaCard(myDataCP)
        rcMainCP.adapter = miAdapter2

    }
    fun CargarByN() {
        myDataCP.clear()
        regCP.clear()
        try {
            var st: ResultSet
            var cadena: String ="select idCita, CONVERT(varchar, fechahora, 100) as fecha,a.nombre, CONCAT(d.nombre, ' ', d.apellido) as 'Doctor'\n" +
                    "from tbCitas c,tbAnimales a, tbDoctores d where c.idAnimal=a.idAnimal and d.idDoctor=c.idDoctor " +
                    "and a.idCliente=? and estado='Pendiente' and a.nombre LIKE '%"+txtNombCP.text.toString()+"%';";
                //"SET LANGUAGE Spanish EXEC selectCitaN ?,?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idCl)
            //ps.setString(2, txtNombCP.text.toString())
            Log.i("con",ps.toString())
            Log.i("con",idCl.toString())
            st = ps.executeQuery()

            while (st?.next() == true) {

                val col1 = st.getInt("idCita")
                val col2 = st.getString("nombre")
                val col3 = st.getString("fecha")
                val col4 = st.getString("Doctor")
                Log.i("dfg",col2)

                regCP.add(filaCP(col1,col2,col3,col4))
                val newElement = "$col2"
                myDataCP.add(newElement)
            }
        } catch (ex: SQLException) {
            Log.i("ol",ex.message.toString())
            Toast.makeText(context, "Error al cargar cita N", Toast.LENGTH_SHORT).show()
        }
    }
    fun CargarByF(Dias:Int) {
        regCP.clear()
        try {
            var st: ResultSet
            var cadena: String ="SET LANGUAGE Spanish EXEC selectCitaD ?,?"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idCl)
            ps.setInt(2, Dias)
            st = ps.executeQuery()

            while (st?.next() == true) {

                val col1 = st.getInt("idCita")
                val col2 = st.getString("nombre")
                val col3 = st.getString("fecha")
                val col4 = st.getString("Doctor")

                regCP.add(filaCP(col1,col2,col3,col4))
            }
        } catch (ex: SQLException) {
            Log.i("ol",ex.message.toString())
            Toast.makeText(context, "Error al cargar cita F", Toast.LENGTH_SHORT).show()
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

    class citaCard(private val Datos: MutableList<String>/*,private val btnClick:(Int)->Unit*/) :
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
            return MyViewHolder(vista)
        }
       var Hola:Int=0
       override fun getItemCount()=Datos.size
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val itmg = regCP[position]
            holder.txvMascota.text = Datos[position]
            holder.txvFecha.text = itmg.fecha
            holder.txvDoc.text = itmg.nDoc
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