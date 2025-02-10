package ufc.smd.restclient

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ufc.smd.restclient.ui.theme.RestClientTheme
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


class DownloadImage_Compose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestClientTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {

   /*  //Salvando o Bitmap para ficar imune a reconfigurações
    var bitmap by rememberSaveable { mutableStateOf<Bitmap?>(null)}
    */

    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null)}
    val coroutineScope = rememberCoroutineScope() // Criamos um escopo de corrotina
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(bitmap==null){
            Image(
                painter = painterResource(id = android.R.drawable.star_on),
                contentDescription = "Placeholder Image",
                modifier = Modifier.size(100.dp)
            )
        }else {
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(), //colocando o valor default da Imagem
                    contentDescription = "Imagem de exemplo",
                    modifier = Modifier.size(400.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = {
            coroutineScope.launch {
                bitmap = mLoadCompose()
            }
            }) {
            Text(text = "Baixar imagem")
        }
    }
}

suspend fun mLoadCompose(): Bitmap? {
    val url: URL =
        mStringToURL("https://a-static.mlcdn.com.br/450x450/camisa-ceara-oficial-01-2021-tam-p-vozao/lojacearamormetro/330ea6d2d36b11eb9bb44201ac18500e/d120ba42d8555bf2a9a10b49874187c9.jpeg")!!
    val connection: HttpsURLConnection?
    return withContext(Dispatchers.IO) {
        // Blocking network request code
        try {
            connection = url.openConnection() as HttpsURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            BitmapFactory.decodeStream(bufferedInputStream) //Esse é o valor retornado
        } catch (e: IOException) {
            e.printStackTrace()
            Log.v("PDM", "Erro de comunicação: " + e.message)
            null //Esse é  valor retornado
        }
    }

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