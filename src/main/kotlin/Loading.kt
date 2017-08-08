import javax.swing.JProgressBar

class Loading(private val jp: JProgressBar) : Thread() {
    var value: Int = 0

    init {
        this.jp.maximum = 100
    }

    override fun run() {
        try {
            this.value = 0
            while (this.value <= 100) {
                this.jp.value = this.value
                Thread.sleep(100L)
                this.value += 2
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
