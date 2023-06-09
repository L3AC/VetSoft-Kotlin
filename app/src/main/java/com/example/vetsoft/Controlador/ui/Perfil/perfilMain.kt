package com.example.vetsoft.Controlador.ui.Perfil

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

lateinit var btnPerC9:ImageButton
lateinit var btnSegC9:ImageButton

class perfilMain : Fragment() {
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
        return inflater.inflate(R.layout.fragment_perfil_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnPerC9 =requireView().findViewById(R.id.btnPerC9)
        btnSegC9 =requireView().findViewById(R.id.btnSegC9)

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnPerC9.setOnClickListener(){
            findNavController().navigate(R.id.action_perfilMain_to_dataPerfil, bundle)
        }
        btnSegC9.setOnClickListener(){
            findNavController().navigate(R.id.action_perfilMain_to_mainSecurity, bundle)
        }
    }


}