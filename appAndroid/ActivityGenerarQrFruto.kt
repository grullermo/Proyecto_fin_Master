package com.proyectoFM.helloqr

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.proyectoFM.helloqr.databinding.ActivityGenerarQrFrutoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.glxn.qrgen.android.QRCode
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class ActivityGenerarQrFruto : AppCompatActivity() {
    private lateinit var binding: ActivityGenerarQrFrutoBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_qr_fruto)
        binding = ActivityGenerarQrFrutoBinding.inflate(layoutInflater)
        val boton1 = findViewById<Button>(R.id.buttonPeso)
        val boton2 = findViewById<Button>(R.id.buttonGenerarQr)
        val boton3 = findViewById<Button>(R.id.buttonsalir)
        val TextoFruto = findViewById<TextView>(R.id.txFruto)
        val TextoPeso = findViewById<TextView>(R.id.txPeso)
        val TextoNombre = findViewById<TextView>(R.id.txvNombre)

        val DatoPeso = findViewById<EditText>(R.id.editTextPeso)
        val VerQr = findViewById<ImageView>(R.id.imageView2)

        val bundle = intent.extras

        val dato = bundle?.getString("codplantaurl");

        val chardato = dato.toString();

        val index = chardato.indexOf('=')

        val domain: String? = if (index == -1) null else chardato.substring(index + 1)

        val cosa = domain!!.toInt()


        val casilla1 = findViewById<TextView>(R.id.txvCodPlanta)

        casilla1.text = domain.toString();

        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getPlantas(cosa)

            val puppies = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                    var res_cod_planta = puppies?.get(0)?.cod_planta
                    var res_nombre = puppies?.get(0)?.nombre

                    val casilla2 = findViewById<TextView>(R.id.txvNombre)
                    casilla2.text = res_nombre;

                } else {
                    showError()
                }
            }

        }


        boton3.setOnClickListener {
            finish();
        }

        boton1.setOnClickListener {
            val tag = Server::class.java.name

            CoroutineScope(Dispatchers.IO).launch {


                val dPeso = DatoPeso.text
                val SPeso = dPeso.toString()


                val sdf = SimpleDateFormat("yyyy/M/dd")
                val currentDate = sdf.format(Date())

                val descInfo = "La ostia"

                val (cod_planta, descripcion, fecha_recoleccion, peso) = Server.post(
                    cosa,
                    descInfo,
                    currentDate,
                    SPeso.toDouble()
                )


                Log.d(tag, "Thread is ${Thread.currentThread().name}")


                Handler(Looper.getMainLooper()).post(Runnable() {
                TextoFruto.text = descInfo
                TextoPeso.text = peso.toString()

                })
            }


        }

        boton2.setOnClickListener{

            val view: View? = this.currentFocus
            view?.clearFocus()
            if (view != null) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }

            var StringQr = chardato + ";" + TextoNombre.text + ";" + TextoFruto.text + ";" + TextoPeso.text;
            println("sirpask: " + StringQr)
            val myBitmap: Bitmap = QRCode.from(StringQr).bitmap()
            VerQr.setImageBitmap(myBitmap)


        }


    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://ip:port/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    data class NTuple2<T1, T2>(val t1: T1, val t2: T2)

    data class NTuple3<T1, T2, T3>(val t1: T1, val t2: T2, val t3: T3)

    data class NTuple4<T1, T2, T3, T4>(val t1: T1, val t2: T2, val t3: T3, val t4: T4)

    data class NTuple5<T1, T2, T3, T4, T5>(
        val t1: T1,
        val t2: T2,
        val t3: T3,
        val t4: T4,
        val t5: T5
    )

    data class NTuple6<T1, T2, T3, T4, T5, T6>(
        val t1: T1,
        val t2: T2,
        val t3: T3,
        val t4: T4,
        val t5: T5,
        val t6: T6
    )

    infix fun <T1, T2> T1.then(t2: T2): NTuple2<T1, T2> {
        return NTuple2(this, t2)
    }

    infix fun <T1, T2, T3> NTuple2<T1, T2>.then(t3: T3): NTuple3<T1, T2, T3> {
        return NTuple3(this.t1, this.t2, t3)
    }

    infix fun <T1, T2, T3, T4> NTuple3<T1, T2, T3>.then(t4: T4): NTuple4<T1, T2, T3, T4> {
        return NTuple4(this.t1, this.t2, this.t3, t4)
    }

    infix fun <T1, T2, T3, T4, T5> NTuple4<T1, T2, T3, T4>.then(t5: T5): NTuple5<T1, T2, T3, T4, T5> {
        return NTuple5(this.t1, this.t2, this.t3, this.t4, t5)
    }

    infix fun <T1, T2, T3, T4, T5, T6> NTuple5<T1, T2, T3, T4, T5>.then(t6: T6): NTuple6<T1, T2, T3, T4, T5, T6> {
        return NTuple6(this.t1, this.t2, this.t3, this.t4, this.t5, t6)
    }

    object Server {
        private const val URL = "IP:port"
        private val service: Service

        init {
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            service = retrofit.create(Service::class.java)
        }

        private val tag = Server::class.java.name
        suspend fun post(
            cod_planta: Int,
            descripcion: String,
            fecha_recoleccion: String,
            peso: Double
        ): NTuple4<Int, String, String, Double> = withContext(Dispatchers.IO) {

            Log.d(tag, "Thread is ${Thread.currentThread().name}")

            val request = Service.PostRequest(cod_planta, descripcion, fecha_recoleccion, peso)



            val response = service.post(request)

            if (response.isSuccessful) {
                val body = response.body()!!.data

                return@withContext NTuple4(
                    body.cod_planta,
                    body.descripcion,
                    body.fecha_recoleccion,
                    body.peso
                )
            }  else {
                throw Exception(response.errorBody()?.charStream()?.readText())

            }


        }


    }
    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

}

