import java.awt.Graphics2D
import java.awt.Image
import java.util.concurrent.CopyOnWriteArrayList

class airplane(_x: Int, _y: Int, private val img: Image, private val weaponImage: Image, private val explosionImage: Image) {
    var x: Int = 0
        private set
    var y: Int = 0
        private set
    private val _weapon: CopyOnWriteArrayList<weapon>

    init {
        this.x = _x
        this.y = _y
        this._weapon = CopyOnWriteArrayList<weapon>()
    }

    fun moveVer(_value: Int) {
        this.x += _value
    }

    fun moveHor(_value: Int) {
        this.y += _value
    }

    fun draw(g: java.awt.Graphics) {
        val g2 = g as Graphics2D
        g2.drawImage(this.img, this.x, this.y, 60, 30, null)
        // TODO: draw weapon
        this._weapon.forEach { it.draw(g) }
    }

    fun fire() {
        if (this._weapon.size > 20)
            return
        this._weapon.add(weapon(this.x + 50, this.y + 10, this.weaponImage))
    }

    fun updateWeapon() {
        // TODO: update weapon
        this._weapon.forEach { it.update() }
    }

    fun checkCollision(ene: enemy): Boolean {
        return this.x + 60 >= ene.getX() && this.x < ene.getX() && this.y >= ene.getY() - 30 && this.y < ene.getY() + 46
    }

    fun checkTimeLive() {
        this._weapon.iterator().forEach {
            if (it.x - this.x >= 500) {
                this._weapon.remove(it)
            }
        }
    }

    fun shootCollision(_enemy: CopyOnWriteArrayList<enemy>, _ex: CopyOnWriteArrayList<explosion>, _playsound: soundplay): Boolean {
        this._weapon.iterator().forEach {
            if (it.checkOver(_enemy)) {
                this._weapon.remove(it)
                _ex.add(explosion(it.x, it.y, this.explosionImage))
                _playsound.play()
                return true
            }
        }
        return false
    }
}
