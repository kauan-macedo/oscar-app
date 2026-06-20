package br.ufpr.oscarmobile.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.ufpr.oscarmobile.R
import br.ufpr.oscarmobile.model.Diretor
import br.ufpr.oscarmobile.network.RetrofitJsonExterno
import br.ufpr.oscarmobile.session.SessionManager
import kotlinx.coroutines.launch

class VotarDiretorActivity : AppCompatActivity() {

    private lateinit var radioGroupDiretores: RadioGroup
    private lateinit var progressBar: ProgressBar
    private lateinit var btnConfirmar: Button
    
    private var listaDiretores: List<Diretor> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_votar_diretor)

        radioGroupDiretores = findViewById(R.id.radioGroupDiretores)
        progressBar = findViewById(R.id.progressBarDiretor)
        btnConfirmar = findViewById(R.id.btnConfirmarDiretorLocal)

        carregarDiretores()

        btnConfirmar.setOnClickListener {
            val checkedId = radioGroupDiretores.checkedRadioButtonId

            if (checkedId == -1) {
                // Critério de aceite: Usuário não consegue confirmar sem selecionar
                Toast.makeText(this, "Por favor, selecione um diretor!", Toast.LENGTH_SHORT).show()
            } else {
                val diretorSelecionado = listaDiretores.find { it.id.toInt() == checkedId }

                if (diretorSelecionado != null) {
                    SessionManager.diretorVotado = diretorSelecionado
                    
                    Toast.makeText(this, "Voto em ${diretorSelecionado.nome} salvo!", Toast.LENGTH_SHORT).show()
                    finish() 
                }
            }
        }
    }

    private fun carregarDiretores() {
        progressBar.visibility = View.VISIBLE
        btnConfirmar.isEnabled = false

        lifecycleScope.launch {
            try {
                val resposta = RetrofitJsonExterno.instance.getDiretores()

                if (resposta.isSuccessful && !resposta.body().isNullOrEmpty()) {
                    listaDiretores = resposta.body()!!
                    popularRadioGroup(listaDiretores)
                } else {
                    Toast.makeText(this@VotarDiretorActivity, "Nenhum diretor encontrado.", Toast.LENGTH_LONG).show()
                }
                
            } catch (e: Exception) {
                Toast.makeText(this@VotarDiretorActivity, "Erro ao carregar diretores.", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
                btnConfirmar.isEnabled = true
            }
        }
    }

    private fun popularRadioGroup(diretores: List<Diretor>) {
        radioGroupDiretores.removeAllViews() // Garante que a view está limpa

        for (diretor in diretores) {
            val radioButton = RadioButton(this).apply {
                // O id do RadioButton vira o ID numérico do diretor
                id = diretor.id.toInt() 
                text = diretor.nome
                textSize = 18f
                setPadding(16, 16, 16, 16)
            }
            
            radioGroupDiretores.addView(radioButton)
        }

        SessionManager.diretorVotado?.let { diretorSalvo ->
            radioGroupDiretores.check(diretorSalvo.id.toInt())
        }
    }
}
