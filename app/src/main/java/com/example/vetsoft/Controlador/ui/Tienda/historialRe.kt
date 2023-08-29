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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
class historialRe : Fragment() {
    lateinit var btnVolverCat2: ImageButton
    lateinit var txtNProd2: EditText
    lateinit var rcHistProd: RecyclerView
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var idTipoP: Int = 0
    private var conx = conx()
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
        btnVolverCat2 = requireView().findViewById(R.id.btnVolverCat2)
        txtNProd2 = requireView().findViewById(R.id.txtNProd2)
        rcHistProd = requireView().findViewById(R.id.rcHistProd)
        rcHistProd.layoutManager = LinearLayoutManager(context)

    }
}