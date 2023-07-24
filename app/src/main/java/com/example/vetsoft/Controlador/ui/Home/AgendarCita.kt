package com.example.vetsoft.Controlador.ui.Home

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Controlador.Main.txtNaci2
import com.example.vetsoft.Controlador.ui.Perfil.txtNaciDP
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.validation.Validat
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class servC(val id: Int, val nombre: String)

val servL = mutableListOf<servC>()

class espC(val id: Int, val nombre: String)

val espL = mutableListOf<espC>()

class docC(val id: Int, val nombre: String)

val docL = mutableListOf<docC>()
lateinit var btnVolver5: ImageButton
lateinit var spinServ5: Spinner
lateinit var spinArea5: Spinner
lateinit var spinDoc5: Spinner
lateinit var btnFecha5: ImageButton
lateinit var txtFecha5: EditText
lateinit var spinEnt5: Spinner
lateinit var txtHora5: EditText
lateinit var txvDispo5: TextView
lateinit var txtNota5: EditText
lateinit var btnConfirm5: Button

class AgendarCIta : Fragment() {
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var idAni: Int = 0
    private var conx = conx()
    private var idServ = 0
    private var nServ = ""
    private var idEsp = 0
    private var nEsp = ""
    private var idDoc = 0
    private var nDoc = ""
    private var vali = Validat()
    private var fechaSql = ""
    private var dateh = ""
    val hora = listOf(
        /*"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00",*/ "07:00",
        "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
        "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"/*, "22:00", "23:00"*/
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs = arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
            idAni = arguments?.getInt("idAni")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agendar_cita, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolver5 = requireView().findViewById(R.id.btnVolver5)
        spinServ5 = requireView().findViewById(R.id.spinServ5)
        spinArea5 = requireView().findViewById(R.id.spinArea5)
        spinDoc5 = requireView().findViewById(R.id.spinDoc5)
        btnFecha5 = requireView().findViewById(R.id.btnFecha5)
        txtFecha5 = requireView().findViewById(R.id.txtFecha5)
        spinEnt5 = requireView().findViewById(R.id.spinEnt5)
        txtHora5 = requireView().findViewById(R.id.txtHora5)
        txvDispo5 = requireView().findViewById(R.id.txvDispo5)
        txtNota5 = requireView().findViewById(R.id.txtNota5)
        btnConfirm5 = requireView().findViewById(R.id.btnConfirm5)
        spinDoc5.isEnabled = false
        txvDispo5.isVisible = false
        btnConfirm5.isEnabled = false
        txtFecha5.isEnabled = false
        txtHora5.isEnabled = false

        //vali.configEditText(txtNota5,300,"^[a-zA-Z0-9]+.,")

        SpinHora()
        SpinServ(spinServ5)
        SpinArea(spinArea5)
        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
            putInt("idAni", idAni)
        }
        btnVolver5.setOnClickListener() {
            findNavController().navigate(R.id.action_agendarCIta_to_infoMascota, bundle)
        }
        btnFecha5.setOnClickListener() {
            showDatePickerDialog()
        }
        btnConfirm5.setOnClickListener() {
            Confirmar()

        }
        spinEnt5.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                txtHora5.setText(spinEnt5.selectedItem.toString())
                if (txtFecha5.text.isEmpty()) {

                } else {
                    verifCita()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun SpinHora() {
        val adaptadorSpinner =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hora)
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = requireView().findViewById<Spinner>(R.id.spinEnt5)
        spinner.adapter = adaptadorSpinner
    }

