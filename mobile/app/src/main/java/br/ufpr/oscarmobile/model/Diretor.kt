package com.ufpr.oscarmobile.model
 
import com.google.gson.annotations.SerializedName

data class Diretor(
    @SerializedName("id")   val id: String,
    @SerializedName("nome") val nome: String
)