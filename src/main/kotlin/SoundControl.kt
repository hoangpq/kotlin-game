import javax.sound.sampled.Clip

class SoundControl(name: String, private val isloop: Boolean) {
    private val url: java.net.URL = GamePanel::class.java.getResource(name)
    private var clip: Clip? = null

    fun play() {
        try {
            this.clip = javax.sound.sampled.AudioSystem.getClip()
            this.clip!!.open(javax.sound.sampled.AudioSystem.getAudioInputStream(this.url))
        } catch (localException1: Exception) {
        }

        try {
            this.clip!!.start()
            if (this.isloop) {
                this.clip!!.loop(-1)
            }
        } catch (exc: Exception) {
            exc.printStackTrace(System.out)
        }

    }

    fun stopClip() {
        this.clip!!.stop()
    }
}
