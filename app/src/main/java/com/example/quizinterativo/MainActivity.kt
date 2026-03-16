package com.example.quizinterativo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Usando o tema do Material 3 corretamente
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                QuizApp()
            }
        }
    }
}

@Composable
fun QuizApp() {
    var nivelSelecionado by remember { mutableStateOf<String?>(null) }
    var perguntasFiltradas by remember { mutableStateOf<List<Pergunta>>(emptyList()) }
    var indiceAtual by remember { mutableIntStateOf(0) }
    var pontuacao by remember { mutableIntStateOf(0) }
    var quizFinalizado by remember { mutableStateOf(false) }

    when {
        quizFinalizado -> {
            TelaFinal(pontuacao, perguntasFiltradas.size) {
                nivelSelecionado = null
                indiceAtual = 0
                pontuacao = 0
                quizFinalizado = false
            }
        }
        nivelSelecionado == null -> {
            TelaInicial { nivel ->
                nivelSelecionado = nivel
                perguntasFiltradas = RepositorioDePerguntas.lista.filter { it.nivel == nivel }
            }
        }
        else -> {
            TelaQuiz(
                pergunta = perguntasFiltradas[indiceAtual],
                pontuacaoAtual = pontuacao,
                numeroAtual = indiceAtual + 1,
                total = perguntasFiltradas.size,
                onProxima = { acertou ->
                    if (acertou) pontuacao++
                    if (indiceAtual + 1 < perguntasFiltradas.size) {
                        indiceAtual++
                    } else {
                        quizFinalizado = true
                    }
                }
            )
        }
    }
}

@Composable
fun TelaInicial(onNivelEscolhido: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dev Quiz Android", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Escolha a dificuldade:")

        listOf("Fácil", "Médio", "Difícil").forEach { nivel ->
            Button(
                onClick = { onNivelEscolhido(nivel) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text(nivel)
            }
        }
    }
}

@Composable
fun TelaQuiz(pergunta: Pergunta, pontuacaoAtual: Int, numeroAtual: Int, total: Int, onProxima: (Boolean) -> Unit) {
    var respostaUsuario by remember { mutableStateOf<String?>(null) }
    var respondeu by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Pontuação: $pontuacaoAtual", fontWeight = FontWeight.Bold)
        Text("Nível: ${pergunta.nivel}", color = Color.Gray)

        LinearProgressIndicator(
            progress = { numeroAtual.toFloat() / total },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )

        Text(pergunta.texto, fontSize = 20.sp, modifier = Modifier.padding(bottom = 24.dp))

        pergunta.opcoes.forEach { opcao ->
            val corBotao = when {
                respondeu && opcao == pergunta.respostaCorreta -> Color(0xFF4CAF50) // Verde
                respondeu && opcao == respostaUsuario -> Color(0xFFF44336) // Vermelho
                else -> ButtonDefaults.buttonColors().containerColor
            }

            Button(
                onClick = { if (!respondeu) { respostaUsuario = opcao; respondeu = true } },
                colors = ButtonDefaults.buttonColors(containerColor = corBotao),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                enabled = !respondeu
            ) {
                Text(opcao)
            }
        }

        if (respondeu) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (respostaUsuario == pergunta.respostaCorreta) "Correto! ✨" else "Errado! ❌",
                fontSize = 18.sp, fontWeight = FontWeight.Medium
            )
            Button(
                onClick = {
                    val foiCorreto = (respostaUsuario == pergunta.respostaCorreta)
                    respondeu = false
                    respostaUsuario = null
                    onProxima(foiCorreto)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Próxima")
            }
        }
    }
}

@Composable
fun TelaFinal(pontos: Int, total: Int, aoReiniciar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val mensagem = if (pontos >= total / 2) "Parabéns!" else "Tente novamente!"
        Text(mensagem, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Você acertou $pontos de $total perguntas.", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = aoReiniciar) {
            Text("Voltar ao Início")
        }
    }
}