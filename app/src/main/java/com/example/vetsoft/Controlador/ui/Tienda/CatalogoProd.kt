package com.example.vetsoft.Controlador.ui.Tienda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.vetsoft.Controlador.ui.Home.btnVolverCP
import com.example.vetsoft.Controlador.ui.Home.filaCP
import com.example.vetsoft.Controlador.ui.Home.spBusqCP
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R

class CatalogoProd : Fragment() {
    lateinit var btnVolverCat:Button
    lateinit var txtNProd:EditText
    lateinit var rcCataProd:RecyclerView
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var conx = conx()

    class filaCP(
        val id: Int, val nTipoProd: String
    )
    val regCP = mutableListOf<com.example.vetsoft.Controlador.ui.Home.filaCP>()
    val myDataCP = mutableListOf<String>()

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
        return inflater.inflate(R.layout.fragment_catalogo_prod, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverCat = requireView().findViewById(R.id.btnVolverCat)
        txtNProd = requireView().findViewById(R.id.txtNProd)
        rcCataProd = requireView().findViewById(R.id.rcCataProd)
    }


}