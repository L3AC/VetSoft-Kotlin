package com.example.vetsoft.Controlador.ui.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.validation.Validat
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

lateinit var btnVolverF5: ImageButton
lateinit var btnAddF5: ImageView
lateinit var txvAniF5:TextView
lateinit var txvRazaF5:TextView
lateinit var txvNombF5:TextView
lateinit var txvEdadF5:TextView
lateinit var txvPesoF5:TextView
lateinit var txvSexoF5:TextView
//lateinit var txvPadF5:TextView


class InfoMascota : Fragment() {
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
            idAni = arguments?.getInt("idAni")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_mascota, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverF5 =requireView().findViewById(R.id.btnVolverF5)
        btnAddF5 =requireView().findViewById(R.id.btnAddF5)
        txvAniF5 =requireView().findViewById(R.id.txvAniF5)
        txvRazaF5 =requireView().findViewById(R.id.txvRazaF5)
        txvNombF5 =requireView().findViewById(R.id.txvNombF5)
        txvEdadF5 =requireView().findViewById(R.id.txvEdadF5)
        txvPesoF5 =requireView().findViewById(R.id.txvPesoF5)
        txvSexoF5 =requireView().findViewById(R.id.txvSexoF5)
        //txvPadF5=requireView().findViewById(R.id.txvPadF5)

        cargarData()
        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
            putInt("idAni", idAni)
        }
        btnAddF5.setOnClickListener(){
            findNavController().navigate(R.id.action_infoMascota_to_agendarCIta, bundle)
        }
        btnVolverF5.setOnClickListener(){
            findNavController().navigate(R.id.action_infoMascota_to_mainMascota, bundle)
        }
    }
    fun cargarData() {
        try {
            var st: ResultSet
            val cadena =
                "EXEC selectAnInf ?;"
            val ps: PreparedStatement = conx.dbConn()?.prepareStatement(cadena)!!
            ps.setInt(1, idAni)
            st = ps.executeQuery()
            st.next()
            txvAniF5.setText("Animal:  "+st.getString("nombrePopular"))
            txvRazaF5.setText("Raza:  "+st.getString("nombreRaza"))
            txvNombF5.setText("Nombre:  "+st.getString("nombre"))
            txvPesoF5.setText("Peso:  "+st.getString("peso"))
            if(st.getString("Edad")==null){//VERIFICAR PARA PONER PENDIENTE
                txvEdadF5.setText("Edad:  Pendiente")
            }
            else{
                txvEdadF5.setText("Edad:  "+st.getString("Edad"))
            }

            txvSexoF5.setText("Sexo:  "+st.getString("sexo"))
            //txvPadF5.setText("Padecimientos: "+st.getString("padecimientos"))

        } catch (ex: SQLException) {
            Log.e("Error: ", ex.message!!)
            Toast.makeText(context, "Error al cargar", Toast.LENGTH_SHORT).show()
        }
        conx.dbConn()!!.close()
    }


}