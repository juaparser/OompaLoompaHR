package com.example.oompaloompahr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.oompaloompahr.databinding.MainActivityBinding
import com.example.oompaloompahr.ui.main.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Actividad principal de la aplicación.
 * Aquí se inicializa la navegación de los fragmentos.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Creando el binding para la vista
         */
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Desactivando modo noche para tener un único tema
         */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /**
         * Estableciendo la navegación entre fragmentos
         */

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_main,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    /**
     * Estableciendo acción al botón back del menú superior
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }
}