package ufc.smd.restclient

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView=findViewById(R.id.imageView)
        checandoARede()
    }



    fun baixarImagem(v: View){
        val bitmap = mLoad("https://static-wp-tor15-prd.torcedores.com/wp-content/uploads/2022/05/fortaleza-x-ceara-ao-vivo.png")
        imageView.setImageBitmap(bitmap)
    }
    fun baixarImagemComThread(v: View){

        Thread(Runnable {
            val bitmap= mLoad("https://static-wp-tor15-prd.torcedores.com/wp-content/uploads/2022/05/fortaleza-x-ceara-ao-vivo.png")!!
            imageView.setImageBitmap(bitmap)
        }).start()

    }

    fun baixarImagemComThread_Post(v: View) {
        Thread(Runnable {
            // a potentially time consuming task
            val bitmap = mLoad("https://a-static.mlcdn.com.br/450x450/camisa-ceara-oficial-01-2021-tam-p-vozao/lojacearamormetro/330ea6d2d36b11eb9bb44201ac18500e/d120ba42d8555bf2a9a10b49874187c9.jpeg")
            imageView.post {
                Log.v("pdm","baixou a imagem")
                imageView.setImageBitmap(bitmap)
            }
        }).start()
    }

    // Function to establish connection and load image
    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpsURLConnection?
        try {
            connection = url.openConnection() as HttpsURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.v("PDM", "Erro de comunicação: "+e.message)
            //Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }
    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null
        }

    }
    @SuppressLint("SuspiciousIndentation")
    fun baixarImagemCo(v: View)= runBlocking{
        launch {
            val bitmap =
                mLoadCoDis("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwqFjszAGVFkNzspsclJ2nAAjW6gMjmstF0g&s")
                when(bitmap) {
                    is  Bitmap -> {
                        Log.v("pdm", "baixou a imagem pra valer")
                        imageView. setImageBitmap (bitmap)
                    }
                    else ->  Log.v("pdm", "deu pau imagem")

                }
            Log.v("PDM","Tô aqui")
        }
    }

    fun baixarImagemCoDis(v: View)= runBlocking{
        launch (Dispatchers.IO) {
            val bitmap =
                mLoadCo("https://static-wp-tor15-prd.torcedores.com/wp-content/uploads/2022/05/fortaleza-x-ceara-ao-vivo.png")
            imageView.post {
                Log.v("pdm","baixou a imagem")
                imageView.setImageBitmap(bitmap)
            }
            Log.v("PDM","Tô aqui")
        }
    }

    suspend fun mLoadCoDis(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpsURLConnection?
        return withContext(Dispatchers.IO) {
            // Blocking network request code
            try {
                connection = url.openConnection() as HttpsURLConnection
                connection.connect()
                val inputStream: InputStream = connection.inputStream
                val bufferedInputStream = BufferedInputStream(inputStream)
                BitmapFactory.decodeStream(bufferedInputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.v("PDM", "Erro de comunicação: "+e.message)
              //  Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                null
            }
        }
    }

    suspend fun mLoadCo(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpsURLConnection?
        try {
            connection = url.openConnection() as HttpsURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            //Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }
    fun checandoARede(){
        val connManager: ConnectivityManager
        connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connManager.activeNetwork
        val networkInfo: NetworkCapabilities?
        networkInfo = connManager.getNetworkCapabilities(activeNetwork)
        if (networkInfo != null) {
            //"Pagamento de dados extra"
            Log.v("PDM", "Paga? ="+
                networkInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED).toString())
            //"Tem acesso à Internet"
            Log.v("PDM", "Tem Internet? ="+
                networkInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).toString())
            //"Envia MMS"
            Log.v("PDM", "Envia MMS? ="+
                networkInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_MMS).toString())
            //"Tem acesso a WIFI"
            Log.v("PDM", "É WIFI? ="+
                networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI).toString())

        }
    }
}