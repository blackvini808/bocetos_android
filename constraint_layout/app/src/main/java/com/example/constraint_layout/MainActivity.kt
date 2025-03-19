package com.example.constraint_layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.constraint_layout.ui.theme.Constraint_layoutTheme
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Constraint_layoutTheme {
                    AplicacionPrueba(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun prevista(){
    AplicacionPrueba(modifier = Modifier.fillMaxSize())
}

@Composable
fun AplicacionPrueba(modifier: Modifier){
    Column(modifier = Modifier.width(90.dp).height(150.dp)){
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .background(Color.DarkGray)
        ){
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(30.dp)
                    .background(Color.Red)
                    .offset(x = 35.dp, y = 0.dp)
            )
        }
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .background(Color.Cyan)
        ){
            Text("fila 2")
        }
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .background(Color.DarkGray)
        ){
            Text("fila 3")
        }
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .background(Color.Cyan)
        ){
            Text("fila 4")
        }
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .background(Color.DarkGray)
        ){
            Text("fila 5")
        }
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .background(Color.Cyan)
        ){
            Text("fila 6")
        }

    }
}