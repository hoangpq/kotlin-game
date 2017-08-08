import java.awt.Color
import javax.swing.*

object GameFrame {

    private fun hexToInt(hex: String): Int {
        return Integer.parseInt(hex, 16)
    }

    @JvmStatic fun main(args: Array<String>) {
        val f = JFrame()
        f.title = "Spaceship game"
        f.setSize(500, 500)
        f.isResizable = false
        f.layout = java.awt.BorderLayout()
        f.setLocationRelativeTo(null)
        f.contentPane.add(GamePanel(), "Center")
        f.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        f.isVisible = true
        f.background = Color(hexToInt("FF"), hexToInt("D7"), hexToInt("00"))
    }
}
