package com.example.vetsoft.ui.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.vetsoft.Conex.conx
import com.example.vetsoft.R
import com.example.vetsoft.Validation.Validat

lateinit var btnVolverS9:ImageButton
lateinit var btnContraS9:ImageButton
lateinit var btnPregS9:ImageButton

class MainSecurity : Fragment() {
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
        return inflater.inflate(R.layout.fragment_main_security, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverS9 =requireView().findViewById(R.id.btnVolverS9)
        btnPregS9 =requireView().findViewById(R.id.btnPregS9)
        btnContraS9 =requireView().findViewById(R.id.btnContraS9)

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnVolverS9.setOnClickListener(){
            findNavController().navigate(R.id.action_mainSecurity_to_perfilMain, bundle)
        }
        btnPregS9.setOnClickListener(){
            findNavController().navigate(R.id.action_mainSecurity_to_changePreguntas, bundle)
        }
        btnContraS9.setOnClickListener(){
            findNavController().navigate(R.id.action_mainSecurity_to_changePassw, bundle)
        }
    }


}