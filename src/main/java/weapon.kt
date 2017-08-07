import java.awt.Graphics2D
import java.awt.Image
import java.util.concurrent.CopyOnWriteArrayList

class weapon(_x: Int, val y: Int, private val img: Image) {
    var x: Int = 0
        private set
    private val speed = 10

    init {
        this.x = _x
    }

    fun draw(g: java.awt.Graphics) {
        val g2 = g as Graphics2D
        g2.drawImage(this.img, this.x, this.y, 10, 10, null)
    }

    fun update() {
        this.x += this.speed
    }

    fun checkOver(_enemy: CopyOnWriteArrayList<enemy>): Boolean {
        _enemy.iterator().forEach {
            if (checkOverSingle(it)) {
                _enemy.remove(it)
                return true
            }
        }
        return false
    }

    fun checkOverSingle(_enemy: enemy): Boolean {
        return this.x >= _enemy.getX() && this.y >= _enemy.getY() &&
                this.y <= _enemy.getY() + 46 && this.x - _enemy.getX() < 150
    }
}
