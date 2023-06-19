package com.example.vetsoft.ui.house

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vetsoft.Conex.conx
import com.example.vetsoft.R
import com.example.vetsoft.Validation.Validat

class CitasPendientes : Fragment() {
    private var idUs: Int = 0
    private var idCl:Int=0
    private var conx = conx()
    private var vali = Validat()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_citas_pendientes, container, false)
    }

}