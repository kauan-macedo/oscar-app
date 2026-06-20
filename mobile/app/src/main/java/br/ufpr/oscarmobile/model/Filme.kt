package com.ufpr.oscarmobile.model
 
import com.google.gson.annotations.SerializedName

data class Filme(
    @SerializedName("id")     val id: String,
    @SerializedName("nome")   val nome: String,
    @SerializedName("genero") val genero: String,
    @SerializedName("foto")   val foto: String
)