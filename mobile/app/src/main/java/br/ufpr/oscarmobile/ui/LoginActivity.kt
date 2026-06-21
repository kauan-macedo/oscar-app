package br.ufpr.oscarmobile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.ufpr.oscarmobile.databinding.ActivityLoginBinding
import br.ufpr.oscarmobile.model.LoginRequest
import br.ufpr.oscarmobile.network.RetrofitCentral
import br.ufpr.oscarmobile.session.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener { tentarLogin() }
    }

    private fun tentarLogin() {
        val login = binding.etLogin.text.toString().trim()
        val senha = binding.etSenha.text.toString().trim()

        if (login.isEmpty() || senha.isEmpty()) {
            mostrarErro("Preencha usuário e senha.")
            return
        }

        setCarregando(true)

        lifecycleScope.launch {
            try {
                val resposta = RetrofitCentral.instance.autenticar(LoginRequest(login, senha))

                val body = resposta.body()

                if (resposta.isSuccessful && body?.sucesso == true && body.token != null) {

                    SessionManager.loginUsuario = login
                    SessionManager.token = body.token
                    SessionManager.filmeVotado = body.filme
                    SessionManager.diretorVotado = body.diretor
                    SessionManager.votoConfirmado = body.votoConfirmado

                    startActivity(Intent(this@LoginActivity, BoasVindasActivity::class.java))
                    finish()

                } else {
                    val mensagem = resposta.body()?.mensagem ?: "Usuário ou senha incorretos."
                    mostrarErro(mensagem)
                }

            } catch (e: Exception) {
                mostrarErro("Erro de conexão. Verifique a rede.")
            } finally {
                setCarregando(false)
            }
        }
    }

    private fun mostrarErro(msg: String) {
        binding.tvErro.text = msg
        binding.tvErro.visibility = View.VISIBLE
    }

    private fun setCarregando(carregando: Boolean) {
        binding.btnEntrar.isEnabled = !carregando
    }
}
