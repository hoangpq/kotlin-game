import java.awt.Graphics
import java.awt.Image
import java.util.concurrent.CopyOnWriteArrayList

class Weapon(x: Int, val y: Int, private val img: Image) : GameObject {
    var x: Int = 0
        private set
    private val speed = 10

    init {
        this.x = x
    }

    override fun draw(g: Graphics) {
        g.drawImage(this.img, this.x, this.y, 10, 10, null)
    }

    override fun update() {
        this.x += this.speed
    }

    fun checkOver(enemies: CopyOnWriteArrayList<Enemy>): Boolean {
        enemies.iterator().forEach {
            if (checkOverSingle(it)) {
                enemies.remove(it)
                return true
            }
        }
        return false
    }

    fun checkOverSingle(enemies: Enemy): Boolean {
        return this.x >= enemies.getX()
                && this.y >= enemies.getY()
                && this.y <= enemies.getY() + 46
                && this.x - enemies.getX() < 150
    }
}
