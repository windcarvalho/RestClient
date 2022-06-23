package ufc.smd.restclient

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CEPActivity : AppCompatActivity() {
    lateinit var edCEP:EditText
    lateinit var edAd:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cep)
        edCEP = findViewById<EditText>(R.id.editTextCEP)
        edAd = findViewById<EditText>(R.id.editTextAddress)
    }


    fun onClickCEPJSON (v: View)= runBlocking{
        val cep: String = edCEP.text.toString()
        Log.v("PDM", "CEP digitado:"+cep)
        launch (Dispatchers.IO)  {
            val retorno =mLoad("https://viacep.com.br/ws/" + cep + "/json/")

            Log.v("PDM", "o serviço respondeu")
            //Log.v("pdm","retorno: "+ (retorno?.readText() ?:"vazio" ))

            if(retorno!=null){
                val jsonObject = JSONObject(retorno.readText())
                val logra=jsonObject.getString("logradouro")
                Log.v("PDM","Endereço: "+logra);
            }


        }
        Log.v("PDM", "serviço requisitado")
    }


    fun onClickCEP (v: View)= runBlocking{
        val cep: String = edCEP.text.toString()
        Log.v("PDM", "CEP digitado:"+cep)
        launch (Dispatchers.IO)  {
            val retorno =mLoad("https://viacep.com.br/ws/" + cep + "/json/")
            Log.v("PDM", "o serviço respondeu")
            Log.v("pdm","retorno: "+ (retorno?.readText() ?:"vazio" ))

        }
        Log.v("PDM", "serviço requisitado")
    }

    suspend fun mLoad(string: String): BufferedReader? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpsURLConnection?
        try {
            connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod= "GET"
            connection.connectTimeout= 20000
            connection.connect()

            Log.v("PDM", "Response Code: "+connection.responseCode)
            Log.v("PDM", "Response: "+connection.responseMessage)

            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return bufferedInputStream.bufferedReader(Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.v("PDM", "Erro de comunicação: "+e.message)


        }
        return null
    }
    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            Log.v("PDM", "Erro de formatação da URL: "+e.message)
        }
        return null
    }
}