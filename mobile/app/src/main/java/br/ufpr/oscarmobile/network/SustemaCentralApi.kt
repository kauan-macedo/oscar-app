package br.ufpr.oscarmobile.network

import br.ufpr.oscarmobile.model.ApiResponse
import br.ufpr.oscarmobile.model.LoginRequest
import br.ufpr.oscarmobile.model.LoginResponse
import br.ufpr.oscarmobile.model.VotoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface SistemaCentralApi {

    @POST("login")
    suspend fun autenticar(@Body request: LoginRequest): Response<LoginResponse>

    @POST("voto")
    suspend fun registrarVoto(@Body request: VotoRequest): Response<ApiResponse>
}
