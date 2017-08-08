import java.awt.Graphics
import java.awt.Image

class Enemy(private var x: Double, private val y: Double, private val img: Image): GameObject {
    private val speed: Double = 5.0

    override fun draw(g: Graphics) {
        g.drawImage(this.img, this.x.toInt(), this.y.toInt(), null)
    }

    override fun update() {
        this.x -= this.speed
    }

    fun getX(): Int {
        return this.x.toInt()
    }

    fun getY(): Int {
        return this.y.toInt()
    }

    fun checkOver(_des: Int): Boolean {
        return this.x.toInt() < _des - 50
    }
}
