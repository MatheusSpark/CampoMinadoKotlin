package modelo

import kotlin.random.Random

enum class TabuleiroEvento { VITORIA, DERROTA }

class Tabuleiro(val qtdLinhas: Int, val qtdColunas: Int, private val qtdMinas: Int) {
    private val campos = ArrayList<ArrayList<Campo>>()
    private val callbacks = ArrayList<(TabuleiroEvento) -> Unit>()

    init {
        gerarCampos()
        associarVizinhos()
        sortearMinas()
    }

    private fun sortearMinas() {
        val gerador = Random
        var linhaSorteada = -1
        var colunaSorteada = -1
        var qtdMinasAtual = 0
        while (qtdMinasAtual < this.qtdMinas) {
            linhaSorteada = gerador.nextInt(qtdLinhas)
            colunaSorteada = gerador.nextInt(qtdColunas)
            val campoSorteado = campos[linhaSorteada][colunaSorteada]
            if (campoSorteado.seguro) {
                campoSorteado.minar()
                qtdMinasAtual++
            }
        }
    }


    private fun associarVizinhos() {
        forEachCampo { associarVizinhos(it) }

    }

    private fun associarVizinhos(campo: Campo) {
        val (linha, coluna) = campo
        val linhas = arrayOf(linha - 1, linha, linha + 1)
        val colunas = arrayOf(coluna - 1, coluna, coluna + 1)
        linhas.forEach { l ->
            colunas.forEach { c ->
                val atual = campos.getOrNull(l)?.getOrNull(c)
                atual?.takeIf { campo != it }?.let { campo.addVizinho(it) }
            }
        }
    }

    private fun gerarCampos() {
        for (linha in 0 until qtdLinhas) {
            campos.add(ArrayList())
            for (coluna in 0 until qtdColunas) {
                val novoCampo = Campo(linha, coluna)
                novoCampo.onEvento(this::verificarVitoriaOuDerrota)
                campos[linha].add(novoCampo)
            }
        }
    }
    private fun objetivoAlcancado():Boolean {
        var jogadorGanhou = true
        forEachCampo { if (!it.objetivoAlcancado) jogadorGanhou = false }
        return jogadorGanhou
    }
    private fun verificarVitoriaOuDerrota(campo: Campo, evento: CampoEvento){
        if (evento ==  CampoEvento.EXPLOSAO){
            callbacks.forEach { it(TabuleiroEvento.DERROTA) }
        }else if (objetivoAlcancado()){
            callbacks.forEach { it(TabuleiroEvento.VITORIA) }
        }
    }
    fun forEachCampo(callbak: (Campo) -> Unit) {
        campos.forEach { linha -> linha.forEach(callbak) }
    }

    fun onEvento(callback: (TabuleiroEvento) -> Unit){
        callbacks.add (callback)
    }
    fun reinicializar(){
        forEachCampo { it.reiniciar() }
        sortearMinas()
    }
}
