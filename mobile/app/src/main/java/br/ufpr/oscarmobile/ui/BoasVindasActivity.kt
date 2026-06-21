package br.ufpr.oscarmobile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    override fun onResume() {
        super.onResume()
        exibirDadosDaSessao()
    }

    private fun exibirDadosDaSessao() {
        binding.tvToken.text = "Token de votação: ${SessionManager.token}"

        val votoConfirmado = SessionManager.votoConfirmado
        // Depois da confirmacao final, o usuario so pode visualizar as escolhas.
        binding.btnVotarFilme.isEnabled = !votoConfirmado
        binding.btnVotarDiretor.isEnabled = !votoConfirmado
        binding.tvStatusVoto.visibility = if (votoConfirmado) View.VISIBLE else View.GONE
    }

    private fun configurarBotoes() {
        binding.btnVotarFilme.setOnClickListener {
            if (SessionManager.votoConfirmado) {
                mostrarVotoBloqueado()
                return@setOnClickListener
            }
            startActivity(Intent(this, FilmesActivity::class.java))
        }

        binding.btnVotarDiretor.setOnClickListener {
            if (SessionManager.votoConfirmado) {
                mostrarVotoBloqueado()
                return@setOnClickListener
            }
            startActivity(Intent(this, DiretoresActivity::class.java))
        }

        binding.btnConfirmarVoto.setOnClickListener {
            startActivity(Intent(this, ConfirmarVotoActivity::class.java))
        }

        binding.btnSair.setOnClickListener {
            SessionManager.limpar()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun mostrarVotoBloqueado() {
        Toast.makeText(this, "Voto já confirmado. As escolhas não podem ser alteradas.", Toast.LENGTH_LONG).show()
    }
}
