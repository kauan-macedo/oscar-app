package br.ufpr.oscarmobile.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.ufpr.oscarmobile.databinding.ActivityBoasVindasBinding
import br.ufpr.oscarmobile.model.VotoRequest
import br.ufpr.oscarmobile.network.RetrofitCentral
import br.ufpr.oscarmobile.session.SessionManager
import kotlinx.coroutines.launch

class BoasVindasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoasVindasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoasVindasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exibirDadosDaSessao()
        configurarBotoes()
    }

    private fun exibirDadosDaSessao() {
        binding.tvToken.text = "Token de votação: ${SessionManager.token}"
    }

    private fun configurarBotoes() {
        binding.btnVotarFilme.setOnClickListener {
            startActivity(Intent(this, FilmesActivity::class.java))
        }

        binding.btnVotarDiretor.setOnClickListener {
            startActivity(Intent(this, DiretoresActivity::class.java))
        }

        binding.btnConfirmarVoto.setOnClickListener {
            confirmarVoto()
        }

        binding.btnSair.setOnClickListener {
            SessionManager.limpar()
            // Volta ao login limpando a pilha de Activities
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun confirmarVoto() {
        val filme = SessionManager.filmeVotado
        val diretor = SessionManager.diretorVotado

        if (filme == null || diretor == null) {
            Toast.makeText(this, "Escolha um filme e um diretor antes de confirmar.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val resposta = RetrofitCentral.instance.registrarVoto(
                    VotoRequest(
                        login = SessionManager.loginUsuario,
                        token = SessionManager.token,
                        idFilme = filme.id,
                        idDiretor = diretor.id
                    )
                )

                if (resposta.isSuccessful && resposta.body()?.sucesso == true) {
                    SessionManager.votoConfirmado = true
                    Toast.makeText(this@BoasVindasActivity, "Voto confirmado.", Toast.LENGTH_SHORT).show()
                } else {
                    val mensagem = resposta.body()?.mensagem ?: "Nao foi possivel confirmar o voto."
                    Toast.makeText(this@BoasVindasActivity, mensagem, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@BoasVindasActivity, "Erro ao confirmar voto.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
