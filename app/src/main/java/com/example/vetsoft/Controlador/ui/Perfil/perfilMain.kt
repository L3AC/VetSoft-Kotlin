package com.example.vetsoft.Controlador.ui.Perfil

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Controlador.Main.MainActivity
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
import com.example.vetsoft.Controlador.validation.Validat

lateinit var btnPerC9:ImageButton
lateinit var btnSegC9:ImageButton
lateinit var btnCerrar:ImageButton
lateinit var btnPer2: TextView
lateinit var btnPer3: TextView
lateinit var btnSeg2: TextView
lateinit var btnSeg3: TextView
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
        btnPer2=requireView().findViewById(R.id.txvCitasP8)
        btnSeg2=requireView().findViewById(R.id.txvCitasP23)
        btnSeg3=requireView().findViewById(R.id.txvCitasP9)
        btnPer3=requireView().findViewById(R.id.txvCitasP24)
        btnCerrar = requireView().findViewById(R.id.btnCerrar)

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnPerC9.setOnClickListener(){
            findNavController().navigate(R.id.action_perfilMain_to_dataPerfil, bundle)
        }
        btnPer2.setOnClickListener {
            findNavController().navigate(R.id.action_perfilMain_to_dataPerfil, bundle)
        }
        btnPer3.setOnClickListener {
            findNavController().navigate(R.id.action_perfilMain_to_dataPerfil, bundle)
        }
        btnSeg2.setOnClickListener {
            findNavController().navigate(R.id.action_perfilMain_to_mainSecurity, bundle)
        }
        btnSeg3.setOnClickListener {
            findNavController().navigate(R.id.action_perfilMain_to_mainSecurity, bundle)
        }
        btnSegC9.setOnClickListener(){
            findNavController().navigate(R.id.action_perfilMain_to_mainSecurity, bundle)
        }
        btnCerrar.setOnClickListener(){
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }


}