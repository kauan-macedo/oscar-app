
Claude terminou a resposta
Trab2_Mobile_OscarApp.pdf
pdf

Estou fazendo a primeira issue desse trabalho. Me mande os códigos.

Escopo
Preparar o projeto Android para as demais telas e integracoes.
Tarefas

Garantir compatibilidade com API 29 e validar execucao em dispositivo/emulador API 29.
Adicionar permissao de Internet no AndroidManifest.xml.
Adicionar dependencias ensinadas nos PDFs: Retrofit, converter Gson, Picasso e suporte a corrotinas/lifecycleScope se ainda faltar.
Criar pacotes coerentes: model, controller ou ui, network, session.
Criar modelos Android para LoginRequest, LoginResponse, Filme, Diretor, VotoRequest, ApiResponse.
Criar um estado de sessao em memoria para guardar login/token e votos locais ate a confirmacao final.
Definir clientes Retrofit separados para Sistema Central e para os JSONs externos http://200.236.3.97/filme.json e http://200.236.3.97/diretor.json.
Criterios de aceite

App compila e abre na tela inicial sem crash.
Nenhum acesso direto do app ao banco.
Chamadas de rede ficam preparadas para rodar fora da UI thread.
Estado de sessao permite guardar filme, diretor, token e flag de voto confirmado.
Mostrar mais
14:06
Vou gerar todos os arquivos dessa issue de setup. Deixa eu organizar tudo de forma estruturada.


Check skills available
Check skills available
Perfeito. Vou gerar todos os arquivos da issue agora.

Os arquivos são Kotlin puro — vou entregar cada um separado com o caminho exato onde colocar no projeto.

AndroidManifest.xml — permissão de internet
Adicione dentro da tag <manifest>, antes de <application>:

xml
<uses-permission android:name="android.permission.INTERNET" />
E garanta que <application> tenha:

xml
android:usesCleartextTraffic="true"
Isso é necessário porque o servidor usa HTTP (não HTTPS). Sem isso, o Android 9+ bloqueia as chamadas.

build.gradle (app) — dependências

Build.gradle dependencies block for the Oscar App
Build.gradle dependencies block for the Oscar App

build.gradle dependencies block for the Oscar App
build_gradle_deps.txt
Concluído
Estrutura de pacotes
Crie os pacotes abaixo dentro de app/src/main/java/com/seugrupo/oscarapp/:

oscarapp/
├── model/          ← data classes (Filme, Diretor, requests, responses)
├── network/        ← Retrofit clients + interfaces de API
├── session/        ← SessionManager (estado em memória)
└── ui/             ← Activities (uma por tela)
Models — model/

All model data classes for Oscar App
All model data classes for Oscar App

All model data classes for Oscar App
Models.kt
Concluído
Network — dois clientes Retrofit separados

Criou 4 arquivos
Criou 4 arquivos
SessionManager — estado em memória

SessionManager singleton to hold in-memory session state
SessionManager singleton to hold in-memory session state
AndroidManifest.xml completo (referência)

AndroidManifest.xml reference for Oscar App
AndroidManifest.xml reference for Oscar App
Agora apresento tudo organizado:


Presented 8 files
Presented 8 files
Tudo gerado. Aqui está o mapa completo do que foi entregue e onde cada arquivo vai no projeto:

Onde colocar cada arquivo
Arquivo	Destino no projeto
Models.kt	…/oscarapp/model/Models.kt
SistemaCentralApi.kt	…/oscarapp/network/SistemaCentralApi.kt
AcademiaApi.kt	…/oscarapp/network/AcademiaApi.kt
RetrofitCentral.kt	…/oscarapp/network/RetrofitCentral.kt
RetrofitJsonExterno.kt	…/oscarapp/network/RetrofitJsonExterno.kt
SessionManager.kt	…/oscarapp/session/SessionManager.kt
AndroidManifest.xml	substitui o existente em app/src/main/
build_gradle_deps.txt	cola o conteúdo no build.gradle (Module: app)
Dois ajustes que você precisa fazer antes de compilar
RetrofitCentral.kt linha 13 — troque http://SEU_IP_AQUI:PORTA/ pelo endereço real do servidor do seu grupo (o backend que o grupo vai desenvolver).
build.gradle — o applicationId "com.seugrupo.oscarapp" deve bater com o pacote que o Android Studio gerou quando você criou o projeto. Se for diferente, ajuste lá e em todos os package com.seugrupo.oscarapp dos arquivos Kotlin.
O que esse setup garante para as próximas issues
SessionManager carrega o token logo após o login e mantém filmeVotado/diretorVotado vivos entre Activities sem precisar de Intent extras complexos.
Dois clients Retrofit separados porque a base URL do servidor da Academia (200.236.3.97) é diferente do servidor do grupo — misturá-los num só client causaria problema.
Todas as funções de API são suspend — você chama via lifecycleScope.launch { } dentro das Activities, nunca na UI thread diretamente.
viewBinding = true já habilitado no gradle — elimina todos os findViewById.

Models
KT 

Sistemacentralapi
KT 

Academiaapi
KT 

Retrofitcentral
KT 

Retrofitjsonexterno
KT 

Sessionmanager
KT 

Androidmanifest
XML 

Build gradle deps
TXT 
Meu build graddle está correto? Se não, ajuste.

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "br.ufpr.oscarmobile"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "br.ufpr.oscarmobile"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

Mostrar mais
14:09
Tem dois problemas aqui:

compileSdk com aquele bloco release(36) { minorApiLevel = 1 } — isso não existe na DSL do Android. É sintaxe inválida que vai quebrar o build.
Faltam todas as dependências do projeto (Retrofit, Gson, Picasso, Coroutines, Lifecycle).
Além disso, percebi que o projeto é Kotlin mas está faltando o plugin kotlin-android e a configuração kotlinOptions. Vou corrigir tudo:


Corrected build.gradle.kts for Oscar App
Corrected build.gradle.kts for Oscar App
Agora tem mais um ajuste necessário no libs.versions.toml para o plugin Kotlin aparecer via alias. Verifique se já existe — se não existir, adicione:


Criou um arquivo, leu um arquivo
Criou um arquivo, leu um arquivo
Resumo das correções
Bug crítico removido:

kotlin
// ❌ Era isso — não existe na DSL do Android
compileSdk {
    version = release(36) { minorApiLevel = 1 }
}

// ✅ Correto
compileSdk = 36
O que foi adicionado/ajustado:

Item	Motivo
alias(libs.plugins.kotlin.android)	Sem ele o compilador Kotlin não é ativado — projeto não compila
kotlinOptions { jvmTarget = "11" }	Tem que bater com o JavaVersion.VERSION_11 do compileOptions
viewBinding = true	Necessário para as Activities usarem o binding gerado
Retrofit, Gson, OkHttp, Picasso, Coroutines, Lifecycle	Tudo que a issue exige e que estava faltando
Após colar o arquivo, faça Sync Now no Android Studio. Se aparecer erro dizendo que kotlin.android não está no libs.versions.toml, adicione as linhas do segundo arquivo no gradle/libs.versions.toml do projeto.


Build.gradle
KTS 

Libs versions toml adicionar
TXT 


Claude Fable 5 está indisponível no momento.
Saiba mais(opens in new tab)




Claude é uma IA e pode cometer erros. Por favor, verifique as respostas.
Sistemacentralapi · KT
package com.seugrupo.oscarapp.network
 
import com.seugrupo.oscarapp.model.ApiResponse
import com.seugrupo.oscarapp.model.LoginRequest
import com.seugrupo.oscarapp.model.LoginResponse
import com.seugrupo.oscarapp.model.VotoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
 
/**
 * Ajustar os caminhos ("login" e "voto") para bater com as rotas reais do backend dps.
 */

interface SistemaCentralApi {

    @POST("login")
    suspend fun autenticar(@Body request: LoginRequest): Response<LoginResponse>
 

    @POST("voto")
    suspend fun registrarVoto(@Body request: VotoRequest): Response<ApiResponse>
}
 
