package com.example.vetsoft.Controlador.ui.Tienda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import com.example.vetsoft.R
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
class historialRe : Fragment() {
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
        return inflater.inflate(R.layout.fragment_historial_re, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}