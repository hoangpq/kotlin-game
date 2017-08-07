import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import javax.swing.ImageIcon

class explosion(private val x: Int, private val y: Int, private val img: Image) {
    private val ic: ImageIcon = ImageIcon(img)
    private val spritew: Float
    private val spriteh: Float
    private var frame = 1

    init {
        this.spritew = (this.ic.iconWidth / 8).toFloat()
        this.spriteh = this.ic.iconHeight.toFloat()
    }

    fun draw(g: Graphics) {
        val g2 = g as Graphics2D
        g2.drawImage(this.img, this.x, this.y, (this.x + 4.0f * this.spritew).toInt(),
                (this.y + 4.0f * this.spriteh).toInt(), this.spritew.toInt() * (this.frame - 1), 0,
                this.spritew.toInt() * this.frame, this.spriteh.toInt(), null)
    }

    fun update() {
        if (this.frame < 8) {
            this.frame += 1
        }
    }

    fun checkOver(): Boolean {
        return this.frame == 8
    }
}
