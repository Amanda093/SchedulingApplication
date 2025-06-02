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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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

// ==== banco de dados =====

val db = Firebase.firestore

class Usuario {
    var name = ""
    var adress = ""
    var neighborhood = ""
    var CEP = ""
    var city = ""
    var state = ""
}

// =========================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchedulingApplicationPreview()
        }
    }
}

@Composable
fun App(){
    // select
    var mostrarLista by remember { mutableStateOf(false) }
    val usuarios = remember { mutableStateListOf<Usuario>() }
    val usuariosRef = db.collection("users")

    usuariosRef.addSnapshotListener { snapshots, e ->
        if (e != null) {
            Log.w(TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        if (snapshots != null && !snapshots.isEmpty) {
            usuarios.clear()

            for (document in snapshots) {
                val usuario = Usuario().apply {
                    name = document.getString("name") ?: ""
                    adress = document.getString("adress") ?: ""
                    neighborhood = document.getString("neighborhood") ?: ""
                    CEP = document.getString("CEP") ?: ""
                    city = document.getString("city") ?: ""
                    state = document.getString("state") ?: ""
                }
                usuarios.add(usuario)
            }

            Log.d(TAG, "Usuários atualizados: ${usuarios.size}")
        } else {
            Log.d(TAG, "Nenhum dado encontrado.")
            usuarios.clear()
        }
    }

    // insert
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
            Column( Modifier.fillMaxWidth(0.35f),
                    Arrangement.Center) {
                Row {
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
                }
                Row {
                    Button(onClick = {
                        usuariosRef.get()
                            .addOnSuccessListener { snapshots ->
                                usuarios.clear()
                                for (document in snapshots) {
                                    val usuario = Usuario().apply {
                                        name = document.getString("name") ?: ""
                                        adress = document.getString("adress") ?: ""
                                        neighborhood = document.getString("neighborhood") ?: ""
                                        CEP = document.getString("CEP") ?: ""
                                        city = document.getString("city") ?: ""
                                        state = document.getString("state") ?: ""
                                    }
                                    usuarios.add(usuario)
                                }
                                mostrarLista = true
                                Log.d(TAG, "Usuários carregados: ${usuarios.size}")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Erro ao carregar usuários", e)
                            }
                    }) {
                        Text("Listar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Row {
                if (mostrarLista) {
                    LazyColumn {
                        items(usuarios) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)) {
                                Text("Nome: ${it.name}")
                                Text("Endereço: ${it.adress}")
                                Text("Bairro: ${it.neighborhood}")
                                Text("CEP: ${it.CEP}")
                                Text("Cidade: ${it.city}")
                                Text("Estado: ${it.state}")
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
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