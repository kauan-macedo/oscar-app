package br.ufpr.oscarmobile.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.ufpr.oscarmobile.R
import br.ufpr.oscarmobile.databinding.ActivityFilmeDetalheBinding
import br.ufpr.oscarmobile.model.Filme
import br.ufpr.oscarmobile.session.SessionManager
import com.squareup.picasso.Picasso

class FilmeDetalheActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmeDetalheBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmeDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Reconstrói o Filme a partir dos extras do Intent
        val filme = Filme(
            id     = intent.getStringExtra("id")     ?: "",
            nome   = intent.getStringExtra("nome")   ?: "",
            genero = intent.getStringExtra("genero") ?: "",
            foto   = intent.getStringExtra("foto")   ?: ""
        )

        exibirDetalhe(filme)

        if (SessionManager.votoConfirmado) {
            binding.btnVotar.isEnabled = false
            binding.btnVotar.text = "Voto confirmado"
        }

        binding.btnVotar.setOnClickListener {
            if (SessionManager.votoConfirmado) {
                Toast.makeText(this, "Voto já confirmado. As escolhas não podem ser alteradas.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            SessionManager.filmeVotado = filme
            Toast.makeText(this, "Voto em \"${filme.nome}\" registrado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun exibirDetalhe(filme: Filme) {
        binding.tvNomeDetalhe.text   = filme.nome
        binding.tvGeneroDetalhe.text = filme.genero

        Picasso.get()
            .load(filme.foto)
            .placeholder(R.drawable.oscar_trofeu)
            .error(R.drawable.oscar_trofeu)
            .into(binding.ivPosterDetalhe)
    }
}
