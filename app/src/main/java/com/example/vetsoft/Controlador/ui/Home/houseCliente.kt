package com.example.vetsoft.Controlador.ui.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.validation.Validat
lateinit var btnMasc4:ImageButton
lateinit var btnPnd4:ImageButton
lateinit var btnHist4:ImageButton
class houseCliente : Fragment() {
    private var idUs: Int = 0
    private var idCl:Int=0
    private var conx = conx()
    private var vali = Validat()
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
        return inflater.inflate(R.layout.fragment_house_cliente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnMasc4 =requireView().findViewById(R.id.btnMasc4)
        btnPnd4 =requireView().findViewById(R.id.btnPnd4)
        btnHist4 =requireView().findViewById(R.id.btnHist4)

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnMasc4.setOnClickListener(){
            findNavController().navigate(R.id.action_houseCliente_to_mainMascota, bundle)
        }
        btnPnd4.setOnClickListener(){
            findNavController().navigate(R.id.action_houseCliente_to_citasPendientes, bundle)
        }
        btnHist4.setOnClickListener(){
            findNavController().navigate(R.id.action_houseCliente_to_historialCitas, bundle)
        }
    }

}