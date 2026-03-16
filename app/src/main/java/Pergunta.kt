package com.example.quizinterativo

data class Pergunta(
    val texto: String,
    val respostaCorreta: String,
    val nivel: String,
    val opcoes: List<String>
)

object RepositorioDePerguntas {
    val lista = listOf(
        // FÁCIL
        Pergunta("Qual linguagem estamos utilizando no Android Studio?", "Kotlin", "Fácil", listOf("Java", "Kotlin", "Python", "C#")),
        Pergunta("Qual componente usamos para mostrar texto no Compose?", "Text", "Fácil", listOf("Label", "TextView", "Text", "String")),
        Pergunta("Qual componente permite que o usuário digite texto?", "TextField", "Fácil", listOf("Input", "TextField", "EditText", "Box")),

        // MÉDIO
        Pergunta("Qual função é usada para guardar estado no Compose?", "remember", "Médio", listOf("save", "persist", "remember", "state")),
        Pergunta("Qual componente é usado para ações de clique?", "Button", "Médio", listOf("Click", "Button", "Press", "Action")),
        Pergunta("O Compose cria interfaces com XML ou Kotlin?", "Kotlin", "Médio", listOf("XML", "Kotlin", "HTML", "JSON")),

        // DIFÍCIL
        Pergunta("Qual conceito atualiza a UI quando um valor muda?", "Estado", "Difícil", listOf("Loop", "Estado", "Thread", "Callback")),
        Pergunta("Qual função define a interface principal da Activity?", "setContent", "Difícil", listOf("initUI", "onCreate", "setContent", "view")),
        Pergunta("No Compose, a interface é construída com funções...?", "Composables", "Difícil", listOf("Classes", "Widgets", "Composables", "Views"))
    )
}