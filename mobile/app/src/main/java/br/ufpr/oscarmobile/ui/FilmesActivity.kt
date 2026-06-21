package br.ufpr.oscarmobile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.ufpr.oscarmobile.databinding.ActivityFilmesBinding
import br.ufpr.oscarmobile.network.RetrofitJsonExterno
import kotlinx.coroutines.launch

class FilmesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFilmes.layoutManager = LinearLayoutManager(this)

        carregarFilmes()
    }

    private fun carregarFilmes() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvErro.visibility      = View.GONE

        lifecycleScope.launch {
            try {
                val resposta = RetrofitJsonExterno.instance.getFilmes()

                if (resposta.isSuccessful && !resposta.body().isNullOrEmpty()) {
                    val filmes = resposta.body()!!
                    binding.rvFilmes.adapter = FilmeAdapter(filmes) { filme ->
                        // O voto ainda e local, o envio ao servidor acontece na confirmacao.
                        val intent = Intent(this@FilmesActivity, FilmeDetalheActivity::class.java).apply {
                            putExtra("id",     filme.id)
                            putExtra("nome",   filme.nome)
                            putExtra("genero", filme.genero)
                            putExtra("foto",   filme.foto)
                        }
                        startActivity(intent)
                    }
                } else {
                    mostrarErro("Nenhum filme encontrado.")
                }

            } catch (e: Exception) {
                mostrarErro("Erro ao carregar filmes. Verifique a conexão.")
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun mostrarErro(msg: String) {
        binding.tvErro.text       = msg
        binding.tvErro.visibility = View.VISIBLE
    }
}
