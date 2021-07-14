package com.proyectoFM.helloqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import com.proyectoFM.helloqr.databinding.ActivityDatosPlantaBinding
import com.proyectoFM.helloqr.databinding.ActivityMainBinding

class ActivityDatosPlanta : AppCompatActivity() {
    private lateinit var binding: ActivityDatosPlantaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_planta)
        binding = ActivityDatosPlantaBinding.inflate(layoutInflater)

        val bundle = intent.extras
        val dato = bundle?.getString("direccion")

        val webView1=findViewById<WebView>(R.id.webView1)
        val boton1=findViewById<Button>(R.id.button)
        val boton2=findViewById<Button>(R.id.button2)


        val chardato = dato.toString();
        val pathWeb = webView1.loadUrl("${dato}").toString()

        val index = chardato.indexOf('=')

        val domain: String? = if (index == -1) null else chardato.substring(index + 1)


        webView1.loadUrl("${dato}")
        boton1.setOnClickListener {
            finish();
        }
        boton2.setOnClickListener {
            val intento1 = Intent(this, ActivityGenerarQrFruto::class.java)
            intento1.putExtra("codplantaurl", (chardato))
            startActivity(intento1)
        }
    }
}