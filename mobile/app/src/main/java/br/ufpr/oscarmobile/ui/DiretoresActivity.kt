package br.ufpr.oscarmobile.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
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
    private lateinit var tvMensagem: TextView
    
    private var listaDiretores: List<Diretor> = emptyList()
    private val diretoresPorViewId = mutableMapOf<Int, Diretor>()
    private val viewIdPorDiretorId = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diretores)

        radioGroupDiretores = findViewById(R.id.radioGroupDiretores)
        progressBar = findViewById(R.id.progressBarDiretor)
        btnSalvarLocal = findViewById(R.id.btnSalvarDiretorLocal)
        tvMensagem = findViewById(R.id.tvMensagemDiretores)

        buscarDiretores()

        btnSalvarLocal.setOnClickListener {
            if (SessionManager.votoConfirmado) {
                Toast.makeText(this, "Voto já confirmado. As escolhas não podem ser alteradas.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val checkedId = radioGroupDiretores.checkedRadioButtonId

            if (checkedId == -1) {
                Toast.makeText(this, "Selecione um diretor para votar!", Toast.LENGTH_SHORT).show()
            } else {
                val selecionado = diretoresPorViewId[checkedId]
                
                if (selecionado != null) {
                    SessionManager.diretorVotado = selecionado
                    
                    Toast.makeText(this, "Escolha (${selecionado.nome}) gravada localmente!", Toast.LENGTH_SHORT).show()
                    finish()
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
                    mostrarMensagem("Nenhum diretor foi retornado pelo servidor.")
                }
                
            } catch (e: Exception) {
                mostrarMensagem("Falha ao carregar diretores. Verifique a conexão.")
            } finally {
                progressBar.visibility = View.GONE
                btnSalvarLocal.isEnabled = !SessionManager.votoConfirmado && listaDiretores.isNotEmpty()
            }
        }
    }

    private fun renderizarDiretores(diretores: List<Diretor>) {
        radioGroupDiretores.removeAllViews()
        diretoresPorViewId.clear()
        viewIdPorDiretorId.clear()
        tvMensagem.visibility = View.GONE

        // A lista vem do JSON externo, entao a quantidade de RadioButtons e dinamica.
        for (diretor in diretores) {
            val viewId = View.generateViewId()
            diretoresPorViewId[viewId] = diretor
            viewIdPorDiretorId[diretor.id] = viewId

            val rb = RadioButton(this).apply {
                id = viewId
                text = diretor.nome
                textSize = 18f
                maxLines = 3
                ellipsize = TextUtils.TruncateAt.END
                isEnabled = !SessionManager.votoConfirmado
                setPadding(16, 24, 16, 24)
            }
            radioGroupDiretores.addView(rb)
        }

        SessionManager.diretorVotado?.let { anterior ->
            viewIdPorDiretorId[anterior.id]?.let { radioGroupDiretores.check(it) }
        }

        if (SessionManager.votoConfirmado) {
            btnSalvarLocal.text = "Voto confirmado"
        }
    }

    private fun mostrarMensagem(mensagem: String) {
        listaDiretores = emptyList()
        radioGroupDiretores.removeAllViews()
        diretoresPorViewId.clear()
        viewIdPorDiretorId.clear()
        tvMensagem.text = mensagem
        tvMensagem.visibility = View.VISIBLE
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show()
    }
}