    fun SpinServ(cb: Spinner) {
        try {
            servL.clear()
            val cadena = "select idTipoServicio,nombre from tbTipoServicio ts,tbNivelServicio ns\n" +
                    "   where ts.idNivelServicio=ns.idNivelServicio and ts.idNivelServicio=2;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            st = ps.executeQuery()

            //LLENAR SPINNER
            while (st.next()) {
                idServ = st.getString("idTipoServicio").toInt()
                val serv = st.getString("nombre")
                servL.add(servC(idServ, "$serv"))
            }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                    servL.map { it.nombre })
            cb.adapter = adapter

            conx.dbConn()!!.close()

            cb.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //spinDoc = parent.getItemAtPosition(position).toString()
                    val espc = servL[position]
                    //nEsp = espc.nombre
                    idServ = espc.id
                    //SpinDoc(cbDoc)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "Error al cargar los servicios", Toast.LENGTH_SHORT).show()
        }
    }

    fun SpinArea(cb: Spinner) {
        try {
            espL.clear()
            val cadena = "   select * from tbEspecialidades;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            st = ps.executeQuery()

            while (st.next()) {
                idEsp = st.getString("idEspecialidad").toInt()
                val nombre = st.getString("especialidad")
                espL.add(espC(idEsp, "$nombre"))
            }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                    espL.map { it.nombre })

            cb.adapter = adapter
            conx.dbConn()!!.close()
            spinDoc5.isEnabled = true

            cb.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val esp = espL[position]
                    // = esp.nombre
                    idEsp = esp.id
                    SpinDoc(spinDoc5)
                    if (txtFecha5.text.isEmpty()) {

                    } else {
                        verifCita()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "No existen doctores", Toast.LENGTH_SHORT).show()
        }
    }

    fun SpinDoc(cb: Spinner) {
        try {
            docL.clear()
            val cadena = "select idDoctor,CONCAT(nombre,' ',apellido) as Nombre from tbDoctores d,tbEspecialidades e\n" +
                    "   where d.idEspecialidad=e.idEspecialidad and d.idEspecialidad=?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setString(1, idEsp.toString())

            st = ps.executeQuery()
            //LLENAR SPINNER
            while (st.next()) {
                idDoc = st.getString("idDoctor").toInt()
                val nombre = st.getString("nombre")
                docL.add(docC(idDoc, "$nombre"))
            }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                    docL.map { it.nombre })

            cb.adapter = adapter
            conx.dbConn()!!.close()


            cb.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val doct = docL[position]
                    idDoc = doct.id
                    if (txtFecha5.text.isEmpty()) {

                    } else {
                        verifCita()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "No existen doctores", Toast.LENGTH_SHORT).show()
        }
    }

    fun verifCita() {
        try {
            dateh = fechaSql + " " + txtHora5.text
            Log.i("date",dateh)
            val cadena = "select * from tbCitas c where idDoctor=? and fecha=? and hora=? and estado='Pendiente';"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setString(1, idDoc.toString())
            ps.setString(2, fechaSql)
            ps.setString(3, spinEnt5.selectedItem.toString() )
            st = ps.executeQuery()
            st.next()

            val found = st.row
            if (found == 1) {
                txvDispo5.isVisible = true
                txvDispo5.text = "No disponible"
                btnConfirm5.isEnabled = false

            } else {
                txvDispo5.isVisible = true
                txvDispo5.text = "Disponible"
                btnConfirm5.isEnabled = true
            }
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "No se verifico", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }

    fun Confirmar() {
        try {
            val cadena: String =
                "insert into tbCitas values(?,?,?,'Pendiente',?," +
                        "'',?,?,GETDATE())"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, idAni)
            ps.setInt(2, idServ)
            ps.setInt(3, idDoc)
            ps.setString(4, txtNota5.text.toString())
            ps.setString(5, fechaSql)
            ps.setString(6, spinEnt5.selectedItem.toString() )
            ps.executeUpdate()

            val bundle = Bundle().apply {
                putInt("idUs", idUs)
                putInt("idCl", idCl)
                putInt("idAni", idAni)
            }
            Toast.makeText(context, "Cita agendada correctamente", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_agendarCIta_to_infoMascota, bundle)
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(
                context,
                "No se pudo agendar",
                Toast.LENGTH_SHORT
            ).show()
        }
        conx.dbConn()!!.close()
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear un DatePickerDialog con la fecha actual y la fecha mínima permitida
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year,month, day ->
                // Aquí puedes hacer algo con la fecha seleccionada, como guardarla en una variable
                val mes = month + 1
                fechaSql = "$year-$mes-$day"
                txtFecha5?.setText("$day-$mes-$year")

                // También puedes mostrarla en un TextView, etc.
            },
            year, month, dayOfMonth
        )

        // 18 años=6570 dias

        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

}