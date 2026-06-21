package br.ufpr.oscarmobile.session

import br.ufpr.oscarmobile.model.Diretor
import br.ufpr.oscarmobile.model.Filme

object SessionManager {


    var loginUsuario: String = ""
    var token: Int = -1

    // Escolhas mantidas apenas na sessao ate o envio definitivo pelo REST.
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
