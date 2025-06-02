package com.example.schedulingapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedulingapplication.ui.theme.SchedulingApplicationTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchedulingApplicationPreview()
        }
    }
}

// ==== banco de dados =====

val db = Firebase.firestore

// =========================

@Composable
fun App(){
    // conteúdo do banco de dados
    var name by remember{ mutableStateOf("") }
    var adress by remember{ mutableStateOf("") }
    var neighborhood by remember{ mutableStateOf("") }
    var CEP by remember{ mutableStateOf("") }
    var city by remember{ mutableStateOf("") }
    var state by remember{ mutableStateOf("") }

    val user = hashMapOf(
        "name" to name,
        "adress" to adress,
        "neighborhood" to neighborhood,
        "CEP" to CEP,
        "city" to city,
        "state" to state
    )

    // aparencia da página
    Column(Modifier.fillMaxSize()) {
        Row( modifier = Modifier.fillMaxWidth().padding(0.dp, 24.dp),
            Arrangement.Center,
        ){
            Text(
                text = "App Agendamento",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row( modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") }
            )
        }
        Row( modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            OutlinedTextField(
                value = adress,
                onValueChange = { adress = it },
                label = { Text("Endereço") }
            )
        }
        Row( modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            OutlinedTextField(
                value = neighborhood,
                onValueChange = { neighborhood = it },
                label = { Text("Bairro") }
            )
        }
        Row( modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            OutlinedTextField(
                value = CEP,
                onValueChange = { CEP = it },
                label = { Text("CEP") }
            )
        }
        Row( modifier = Modifier.fillMaxWidth(),
            Arrangement.Center
        ){
            OutlinedTextField(
                value = state,
                onValueChange = { state = it },
                label = { Text("Estado") }
            )
        }

        Row( modifier = Modifier.fillMaxWidth()
            .padding(0.dp, 18.dp),
            Arrangement.Center
        ){
            Column( Modifier.fillMaxWidth(0.35f)) {
                Row( modifier = Modifier.padding(10.dp, 0.dp)) {
                    Button(
                        onClick = {
                            // Add a new document with a generated ID
                            db.collection("users")
                                .add(user)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                } },
                    ) {
                        Text(
                            text = "Salvar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            // select do banco de dados
                            db.collection("user")
                                .get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        Log.d(TAG, "${document.id} => ${document.data}")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "Error getting documents: ", exception)
                                } },
                    ) {
                        Text(
                            text = "Listar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SchedulingApplicationPreview(){
    SchedulingApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            App()
        }
    }
}