package com.example.vetsoft.Controlador.ui.Perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.vetsoft.Controlador.Cryptation.Crypto
import com.example.vetsoft.Controlador.Recuperacion.btnConfirmCC
import com.example.vetsoft.Controlador.Recuperacion.btnMirarCC
import com.example.vetsoft.Controlador.Recuperacion.btnVerifCC
import com.example.vetsoft.Controlador.Recuperacion.btnVolverCC
import com.example.vetsoft.Controlador.Recuperacion.txtContra1CC
import com.example.vetsoft.Controlador.Recuperacion.txtContra2CC
import com.example.vetsoft.Controlador.Recuperacion.txtContraCC
import com.example.vetsoft.Controlador.Recuperacion.txvAdvCC
import com.example.vetsoft.Controlador.validation.Validat
import com.example.vetsoft.Modelo.conx
import com.example.vetsoft.R
lateinit var btnVolverCP2: ImageButton
lateinit var txtContraCP2: EditText
lateinit var txtContra1CP2: EditText
lateinit var txtContra2CP2: EditText
lateinit var btnVerifCP2: Button
lateinit var btnConfirmCP2: Button
lateinit var txvAdvCP2: TextView
lateinit var btnMirarCP2: ImageButton
class ChangePassw : Fragment() {
    private var idUs: Int = 0
    private var idCl: Int = 0
    private var pasw=""
    private var conx = conx()
    private var crypt= Crypto()
    private var vali = Validat()
    private var contraVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs= arguments?.getInt("idUs")!!
            idCl = arguments?.getInt("idCl")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_passw, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVolverCP2 =requireView().findViewById(R.id.btnVolverCP2)
        btnVerifCP2 =requireView().findViewById(R.id.btnVerifCP2)
        btnConfirmCP2=requireView().findViewById(R.id.btnConfirmCP2)
        txtContraCP2 =requireView().findViewById(R.id.txtContraCP2)
        txtContra1CP2 =requireView().findViewById(R.id.txtContra1CP2)
        txtContra2CP2 =requireView().findViewById(R.id.txtContra2CP2)
        txvAdvCP2 =requireView().findViewById(R.id.txvAdvCP2)
        btnMirarCP2 =requireView().findViewById(R.id.btnMirarCP2)
        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        btnVolverCP2.setOnClickListener(){

        }
    }


}