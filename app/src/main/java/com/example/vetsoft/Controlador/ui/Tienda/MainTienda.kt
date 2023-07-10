package com.example.vetsoft.Controlador.ui.Tienda

import Domain.FoodDomain
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetsoft.R

class MainTienda : Fragment() {
    private var idUs: Int = 0
    private var idCl: Int = 0
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
        return inflater.inflate(R.layout.fragment_main_tienda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun initRecyclerview() {
        val items = ArrayList<FoodDomain>()
        items.add(
            FoodDomain(
                "Mesa en la Terraza",
                "Mesa con unas increibles vistas",
                "mesa_terraza",
                10.0,
                0,
                5.0
            )
        )
        items.add(
            FoodDomain(
                "Mesa en el salon",
                "Mesa dentro del salon con increible servicio",
                "mesa_salon",
                10.0,
                0,
                5.0
            )
        )
        items.add(
            FoodDomain(
                "Chocolate",
                "Pastel delicioso de chocolate",
                "pastel_chocolate",
                20.0,
                100,
                5.0
            )
        )
        items.add(FoodDomain("Fresa", "Pastel delicioso de fresa", "pastel_fresa", 15.0, 100, 5.0))
        items.add(
            FoodDomain(
                "Frutas",
                "Pastel delicioso de frutas",
                "pastel_frutas",
                25.0,
                100,
                5.0
            )
        )
        items.add(FoodDomain("Mango", "Pastel delicioso de mango", "mango", 22.0, 100, 5.0))
        items.add(
            FoodDomain(
                "Cheesecake",
                "Pastel delicioso de fresa",
                "cheesecake",
                10.0,
                100,
                5.0
            )
        )
        items.add(
            FoodDomain(
                "´Pastel de Nutella",
                "Pastel delicioso de frutas",
                "nutella",
                30.0,
                100,
                5.0
            )
        )
        recyclerViewFood = findViewById(R.id.view1)
        recyclerViewFood.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        adapterFoodList = FoodListAdapter(items)
        recyclerViewFood.setAdapter(adapterFoodList)
    }
}