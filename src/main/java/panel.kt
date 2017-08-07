import java.awt.Color
import java.awt.Font
import java.awt.FontFormatException
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.IOException
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import javax.swing.ImageIcon
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.Timer

class panel : JPanel(), KeyListener {
    private var xback1 = 0.0
    private var xback2 = 0.0
    private val cityx = 0.0
    private val city1speed = 1
    private val city2speed = 2
    private var scores = 0
    private var blood = 100
    private var isdead = false
    private var isstart = false
    private var lock = true
    private var isUpPressed = false
    private var isDownPressed = false
    private var isLeftPressed = false
    private var isRightPressed = false
    private var isSpacePressed = false
    private var musiclock = true
    private val rd: Random
    private lateinit var timer: Timer

    private var city1: Image? = null
    private var city2: Image? = null
    private var start: Image? = null
    private var gameover: Image? = null
    private var loading: Image? = null
    private lateinit var playerImage: Image
    private lateinit var explosionImage: Image
    private var background: Image? = null
    private lateinit var enemyImage: Image

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var lockairplane: Int = 0
    private lateinit var _airplane: airplane
    private lateinit var _playsound: soundplay
    private lateinit var _backgroundsound: soundplay
    private var screenFont: Font? = null
    private val jp: JProgressBar
    private lateinit var th: loading

    private var _enemy: CopyOnWriteArrayList<enemy> = CopyOnWriteArrayList()
    private var ex: CopyOnWriteArrayList<explosion> = CopyOnWriteArrayList()

