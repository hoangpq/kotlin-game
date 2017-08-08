import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.util.concurrent.CopyOnWriteArrayList

class Airplane(x: Int, y: Int, private val img: Image,
               private val weaponImage: Image,
               private val explosionImage: Image) : GameObject {

    var x: Int = 0
        private set
    var y: Int = 0
        private set
    private val weapons: CopyOnWriteArrayList<Weapon>

    init {
        this.x = x
        this.y = y
        this.weapons = CopyOnWriteArrayList<Weapon>()
    }

    fun moveVer(_value: Int) {
        this.x += _value
    }

    fun moveHor(_value: Int) {
        this.y += _value
    }

    override fun draw(g: Graphics) {
        val g2 = g as Graphics2D
        g2.drawImage(this.img, this.x, this.y, 60, 30, null)
        // TODO: draw models.Weapon
        this.weapons.forEach { it.draw(g) }
    }

    override fun update() {
        // TODO: update models.Weapon
        this.weapons.forEach { it.update() }
    }

    fun fire() {
        if (this.weapons.size > 20)
            return
        this.weapons.add(Weapon(this.x + 50, this.y + 10, this.weaponImage))
    }

    fun checkCollision(enemy: Enemy): Boolean {
        return this.x + 60 >= enemy.getX()
                && this.x < enemy.getX()
                && this.y >= enemy.getY() - 30
                && this.y < enemy.getY() + 46
    }

    fun checkTimeLive() {
        this.weapons.iterator().forEach {
            if (it.x - this.x >= 500) {
                this.weapons.remove(it)
            }
        }
    }

    fun shootCollision(enemies: CopyOnWriteArrayList<Enemy>,
                       explosion: CopyOnWriteArrayList<Explosion>,
                       soundControl: SoundControl): Boolean {

        this.weapons.iterator().forEach {
            if (it.checkOver(enemies)) {
                this.weapons.remove(it)
                explosion.add(Explosion(it.x, it.y, this.explosionImage))
                soundControl.play()
                return true
            }
        }
        return false
    }
}
