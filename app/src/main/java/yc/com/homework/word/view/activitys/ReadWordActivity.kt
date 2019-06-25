package yc.com.homework.word.view.activitys

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.iflytek.cloud.ErrorCode
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SpeechSynthesizer
import com.iflytek.cloud.SynthesizerListener


import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.TimeUnit

import butterknife.BindView
import butterknife.OnClick

import com.kk.utils.LogUtil
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.subjects.PublishSubject
import yc.com.base.BaseActivity
import yc.com.blankj.utilcode.util.ActivityUtils

import yc.com.homework.R
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.base.widget.MainToolBar
import yc.com.homework.base.widget.StateView
import yc.com.homework.word.common.SpeechUtils
import yc.com.homework.word.contract.ReadWordContract
import yc.com.homework.word.model.domain.WordDetailInfo
import yc.com.homework.word.model.domain.WordInfo
import yc.com.homework.word.presenter.ReadWordPresenter
import yc.com.homework.word.view.adapter.ReadWordExpandAdapter

/**
 * Created by admin on 2017/7/27.
 */

class ReadWordActivity : BaseActivity<ReadWordPresenter>(), ReadWordContract.View, ReadWordExpandAdapter.ItemViewClickListener {

    @BindView(R.id.sv_loading)
    lateinit var mStateView: StateView

    @BindView(R.id.layout_content)
    lateinit var mLayoutContext: RelativeLayout

    @BindView(R.id.rv_word_list)
    lateinit var mWordListView: ExpandableListView


    @BindView(R.id.iv_read_all)
    lateinit var mReadAllImageView: ImageView

    @BindView(R.id.iv_spell_icon)
    lateinit var mSpellWordImageView: ImageView

    @BindView(R.id.tv_read_current_num)
    lateinit var mReadCurrentNum: TextView

    @BindView(R.id.tv_read_total_num)
    lateinit var mReadTotalNum: TextView

    @BindView(R.id.pb_read_num)
    lateinit var mProgressReadNum: ProgressBar

    lateinit var mReadWordExpandAdapter: ReadWordExpandAdapter

    lateinit var mDatas: List<WordInfo>

    var isSpell = false
    @BindView(R.id.toolbar)
    lateinit var toolbar: MainToolBar


    // 语音合成对象
    private var mTts: SpeechSynthesizer? = null

    // 缓冲进度
    private var mPercentForBuffering = 0

    // 播放进度
    private var mPercentForPlaying = 0

    private var readCurrentWordIndex: Int = 0

    lateinit var mediaPlayer: MediaPlayer

    //当前读到的单词
    private var currentIndex: Int = 0

    private var isContinue = false

    private var unitId: String? = null

    private var unitTitle: String? = null

    private var mTsSubject: PublishSubject<Int>? = null

    private var isWordDetailPlay = false

    private var lastExpandPosition = -1

    private var wordInfos: MutableList<WordInfo>? = null

    private var wordDetailInfos: MutableList<WordDetailInfo>? = null

    private var groupCurrentIndex: Int = 0
    private var groupCurrentView: View? = null