    init {
        addKeyListener(this)
        isFocusable = true
        this.rd = Random()
        try {
            screenWidth = toolkit.screenSize.width
            screenHeight = toolkit.screenSize.height
            loading = ImageIcon(javaClass.getResource("/images/loading.png")).image
            start = ImageIcon(javaClass.getResource("/images/start.png")).image
            gameover = ImageIcon(javaClass.getResource("/images/restart.png")).image
            background = ImageIcon(javaClass.getResource("/images/bg.png")).image
            city1 = ImageIcon(javaClass.getResource("/images/city1.png")).image
            city2 = ImageIcon(javaClass.getResource("/images/city2.png")).image
            enemyImage = ImageIcon(javaClass.getResource("/images/mine.png")).image
            playerImage = ImageIcon(javaClass.getResource("/images/redJet.png")).image
            explosionImage = ImageIcon(javaClass.getResource("/images/explosion.png")).image
            val weaponImage = ImageIcon(javaClass.getResource("/images/weapon.png")).image
            _playsound = soundplay("/sounds/explosion.wav", false)
            _backgroundsound = soundplay("/sounds/audio.wav", true)
            _airplane = airplane(50, 100, this.playerImage, weaponImage, this.explosionImage)
            _enemy = CopyOnWriteArrayList()
            ex = CopyOnWriteArrayList()
            for (i in 0..199) {
                this._enemy.add(enemy((this.lockairplane + 600 + i * 200).toDouble(),
                        (50 + this.rd.nextInt(201)).toDouble(), this.enemyImage))
            }
            val inpStream = javaClass.getResourceAsStream("/fonts/starcraft.ttf")
            try {
                this.screenFont = Font.createFont(0, inpStream).deriveFont(20.0f)
            } catch (e: FontFormatException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            this.timer = Timer(10) { evt ->
                if (this.isstart && !this.lock) {
                    this.th.stop()
                    this._airplane.updateWeapon()
                    if (this._airplane.shootCollision(this._enemy, this.ex, this._playsound)) {
                        this.scores += 1
                    }
                    this._airplane.checkTimeLive()
                    if (this.isSpacePressed) {
                        if (this.isdead) {
                            this.timer.restart()
                            this.isdead = false
                            this.blood = 100
                            this.scores = 0
                            this._backgroundsound.play()
                            this.isstart = true
                        }
                        this._airplane.fire()
                    }
                    if (this.isUpPressed) {
                        if (this._airplane.y > 10) {
                            this._airplane.moveHor(-2)
                        }
                    } else if (this.isDownPressed && this._airplane.y < 400) {
                        this._airplane.moveHor(2)
                    }

                    if (this.isRightPressed) {
                        if (this._airplane.x < 300) {
                            this._airplane.moveVer(2)
                        } else {
                            this.xback1 -= this.city1speed.toDouble()
                            this.xback2 -= this.city2speed.toDouble()
                        }
                    } else if (this.isLeftPressed && this._airplane.x > 0) {
                        this._airplane.moveVer(-2)
                    }
                    this.lockairplane = this.xback1.toInt()

                    // TODO: update enemy
                    if (this._enemy.size > 1) {
                        this._enemy.iterator().forEach {
                            it.update()
                            if (it.checkOver(this.lockairplane)) {
                                this._enemy.remove(it)
                            } else if (this._airplane.checkCollision(it)) {
                                this._enemy.remove(it)
                                this.ex.add(explosion(it.getX(), it.getY(), this.explosionImage))
                                this._playsound.play()
                                if (this.blood <= 10) {
                                    this.isdead = true
                                    this.timer.stop()
                                } else {
                                    this.blood -= 10
                                }
                            }
                        }
                    }
                    // TODO: remove all explosion
                    this.ex.iterator().forEach {
                        it.update()
                        if (it.checkOver()) {
                            this.ex.remove(it)
                        }
                    }
                    this.repaint()
                } else {
                    this.repaint()
                }
            }
        } catch (e: Exception) {
            e.message
        }

        this.timer.start()
        this.jp = JProgressBar()
        this.th = loading(this.jp)
        this.th.start()
    }

    public override fun paintComponent(g: Graphics) {
        g.clearRect(0, 0, 500, 500)
        val g2 = g as Graphics2D
        val overString = "Press space to"
        val startString = "Press any key to start"
        val loadingString = "Loading"
        g.setFont(this.screenFont)
        g.setColor(Color.GREEN)
        if (!this.isstart) {
            g2.drawImage(this.loading, 0, 0, 500, 500, this)
            if (this.th.value < 100) {
                val width = g2.fontMetrics.stringWidth(loadingString)
                if (this.th.value % 4 == 0) {
                    g2.drawString("", 250 - width / 2, 50)
                } else {
                    g2.drawString(loadingString, 250 - width / 2, 50)
                }

            } else {
                val width = g2.fontMetrics.stringWidth(startString)
                g2.drawString(startString, 250 - width / 2, 50)
                this.lock = false
            }
            g2.color = Color(Integer.parseInt("00", 16), Integer.parseInt("CC", 16), Integer.parseInt("ff", 16))

            g2.fillRect(30, 409, 436 * this.th.value / 100, 19)
        } else if (this.isstart && !this.lock) {
            if (this.isdead) {

                this._backgroundsound.stopClip()
                g2.drawImage(this.gameover, 0, 0, 500, 500, this)
                val width = g2.fontMetrics.stringWidth(overString)
                g2.drawString(overString, 250 - width / 2, 50)

            } else {
                g.drawImage(this.background, 0, -50, this.width, this.height, this)
                if (this.xback1 > -500.0) {
                    this.xback1 -= this.cityx
                } else {
                    this.xback1 = 0.0
                }
                if (this.xback2 > -500.0) {
                    this.xback2 -= this.cityx
                } else {
                    this.xback2 = 0.0
                }
                g.drawImage(this.city1, this.xback1.toInt(), 50, 500, 450, this)
                g.drawImage(this.city1, this.xback1.toInt() + 500, 50, 500, 450, this)
                g.drawImage(this.city2, this.xback2.toInt(), 300, 500, 200, this)
                g.drawImage(this.city2, this.xback2.toInt() + 500, 300, 500, 200, this)
                for (i in this._enemy.indices) {
                    (this._enemy[i] as enemy).draw(g)
                }
                this._airplane.draw(g)
                for (i in this.ex.indices) {
                    (this.ex[i] as explosion).draw(g)
                }
                g.setColor(Color.MAGENTA)
                g.drawString("Scores : " + this.scores.toString(), 10, 30)
                g.setColor(Color.RED)
                g.fillRect(200, 15, this.blood * 2, 20)
                g.setColor(Color.WHITE)
                g.fillRect(200 + this.blood * 2, 15, (100 - this.blood) * 2, 20)
            }
        }
    }

    override fun keyPressed(arg0: KeyEvent) {
        if (!this.lock) {
            if (this.musiclock) {
                this._backgroundsound.play()
                this.musiclock = false
            }
            if (!this.isstart) {
                this.isstart = true
                this.timer.start()
            }
        }
        when (arg0.keyCode) {
            38 -> {
                this.isUpPressed = true
                this.isDownPressed = false
            }
            40 -> {
                this.isDownPressed = true
                this.isUpPressed = false
            }
            37 -> {
                this.isLeftPressed = true
                this.isRightPressed = false
            }
            39 -> {
                this.isRightPressed = true
                this.isLeftPressed = false
            }
            32 -> {
                this.timer.start()
                this.isSpacePressed = true
            }
        }
    }


    override fun keyReleased(arg0: KeyEvent) {
        when (arg0.keyCode) {
            38 -> this.isUpPressed = false
            40 -> this.isDownPressed = false
            37 -> this.isLeftPressed = false
            39 -> this.isRightPressed = false
            32 -> this.isSpacePressed = false
        }
    }


    override fun keyTyped(arg0: KeyEvent) {}

    companion object {
        private val serialVersionUID = 1L
    }
}

