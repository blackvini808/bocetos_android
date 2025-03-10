package com.example.scalfolding

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scalfolding.ui.theme.ScalfoldingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScalfoldingTheme {
                Aplicacion(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun prevista(){
    Aplicacion(modifier = Modifier.fillMaxSize())
}

@Composable
fun Aplicacion(modifier: Modifier){
    fun al_pulsar_carrito(){
        Log.v("Aplicacion", "Se ha pulsado el boton y utilizado la funcion al pulsar el carrito")
    }
    Scaffold( modifier = modifier,
        topBar = {
           BarraSuperior()
        },
        bottomBar = {
            BarraInferior()
        },
        floatingActionButton = {
            BotonFlotante(al_presionar = {
                al_pulsar_carrito()
                al_pulsar_carrito()
            })
        })
    { paddingInterior ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingInterior).background(Color.Cyan)){
            Text("MENSAJE OLA K ACE",
                modifier = Modifier
                .fillMaxHeight(0.1f)
                .background(color = Color.Blue)
                )
            Greeting("Esto es la columna")
            Greeting("Esto es la columna")
            Greeting("Esto es la columna")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(){
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
        Text("TITULO DE LA BARRA", modifier = Modifier)
    })
}

@Composable
fun BarraInferior(){
    fun boton_pulsado_inferior(){
        Log.v("Barra Inferior", "Se ha pulsado el boton de la barra inferior")
    }
    BottomAppBar{
        Row(verticalAlignment = Alignment.CenterVertically){
            Text("TITULO DE LA BARRA")
            Spacer(modifier = Modifier.width(15.dp))
            Icon(Icons.Rounded.ShoppingCart, contentDescription = " ")
            BotonFlotante(al_presionar = {boton_pulsado_inferior()})
        }
    }
}

@Composable
fun BotonFlotante(al_presionar: () -> Unit){
    FloatingActionButton(onClick = {al_presionar}){
        Icon(Icons.Rounded.ShoppingCart, contentDescription = " ")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}