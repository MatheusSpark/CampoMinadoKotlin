package visao

import modelo.Campo
import modelo.Tabuleiro
import java.awt.GridLayout
import javax.swing.JPanel

class PainelTabuleiro(tabuleiro: Tabuleiro) : JPanel() {
    init {
        layout = GridLayout(tabuleiro.qtdLinhas, tabuleiro.qtdColunas)
        tabuleiro.forEachCampo { campo: Campo ->
            val botao = BotaoCampo(campo)
            add(botao)
        }
    }
}
