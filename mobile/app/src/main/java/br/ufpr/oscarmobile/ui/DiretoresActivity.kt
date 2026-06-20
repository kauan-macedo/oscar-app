package br.ufpr.oscarmobile.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.ufpr.oscarmobile.R
import br.ufpr.oscarmobile.model.Diretor
import br.ufpr.oscarmobile.network.RetrofitJsonExterno
import br.ufpr.oscarmobile.session.SessionManager
import kotlinx.coroutines.launch

class DiretoresActivity : AppCompatActivity() {

    private lateinit var radioGroupDiretores: RadioGroup
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSalvarLocal: Button
    
    private var listaDiretores: List<Diretor> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diretores)

        radioGroupDiretores = findViewById(R.id.radioGroupDiretores)
        progressBar = findViewById(R.id.progressBarDiretor)
        btnSalvarLocal = findViewById(R.id.btnSalvarDiretorLocal)

        buscarDiretores()

        btnSalvarLocal.setOnClickListener {
            val checkedId = radioGroupDiretores.checkedRadioButtonId

            if (checkedId == -1) {
                Toast.makeText(this, "Selecione um diretor para votar!", Toast.LENGTH_SHORT).show()
            } else {
                val selecionado = listaDiretores.find { it.id.toInt() == checkedId }
                
                if (selecionado != null) {
                    SessionManager.diretorVotado = selecionado
                    
                    Toast.makeText(this, "Escolha (${selecionado.nome}) gravada localmente!", Toast.LENGTH_SHORT).show()
                    finish() // Retorna à BoasVindasActivity
                }
            }
        }
    }

    private fun buscarDiretores() {
        progressBar.visibility = View.VISIBLE
        btnSalvarLocal.isEnabled = false

        lifecycleScope.launch {
            try {
                val resposta = RetrofitJsonExterno.instance.getDiretores()

                if (resposta.isSuccessful && !resposta.body().isNullOrEmpty()) {
                    listaDiretores = resposta.body()!!
                    renderizarDiretores(listaDiretores)
                } else {
                    Toast.makeText(this@DiretoresActivity, "Nenhum diretor encontrado.", Toast.LENGTH_LONG).show()
                }
                
            } catch (e: Exception) {
                Toast.makeText(this@DiretoresActivity, "Falha ao carregar diretores.", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
                btnSalvarLocal.isEnabled = true
            }
        }
    }

    private fun renderizarDiretores(diretores: List<Diretor>) {
        radioGroupDiretores.removeAllViews()

        for (diretor in diretores) {
            val rb = RadioButton(this).apply {
                id = diretor.id.toInt() // Facilita o mapeamento no clique
                text = diretor.nome
                textSize = 18f
                setPadding(16, 24, 16, 24)
            }
            radioGroupDiretores.addView(rb)
        }

        SessionManager.diretorVotado?.let { anterior ->
            radioGroupDiretores.check(anterior.id.toInt())
        }
    }
}
