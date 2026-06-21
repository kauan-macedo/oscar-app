package br.ufpr.oscarmobile.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.ufpr.oscarmobile.databinding.ActivityConfirmarVotoBinding
import br.ufpr.oscarmobile.model.ApiResponse
import br.ufpr.oscarmobile.model.VotoRequest
import br.ufpr.oscarmobile.network.RetrofitCentral
import br.ufpr.oscarmobile.session.SessionManager
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class ConfirmarVotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmarVotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmarVotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        atualizarTela()

        binding.btnEnviarVoto.setOnClickListener {
            enviarVoto()
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarTela()
    }

    private fun atualizarTela() {
        val filme = SessionManager.filmeVotado
        val diretor = SessionManager.diretorVotado
        val votoConfirmado = SessionManager.votoConfirmado

        binding.tvFilmeSelecionado.text = filme?.let {
            "${it.nome}\n${it.genero}"
        } ?: "Nenhum filme escolhido."

        binding.tvDiretorSelecionado.text = diretor?.nome ?: "Nenhum diretor escolhido."

        binding.tvAviso.text = when {
            votoConfirmado -> "Voto já confirmado. As escolhas podem ser visualizadas, mas não alteradas."
            filme == null && diretor == null -> "Escolha um filme e um diretor antes de enviar."
            filme == null -> "Escolha um filme antes de enviar."
            diretor == null -> "Escolha um diretor antes de enviar."
            else -> "Digite o token exibido na tela de boas-vindas."
        }

        binding.etTokenConfirmacao.isEnabled = !votoConfirmado
        binding.btnEnviarVoto.isEnabled = !votoConfirmado && SessionManager.prontoParaConfirmar()
    }

    private fun enviarVoto() {
        // Filme e diretor ficam locais ate o usuario confirmar o token nesta tela.
        val filme = SessionManager.filmeVotado
        val diretor = SessionManager.diretorVotado

        if (filme == null || diretor == null) {
            mostrarDialogo("Voto incompleto", "Escolha um filme e um diretor antes de confirmar.")
            atualizarTela()
            return
        }

        val tokenTexto = binding.etTokenConfirmacao.text.toString().trim()
        if (tokenTexto.isEmpty()) {
            binding.etTokenConfirmacao.error = "Informe o token."
            return
        }

        val token = tokenTexto.toIntOrNull()
        if (token == null) {
            binding.etTokenConfirmacao.error = "Token deve ser numérico."
            return
        }

        val filmeId = filme.id.toLongOrNull()
        val diretorId = diretor.id.toLongOrNull()

        if (filmeId == null || diretorId == null) {
            mostrarDialogo("Voto inválido", "Escolha filme e diretor novamente.")
            return
        }

        lifecycleScope.launch {
            setCarregando(true)
            try {
                val resposta = RetrofitCentral.instance.registrarVoto(
                    VotoRequest(
                        token = token,
                        filmeId = filmeId,
                        diretorId = diretorId
                    )
                )

                if (resposta.isSuccessful && resposta.body()?.sucesso == true) {
                    SessionManager.votoConfirmado = true
                    atualizarTela()
                    mostrarDialogo(
                        "Voto confirmado",
                        resposta.body()?.mensagem ?: "Voto registrado com sucesso."
                    )
                } else {
                    mostrarDialogo("Falha ao confirmar", mensagemDaResposta(resposta))
                }
            } catch (e: Exception) {
                mostrarDialogo("Falha ao confirmar", "Não foi possível conectar ao servidor.")
            } finally {
                setCarregando(false)
                atualizarTela()
            }
        }
    }

    private fun setCarregando(carregando: Boolean) {
        binding.progressConfirmacao.visibility = if (carregando) View.VISIBLE else View.GONE
        binding.btnEnviarVoto.isEnabled =
            !carregando && !SessionManager.votoConfirmado && SessionManager.prontoParaConfirmar()
        binding.etTokenConfirmacao.isEnabled = !carregando && !SessionManager.votoConfirmado
    }

    private fun mensagemDaResposta(resposta: Response<ApiResponse>): String {
        val mensagemBody = resposta.body()?.mensagem
        if (!mensagemBody.isNullOrBlank()) return mensagemBody

        val erro = resposta.errorBody()?.string()
        if (!erro.isNullOrBlank()) {
            val mensagemJson = runCatching {
                JSONObject(erro).optString("mensagem")
            }.getOrNull()

            if (!mensagemJson.isNullOrBlank()) return mensagemJson
        }

        return when (resposta.code()) {
            400 -> "Dados inválidos para confirmação do voto."
            401 -> "Token inválido."
            else -> "Não foi possível confirmar o voto."
        }
    }

    private fun mostrarDialogo(titulo: String, mensagem: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensagem)
            .setPositiveButton("OK", null)
            .show()
    }
}
