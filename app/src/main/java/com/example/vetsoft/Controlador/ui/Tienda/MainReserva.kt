package com.example.vetsoft.Controlador.ui.Tienda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.R

class MainReserva : Fragment() {
    lateinit var btnReM: ImageButton
    lateinit var btnHistM: ImageButton
    lateinit var btnBack: ImageView
    private var idUs: Int = 0
    private var idCl: Int = 0

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
        return inflater.inflate(R.layout.fragment_main_reserva, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBack = requireView().findViewById(R.id.btnBackM)
        btnReM = requireView().findViewById(R.id.btnReM)
        btnHistM = requireView().findViewById(R.id.btnHistM)


        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnBack.setOnClickListener(){
            findNavController().navigate(R.id.action_mainReserva_to_mainTienda, bundle)
        }
        btnReM.setOnClickListener(){
            findNavController().navigate(R.id.action_mainReserva_to_prodReserv, bundle)
        }
        btnHistM.setOnClickListener(){
            findNavController().navigate(R.id.action_mainReserva_to_historialRe, bundle)
        }
    }


}