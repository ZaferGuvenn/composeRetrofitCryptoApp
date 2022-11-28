package com.example.composeretrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeretrofit.model.CryptoModel
import com.example.composeretrofit.service.CryptoAPI
import com.example.composeretrofit.ui.theme.ComposeRetrofitTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRetrofitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(){

    //url=https://raw.githubusercontent.com/
    //atilsamancioglu/K21-JSONDataSet/master/crypto.json

    val cryptoList=remember{ mutableStateListOf<CryptoModel>() }

    val retrofit= Retrofit.Builder().baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service= retrofit.create(CryptoAPI::class.java)
    val call=service.getData()

    call.enqueue(object : Callback<List<CryptoModel>>{
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {

            if (response.isSuccessful)
            {
                response.body()?.let {
                    cryptoList.addAll(it)
                    println(cryptoList)

                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }
    })

    Scaffold(topBar = {TopBar()}) {
        ShowCryptos(cryptos = cryptoList)
    }

}

@Composable
fun TopBar(){
    TopAppBar(contentPadding = PaddingValues(16.dp), backgroundColor = MaterialTheme.colors.secondary) {
        Text(text = "Compose Retrofit Uygulaması", style = MaterialTheme.typography.h5, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ShowCryptos(cryptos:List<CryptoModel>){



    LazyColumn(contentPadding = PaddingValues(4.dp)){
        items(cryptos){
            CryptoRow(it)
        }
    }
}

@Composable
fun CryptoRow(crypto:CryptoModel){
    Column(Modifier.fillMaxWidth().background(MaterialTheme.colors.primary).padding(15.dp)) {
        Text(text = crypto.currency, color = Color.White, style = MaterialTheme.typography.h5, modifier = Modifier.background(MaterialTheme.colors.primaryVariant).padding(15.dp))
        Text(text = crypto.price, color = Color.Black, style = MaterialTheme.typography.h6, modifier = Modifier.background(MaterialTheme.colors.onSurface).padding(15.dp).fillMaxWidth())
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeRetrofitTheme {
        MainScreen()
    }
}