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
                    AppSession.diretorSelecionado = diretorSelecionado
                    
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
                // Chame o seu cliente Retrofit configurado para a API externa na Issue 1
                val response = RetrofitClientExternal.diretorService.getDiretores()
                listaDiretores = response
                
                popularRadioGroup(listaDiretores)
                
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
                textSize = 18sp
                setPadding(16, 16, 16, 16)
            }
            
            radioGroupDiretores.addView(radioButton)
        }

        AppSession.diretorSelecionado?.let { diretorSalvo ->
            radioGroupDiretores.check(diretorSalvo.id.toInt())
        }
    }
}