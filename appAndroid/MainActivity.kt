package com.proyectoFM.helloqr

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.proyectoFM.helloqr.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonLeerQR.setOnClickListener { initScanner() }
        binding.buttonCrearQR.setOnClickListener {
            val intento1 = Intent(this, ActivityGenerarQrPlanta::class.java)
            startActivity(intento1)
        }

        }
        //variablesYconstantes()




    private fun initScanner(){
        IntentIntegrator(this).initiateScan()
    }


    // Get the results:
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {

            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                var datos = result.contents;
                val intento2 = Intent(this, ActivityDatosPlanta::class.java)
                intento2.putExtra("direccion", datos)
                startActivity(intento2)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
