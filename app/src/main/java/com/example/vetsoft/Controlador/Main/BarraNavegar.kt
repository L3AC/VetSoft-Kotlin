package com.example.vetsoft.Controlador.Main

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.vetsoft.R
import com.example.vetsoft.databinding.ActivityBarraNavegarBinding

@Suppress("UNREACHABLE_CODE")
class BarraNavegar : AppCompatActivity() {
    private var idUs: Int = 0
    private var idCl:Int=0

    private lateinit var binding: ActivityBarraNavegarBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val extras = intent.extras
        idUs= extras?.getInt("idUs")!!
        idCl = extras?.getInt("idCl")!!


        binding = ActivityBarraNavegarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_barra_navegar)

        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.houseCliente, R.id.doctMain, R.id.tiendaMain, R.id.perfilMain
            )
        )*/

        val bundle = Bundle().apply {
            putInt("idUs", idUs)
            putInt("idCl", idCl)
        }
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.navigate(R.id.houseCliente,bundle)

        navView.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId, bundle)
            true
        }
    }
}