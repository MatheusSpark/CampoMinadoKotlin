package visao

import modelo.Tabuleiro
import modelo.TabuleiroEvento
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main(){
    TelaPrincipal()
}
class TelaPrincipal: JFrame(){
    private val tabuleiro = Tabuleiro(16, 30, 60)
    private val painelTabuleiro = PainelTabuleiro(tabuleiro)
    init {
        tabuleiro.onEvento(this::mostrarResultado)
        add(painelTabuleiro)
        setSize(690, 438)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Campo minado"
        isVisible = true
    }

    private fun mostrarResultado(tabuleiroEvento: TabuleiroEvento) {
        SwingUtilities.invokeLater {
            val msg = when (tabuleiroEvento) {
                TabuleiroEvento.VITORIA -> "Você ganhou!"
                TabuleiroEvento.DERROTA -> "Você perdeu... :P"
            }
            JOptionPane.showMessageDialog(this,msg)
            tabuleiro.reinicializar()
            painelTabuleiro.repaint()
            painelTabuleiro.validate()
        }
    }
}