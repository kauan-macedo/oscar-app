package com.seugrupo.oscarapp.network

import com.seugrupo.oscarapp.model.Diretor
import com.seugrupo.oscarapp.model.Filme
import retrofit2.Response
import retrofit2.http.GET

interface AcademiaApi {


    @GET("filme.json")
    suspend fun getFilmes(): Response<List<Filme>>


    @GET("diretor.json")
    suspend fun getDiretores(): Response<List<Diretor>>
}