    /**
     * 合成回调监听。
     */
    private val mTtsListener = object : SynthesizerListener {

        override fun onSpeakBegin() {
            //开始播放
        }

        override fun onSpeakPaused() {
            //暂停播放
        }

        override fun onSpeakResumed() {
            //继续播放
        }

        override fun onBufferProgress(percent: Int, beginPos: Int, endPos: Int, info: String) {
            // 合成进度
            mPercentForBuffering = percent
        }

        override fun onSpeakProgress(percent: Int, beginPos: Int, endPos: Int) {
            // 播放进度
            mPercentForPlaying = percent
        }

        override fun onCompleted(error: SpeechError?) {
            if (error == null) {
                if (disableWordDetailState()) {
                    return
                }
                playWord(currentIndex, Runnable { speekContinue() })
            } else {
                if (error.errorDescription.contains("权")) {
                    yc.com.homework.word.utils.SpeechUtils.resetAppid(this@ReadWordActivity)
                    return
                }
                if (disableWordDetailState()) {
                    return
                }

                speekContinue()
            }
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.read_activity_word_play
    }

    override fun init() {

        val bundle = intent.extras
        if (bundle != null) {
            unitId = bundle.getString("unit_id")
            unitTitle = bundle.getString("unit_title")
        }
        //        WakeLockUtils.acquireWakeLock(this);

        mPresenter = ReadWordPresenter(this, this)
        toolbar.setTitle(unitTitle)
        toolbar.showNavigationIcon()
        toolbar.init(this)
        toolbar.setRightContainerVisible(false)

        mediaPlayer = MediaPlayer()

        mTts = SpeechUtils.getTts(this)

        mReadWordExpandAdapter = ReadWordExpandAdapter(this@ReadWordActivity, wordInfos, wordDetailInfos)
        mReadWordExpandAdapter.setExpandableListView(mWordListView)
        mWordListView.setAdapter(mReadWordExpandAdapter)
        mWordListView.setGroupIndicator(null)
        mReadWordExpandAdapter.setItemDetailClick(this)
        mWordListView.setOnGroupClickListener { parent, v, groupPosition, id ->
            disableWordDetailState()
            isContinue = false
            readOver(currentIndex)
            if (groupPosition != lastExpandPosition && lastExpandPosition > -1) {
                mWordListView.collapseGroup(lastExpandPosition)
            }

            if (mWordListView.isGroupExpanded(groupPosition)) {
                mWordListView.collapseGroup(groupPosition)
            } else {
                mWordListView.expandGroup(groupPosition)
                lastExpandPosition = groupPosition
                mReadWordExpandAdapter.setLastExpandPosition(lastExpandPosition)
            }
            true
        }

        mWordListView.setOnChildClickListener(ExpandableListView.OnChildClickListener { parent, view, groupPosition, childPosition, id ->
            isContinue = false
            readOver(currentIndex)
            if (isWordDetailPlay) {
                return@OnChildClickListener true
            }
            groupCurrentIndex = groupPosition
            isWordDetailPlay = true
            startSynthesizer(wordDetailInfos!![groupPosition].wordExample)
            wordDetailInfos!![groupPosition].isPlay = true
            groupCurrentView = view
            mReadWordExpandAdapter.setChildViewPlayState(view, true)
            true
        })

        mTsSubject = PublishSubject.create<Int>()
        mTsSubject?.delay(800, TimeUnit.MILLISECONDS)?.observeOn(AndroidSchedulers.mainThread())?.subscribe { position ->
            if (position < mDatas.size) {
                endableState(currentIndex)
                startSynthesizer(position!!)
            } else {
                isContinue = false
                readOver(currentIndex)
                currentIndex = 0
                if (ActivityUtils.isValidContext(this@ReadWordActivity)) {
                    Glide.with(this@ReadWordActivity).load(R.mipmap.read_audio_white_stop).into(mReadAllImageView)
                }
            }
        }

        mPresenter.getWordListByUnitId(0, 0, unitId!!)

        if (yc.com.homework.word.utils.SpeechUtils.getAppids() == null || yc.com.homework.word.utils.SpeechUtils.getAppids().size <= 0) {
            yc.com.homework.word.utils.SpeechUtils.setAppids(this)
        }
    }


    override fun hide() {
        mStateView.hide()
    }

    override fun showNoNet() {
        mStateView.showNoNet(mLayoutContext, "网络不给力") { mPresenter.getWordListByUnitId(0, 0, unitId!!) }
    }

    override fun showNoData() {
        mStateView.showNoData(mLayoutContext)
    }

    override fun showLoading() {
        mStateView.showLoading(mLayoutContext)
    }

    override fun showWordListData(list: List<WordInfo>?) {
        list?.let {
            mDatas = list
            wordInfos = ArrayList()
            wordDetailInfos = ArrayList()
            setProgressNum(0, list.size)

            for (i in list.indices) {
                val wordInfo = list[i]
                val wordDetailInfo = WordDetailInfo()
                wordDetailInfo.wordExample = wordInfo.epSentence
                wordDetailInfo.wordCnExample = wordInfo.epSentenceMeans
                wordInfos?.add(wordInfo)
                wordDetailInfos?.add(wordDetailInfo)
            }

            mReadWordExpandAdapter.setNewDatas(wordInfos, wordDetailInfos)
            mReadWordExpandAdapter.notifyDataSetChanged()
        }

    }

    fun setProgressNum(current: Int, total: Int) {
        mReadCurrentNum.text = "$current"
        mReadTotalNum.text = "$total"
        mProgressReadNum.max = total
        mProgressReadNum.progress = current
    }

    /**
     * 拼读单个单词
     */
    fun playWord(index: Int, runnable: Runnable?) {
        if (isSpell) {
            try {

                val readCurrentWord = mDatas[index].name.replace("[\\s\\W]".toRegex(), "")
                if (readCurrentWordIndex < readCurrentWord.length) {
                    mediaPlayer.reset()
                    val readChat = readCurrentWord[readCurrentWordIndex].toString().toLowerCase()
                    LogUtil.msg("readChat:  $readChat")
                    val fd = assets.openFd("$readChat.mp3")
                    mediaPlayer.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener {
                        readCurrentWordIndex++
                        playWord(index, runnable)
                    }
                } else {
                    readCurrentWordIndex = 0
                    runnable?.run()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            runnable?.run()
        }
    }


    //单词朗读
    fun startSynthesizer(position: Int) {
        val text = wordInfos!![position].name
        val code = mTts?.startSpeaking(text, mTtsListener)
        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                // 未安装则跳转到提示安装页面
                //mInstaller.install();
            } else {
                ToastUtils.showCenterToast(this, "语音合成失败,错误码: $code")
                mTts?.stopSpeaking()
            }
        }
    }

    //示例句子朗读
    fun startSynthesizer(sentence: String) {
        val code = mTts?.startSpeaking(sentence, mTtsListener)
        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                // 未安装则跳转到提示安装页面
                //mInstaller.install();
            } else {
                ToastUtils.showCenterToast(this, "语音合成失败,错误码: $code")
                mTts?.stopSpeaking()
            }
        }
    }


    private fun disableWordDetailState(): Boolean {
        if (isWordDetailPlay) {
            stopSts()

            isWordDetailPlay = false
            if (groupCurrentView != null && groupCurrentIndex != -1) {
                wordDetailInfos?.get(groupCurrentIndex)?.isPlay = false
                mReadWordExpandAdapter.setChildViewPlayState(groupCurrentView, false)
            }
        }
        return isWordDetailPlay
    }

    private fun speekContinue() {
        readOver(currentIndex)
        if (isContinue && currentIndex < mReadWordExpandAdapter.wordInfos!!.size) {
            mTsSubject?.onNext(++currentIndex)
        }
    }

    private fun endableState(index: Int) {
        if (index < 0 || index >= mReadWordExpandAdapter.wordInfos!!.size) {
            return
        }

        resetPlay()
        mReadWordExpandAdapter.setViewPlayState(index, mWordListView, true)
        mReadWordExpandAdapter.wordInfos?.get(index)?.isPlay = true
        setProgressNum(currentIndex + 1, mDatas.size)
        mWordListView.smoothScrollToPosition(index)
    }

    private fun disableState(index: Int) {
        if (index <= 0 && index < mReadWordExpandAdapter.wordInfos?.size!!) {
            return
        }

        mReadWordExpandAdapter.setViewPlayState(index, mWordListView, false)
        mReadWordExpandAdapter.wordInfos!![index].isPlay = false

        stopSts()
    }

    private fun stopSts() {
        try {
            mTts?.let {
                if (mTts!!.isSpeaking) {
                    mTts?.stopSpeaking()
                }
            }
        } catch (e: Exception) {
            LogUtil.msg("e : ${e.message}")
        }

    }

    private fun readOver(index: Int) {
        resetPlay()
        mReadWordExpandAdapter.setViewPlayState(index, mWordListView, false)
        stopSts()

        if (!isContinue && ActivityUtils.isValidContext(this) && ActivityUtils.isValidContext(this@ReadWordActivity)) {
            Glide.with(this@ReadWordActivity).load(R.mipmap.read_audio_white_stop).into(mReadAllImageView)
        }
    }

    fun resetPlay() {
        for (wordInfo in mReadWordExpandAdapter.wordInfos!!) {
            wordInfo.isPlay = false
        }
    }


    /**
     * 单词闯关
     */
    @OnClick(R.id.layout_pass_word)
    fun wordPractice() {
        val intent = Intent(this@ReadWordActivity, WordPracticeActivity::class.java)
        intent.putExtra("unit_id", unitId)
        startActivity(intent)
    }

    /**
     * 全部朗读
     */
    @OnClick(R.id.layout_read_all)
    fun readAll() {
        disableWordDetailState()
        isContinue = !isContinue
        if (isContinue) {
            if (currentIndex < mDatas.size) {
                if (ActivityUtils.isValidContext(this@ReadWordActivity)) {
                    Glide.with(this@ReadWordActivity).load(R.mipmap.read_audio_white_gif_play).into(mReadAllImageView)
                }
                endableState(currentIndex)
                startSynthesizer(currentIndex)
            }
        } else {
            readOver(currentIndex)
        }
    }

    /**
     * 拼写朗读
     */
    @OnClick(R.id.layout_spell_word)
    fun spellWord() {
        isSpell = !isSpell

        if (isSpell) {
            mSpellWordImageView.visibility = View.VISIBLE
        } else {
            mSpellWordImageView.visibility = View.GONE
        }
    }

    override fun groupWordClick(gPosition: Int) {

        isContinue = false
        disableWordDetailState()
        if (currentIndex != gPosition) {
            disableState(currentIndex)
        }
        currentIndex = gPosition
        endableState(currentIndex)
        startSynthesizer(currentIndex)
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()

        if (mTts != null) {
            mTts?.stopSpeaking()
            // 退出时释放连接
            mTts?.destroy()
            mTts= null
        }
        //        WakeLockUtils.releaseWakeLock();
    }


}
