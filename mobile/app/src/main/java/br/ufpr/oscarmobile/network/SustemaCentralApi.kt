package com.ufpr.oscarmobile.network

import com.seugrupo.oscarapp.model.ApiResponse
import com.seugrupo.oscarapp.model.LoginRequest
import com.seugrupo.oscarapp.model.LoginResponse
import com.seugrupo.oscarapp.model.VotoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface SistemaCentralApi {

    @POST("login")
    suspend fun autenticar(@Body request: LoginRequest): Response<LoginResponse>

    @POST("voto")
    suspend fun registrarVoto(@Body request: VotoRequest): Response<ApiResponse>
}