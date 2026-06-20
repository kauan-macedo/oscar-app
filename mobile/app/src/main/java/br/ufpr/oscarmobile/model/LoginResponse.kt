package br.ufpr.oscarmobile.model
 
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("sucesso")  val sucesso: Boolean,
    @SerializedName("token")    val token: Int? = null,
    @SerializedName("mensagem") val mensagem: String? = null
)
