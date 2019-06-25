package yc.com.homework.read.listener

/**
 * Created by wanglin  on 2018/11/1 15:48.
 */
interface OnAVManagerListener {

    val isRecording: Boolean

    val isPlaying: Boolean//是否在播放

    fun playMusic(musicUrl: String?, startTime: Float, endTime: Float, lastEndtime: Float)


    fun playMusic(musicUrl: String?)

    fun playMusic()

    fun stopMusic()

    fun pauseMusic()

    fun startRecordAndSynthesis(word: String, isWord: Boolean)

    fun stopRecord()

    fun playRecordFile()

    fun playAssetFile(assetFilePath: String, step: Int)

    fun destroy()


}
