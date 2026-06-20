package br.ufpr.oscarmobile.network

import br.ufpr.oscarmobile.model.Diretor
import br.ufpr.oscarmobile.model.Filme
import retrofit2.Response
import retrofit2.http.GET

interface AcademiaApi {


    @GET("filme.json")
    suspend fun getFilmes(): Response<List<Filme>>


    @GET("diretor.json")
    suspend fun getDiretores(): Response<List<Diretor>>
}
