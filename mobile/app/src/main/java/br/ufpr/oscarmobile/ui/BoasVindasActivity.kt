package br.ufpr.oscarmobile.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.ufpr.oscarmobile.databinding.ActivityBoasVindasBinding
import br.ufpr.oscarmobile.session.SessionManager

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
            startActivity(Intent(this, DiretorActivity::class.java))
        }

        binding.btnConfirmarVoto.setOnClickListener {
            startActivity(Intent(this, ConfirmarVotoActivity::class.java))
        }

        binding.btnSair.setOnClickListener {
            SessionManager.limpar()
            // Volta ao login limpando a pilha de Activities
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}