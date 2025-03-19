package modelo

enum class CampoEvento {
    ABERTURA, MARCACAO, DESMARCACAO, EXPLOSAO, REINICIALIZACAO
}

data class Campo(val linha: Int, val coluna: Int) {
    private val vizinhos = ArrayList<Campo>()
    private val callbacks = ArrayList<(Campo, CampoEvento) -> Unit>()
    var marcado: Boolean = false
    var aberto: Boolean = false
    var minado: Boolean = false
    val desmarcado: Boolean get() = !marcado
    val fechado: Boolean get() = !aberto
    val seguro: Boolean get() = !minado
    val objetivoAlcancado: Boolean get() = seguro && aberto || minado && marcado
    val qtdVizinjosMinados: Int get() = vizinhos.filter { it.minado }.size


    fun addVizinho(vizinho: Campo) {
        vizinhos.add(vizinho)
    }

    fun onEvento(callback: (Campo, CampoEvento) -> Unit) {
        callbacks.add(callback)
    }

    fun abrir() {
        if (fechado) {
            aberto = true
            if (minado) {
                callbacks.forEach { it(this, CampoEvento.EXPLOSAO) }
            } else {
                callbacks.forEach {
                    it(this, CampoEvento.ABERTURA)
                    vizinhos.filter { it.fechado && it.seguro && qtdVizinjosMinados == 0 }.forEach { it.abrir() }
                }
            }
        }
    }

    fun alterarMarcacao() {
        if (fechado) {
            marcado = !marcado
            val evento = if (marcado) CampoEvento.MARCACAO else CampoEvento.DESMARCACAO
            callbacks.forEach { it(this, evento) }
        }
    }

    fun minar() {
        minado = true
    }

    fun reiniciar() {
        aberto = false
        minado = false
        marcado = false
        callbacks.forEach { it(this, CampoEvento.REINICIALIZACAO) }
    }
}
