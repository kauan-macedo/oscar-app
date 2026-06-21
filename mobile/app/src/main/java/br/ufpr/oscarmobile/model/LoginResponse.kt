package br.ufpr.oscarmobile.model
 
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("sucesso")         val sucesso: Boolean,
    @SerializedName("token")           val token: Int? = null,
    @SerializedName("mensagem")        val mensagem: String? = null,
    @SerializedName("votoConfirmado")  val votoConfirmado: Boolean = false,
    @SerializedName("filme")           val filme: Filme? = null,
    @SerializedName("diretor")         val diretor: Diretor? = null
)
