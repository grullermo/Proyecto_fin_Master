package com.proyectoFM.helloqr

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proyectoFM.helloqr.databinding.ActivityGenerarQrPlantaBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.glxn.qrgen.android.QRCode
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class ActivityGenerarQrPlanta : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityGenerarQrPlantaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGenerarQrPlantaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.encontrarPlanta.setOnQueryTextListener(this)

    }
    /*
    Este es el primero de los métodos del buscador, nos avisará de cara carácter que se añada al buscador, pero nosotros no necesitamos eso, solo necesitamos que nos avise cuando el usuario haya terminado de escribir así que lo dejamos como está y no hacemos nada.
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    /*
    El siguiente método será bastante similar pero será llamado cuando el usuario pulse en enter al terminar de buscar y ahí es cuando tendremos el texto que hemos escrito y se lo pasaremos a retrofit para que haga la petición a internet.
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            searchByName(query.lowercase())
        }
        return true
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("ip:port")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun searchByName(query:String){
        CoroutineScope(Dispatchers.IO).launch {

            val numEntero: Int = query.toInt()
            val call = getRetrofit().create(APIService::class.java).getPlantas(numEntero)

            val puppies = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                   var res_cod_planta = puppies?.get(0)?.cod_planta

                   var res_nombre = puppies?.get(0)?.nombre

                    var urlWebPlanta = "url =" + res_cod_planta
                    val myBitmap: Bitmap = QRCode.from(urlWebPlanta).bitmap()
                    binding.imageQrPlanta.setImageBitmap(myBitmap)
                } else {
                    showError()
                }
            }
        }
    }
    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

}




