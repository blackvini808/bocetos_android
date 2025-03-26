package com.example.clone_fulanito.API

import com.example.clone_fulanito.modelos.Publicacion
import retrofit2.http.GET

interface FulanitoAPIServicio{
    // Cuando use la respuesta de internet se resolvera como: https://jsonplaceholder.typicode.com/posts
    @GET("/posts")
    suspend fun obtener_publicaciones(): List<Publicacion>
}