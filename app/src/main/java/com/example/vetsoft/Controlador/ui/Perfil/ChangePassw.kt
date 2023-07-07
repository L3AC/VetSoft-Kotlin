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
    private var pasw=""
    private var conx = conx()
    private var crypt= Crypto()
    private var vali = Validat()
    private var contraVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUs= arguments?.getInt("idUs")!!
            pasw = arguments?.getString("pasw")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_passw, container, false)
    }


}