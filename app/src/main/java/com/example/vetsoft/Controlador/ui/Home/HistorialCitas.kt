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
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import kotlin.math.log


val regHC = mutableListOf<HistorialCitas.filaHC>()
val myDataHC = mutableListOf<String>()
class HistorialCitas : Fragment() {
    lateinit var btnVolverHC: ImageButton
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
        txtNombHC = requireView().findViewById(R.id.txtNombHC)
        rcMainHC = requireView().findViewById(R.id.rcMainHC)
        rcMainHC.layoutManager = LinearLayoutManager(context)

        val miAdapter = CitasPendientes.citaCard(myDataHC)
        rcMainHC.adapter = miAdapter

        CargarByN()
        val bundle2 = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnVolverHC.setOnClickListener() {
            findNavController().navigate(R.id.action_historialCitas_to_houseCliente, bundle2)
        }
        rcMainHC.addOnItemTouchListener(
            citasHCRecycler(requireContext(), rcMainHC,
                object : citasHCRecycler.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // Acciones a realizar cuando se hace clic en un elemento del RecyclerView
                        val itm = regHC[position]
                        idCit = itm.idC
                        idDoc = itm.idD
                        val bundle = Bundle().apply {
                            putInt("idUs", idUs)
                            putInt("idCl", idCl)
                            putInt("idCit", idCit)
                            putInt("idDoc", idDoc)
                            putInt("citaT", 2)
                        }
                        Log.i("IDE: ", idCit.toString())
                        findNavController().navigate(
                            R.id.action_historialCitas_to_infoCita,
                            bundle
                        )
                    }
                })
        )

        txtNombHC.addTextChangedListener(object : TextWatcher {
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
        myDataHC.clear()
        regHC.clear()
        try {
            var st: ResultSet
            var cadena: String = "SET LANGUAGE Spanish EXEC selectCitaN2 ?,?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idCl)
            ps.setString(2, txtNombHC.text.toString())
            st = ps.executeQuery()

            while (st?.next() == true) {
                val col1 = st.getInt("idCita")
                val col2 = st.getInt("idDoctor")
                val col3 = st.getString("nombre")
                val col4 = st.getString("fecha")
                val col5 = st.getString("Doctor")
                val col6 = st.getString("estado")

                regHC.add(filaHC(col1, col2, col3, col4, col5, col6))
                val newElement = "$col3"
                myDataHC.add(newElement)

                val miAdapter = citaCardH(myDataHC)
                rcMainHC.adapter = miAdapter
            }

        } catch (ex: SQLException) {
            Log.i("ol", ex.message.toString())
            Toast.makeText(context, "Error al cargar cita N", Toast.LENGTH_SHORT).show()
        }
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