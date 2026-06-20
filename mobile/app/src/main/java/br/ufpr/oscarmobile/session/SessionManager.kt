package com.seugrupo.oscarapp.session

import com.seugrupo.oscarapp.model.Diretor
import com.seugrupo.oscarapp.model.Filme

object SessionManager {


    var loginUsuario: String = ""
    var token: Int = -1

    var filmeVotado: Filme? = null
    var diretorVotado: Diretor? = null

    var votoConfirmado: Boolean = false

    fun prontoParaConfirmar(): Boolean =
        filmeVotado != null && diretorVotado != null

    fun estaLogado(): Boolean = loginUsuario.isNotEmpty() && token >= 0


    fun limpar() {
        loginUsuario = ""
        token = -1
        filmeVotado = null
        diretorVotado = null
        votoConfirmado = false
    }
}