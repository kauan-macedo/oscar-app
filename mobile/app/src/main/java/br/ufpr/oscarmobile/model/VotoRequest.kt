package br.ufpr.oscarmobile.model
 
import com.google.gson.annotations.SerializedName

data class VotoRequest(
    @SerializedName("token")    val token: Int,
    @SerializedName("filmeId")  val filmeId: Long,
    @SerializedName("diretorId") val diretorId: Long
)
 
