package com.example.vetsoft.Controlador.ui.Tienda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetsoft.R

class MainTienda : Fragment() {
    lateinit var btnProd:ImageButton
    lateinit var btnReserv:ImageButton
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
        return inflater.inflate(R.layout.fragment_main_tienda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnProd = requireView().findViewById(R.id.btnProd)
        btnReserv = requireView().findViewById(R.id.btnReserv)

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnProd.setOnClickListener(){
            findNavController().navigate(R.id.action_mainTienda_to_catalogoProd, bundle)
        }
        btnReserv.setOnClickListener(){
            findNavController().navigate(R.id.action_mainTienda_to_mainReserva, bundle)
        }
    }
}