package com.example.vetsoft.Controlador.ui.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.validation.Validat
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var btnVolverAM: ImageButton
lateinit var spAnimalAM: Spinner
lateinit var spRazaAM: Spinner
lateinit var txtNombreAM: EditText
lateinit var spSexoAM: Spinner
lateinit var btnConfirmAM: Button

class aniC(val id: Int, val nombre: String)

val aniL = mutableListOf<aniC>()

class razaC(val id: Int, val nombre: String)

val razaL = mutableListOf<razaC>()

class AgregarMascota : Fragment() {

    private var conx = conx()
    private var vali = Validat()
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var idAni = 0
    private var idRaza = 0
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
        return inflater.inflate(R.layout.fragment_agregar_mascota, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverAM = requireView().findViewById(R.id.btnVolverAM)
        spAnimalAM = requireView().findViewById(R.id.spAnimalAM)
        spRazaAM = requireView().findViewById(R.id.spRazaAM)
        txtNombreAM = requireView().findViewById(R.id.txtNombreAM)
        btnConfirmAM = requireView().findViewById(R.id.btnConfirmAM)
        spRazaAM.isEnabled = false


        SpinAni(spAnimalAM)
        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        //VALIDACION
        vali.setMax(txtNombreAM, 25);
        val editTextList1 = listOf(txtNombreAM)
        vali.setupET(editTextList1);

        btnVolverAM.setOnClickListener() {
            findNavController().navigate(R.id.action_agregarMascota_to_mainMascota, bundle)
        }
        btnConfirmAM.setOnClickListener() {
            val editTextList = listOf(txtNombreAM)
            val areFieldsValid = vali.areFieldsNotEmpty(editTextList)
            if (areFieldsValid) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Registrar mascota")
                builder.setMessage("¿Está seguro de la información de su mascota?")
                builder.setPositiveButton("Sí") { dialog, which ->
                    Confirmar()
                }

                builder.setNegativeButton("No", null)
                val dialog = builder.create()
                dialog.show()
            }
            else{
                Toast.makeText(requireContext(), "Campos vacíos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun SpinAni(cb: Spinner) {
        try {
            aniL.clear()
            val cadena = "EXEC selectTipoAni;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            st = ps.executeQuery()

            while (st.next()) {
                idAni = st.getString("idTipoAnimal").toInt()
                val nombre = st.getString("nombrePopular")
                aniL.add(aniC(idAni, "$nombre"))
            }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                    aniL.map { it.nombre })

            cb.adapter = adapter
            conx.dbConn()!!.close()
            spRazaAM.isEnabled = true

            cb.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val esp = aniL[position]
                    // = esp.nombre
                    idAni = esp.id
                    SpinRaza(spRazaAM)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "No existen doctores", Toast.LENGTH_SHORT).show()
        }
    }

    fun SpinRaza(cb: Spinner) {
        try {
            razaL.clear()
            val cadena = "EXEC selectRaza ?;"
            val st: ResultSet
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setString(1, idAni.toString())

            st = ps.executeQuery()
            //LLENAR SPINNER
            while (st.next()) {
                idRaza = st.getString("idRaza").toInt()
                val nombre = st.getString("nombreRaza")
                razaL.add(razaC(idRaza, "$nombre"))
            }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                    razaL.map { it.nombre })

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
                    val posi = razaL[position]
                    idRaza = posi.id
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "No existen doctores", Toast.LENGTH_SHORT).show()
        }
    }

    fun Confirmar() {
        try {
            val cadena: String =
                "EXEC insertAnCl ?,?,?;"

            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!

            ps.setInt(1, idCl)
            ps.setInt(2, idRaza)
            ps.setString(3, txtNombreAM.text.toString())
            ps.executeUpdate()

            val bundle = Bundle().apply {
                putInt("idUs", idUs)
                putInt("idCl", idCl)
            }
            Toast.makeText(context, "Mascota agregada correctamente", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_agregarMascota_to_mainMascota, bundle)
        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(
                context,
                "No se pudo agregar",
                Toast.LENGTH_SHORT
            ).show()
        }
        conx.dbConn()!!.close()
    }

}