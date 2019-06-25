package yc.com.homework.word.view.activitys

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.iflytek.cloud.ErrorCode
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SpeechSynthesizer
import com.iflytek.cloud.SynthesizerListener
import com.jakewharton.rxbinding.view.RxView

import java.util.ArrayList
import java.util.Random
import java.util.TreeSet
import java.util.concurrent.TimeUnit

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.kk.utils.LogUtil
import rx.functions.Action1
import yc.com.base.BaseActivity
import yc.com.blankj.utilcode.util.StringUtils
import yc.com.homework.R
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.base.widget.MainToolBar
import yc.com.homework.base.widget.StateView
import yc.com.homework.word.common.SpeechUtils
import yc.com.homework.word.contract.ReadWordContract
import yc.com.homework.word.model.domain.LetterInfo
import yc.com.homework.word.model.domain.WordInfo
import yc.com.homework.word.presenter.ReadWordPresenter
import yc.com.homework.word.utils.DrawableUtils
import yc.com.homework.word.view.adapter.ReadLetterItemClickAdapter
import yc.com.homework.word.view.wdigets.CountDown
import yc.com.homework.word.view.wdigets.GridSpacingItemDecoration


/**
 * Created by admin on 2017/7/26.
 */

class WordPracticeActivity : BaseActivity<ReadWordPresenter>(), ReadWordContract.View {

    @BindView(R.id.sv_loading)
    lateinit var mStateView: StateView

    @BindView(R.id.layout_content)
    lateinit var mLayoutContext: RelativeLayout

    @BindView(R.id.rv_letter_list)
    lateinit var mLetterRecyclerView: RecyclerView

    @BindView(R.id.et_word_input)
    lateinit var mWordInputTextView: TextView

    @BindView(R.id.iv_word_delete)
    lateinit var mWordDeleteImageView: ImageView

    @BindView(R.id.iv_audio_play)
    lateinit var mAudioPlayImageView: ImageView

    @BindView(R.id.btn_right_next_word)
    lateinit var mRightNextButton: Button

    @BindView(R.id.btn_error_next_word)
    lateinit var mErrorNextButton: Button

    @BindView(R.id.btn_error_again_word)
    lateinit var mErrorAgainButton: Button

    @BindView(R.id.tv_cn_word)
    lateinit var mChineseWordTextView: TextView

    @BindView(R.id.tv_right_remind_word)
    lateinit var mRightRemindTextView: TextView

    @BindView(R.id.tv_word_count_down)
    lateinit var mWordCountDownTextView: TextView

    @BindView(R.id.btn_check_word)
    lateinit var mCheckWordButton: Button

    @BindView(R.id.layout_letter_list)
    lateinit var mLetterListLayout: RelativeLayout

    @BindView(R.id.layout_right)
    lateinit var mRightLayout: RelativeLayout

    @BindView(R.id.layout_error)
    lateinit var mErrorLayout: RelativeLayout

    @BindView(R.id.tv_word_error_tip)
    lateinit var mWordErrorTipTextView: TextView
    @BindView(R.id.toolbar)
    lateinit var toolbar: MainToolBar


    private var mLetterAdapter: ReadLetterItemClickAdapter? = null

    private var mWordInfoDatas: List<WordInfo>? = null

    private var mLetterDatas: MutableList<LetterInfo>? = null

    // 语音合成对象
    private var mTts: SpeechSynthesizer? = null

    // 缓冲进度
    private var mPercentForBuffering = 0

    // 播放进度
    private var mPercentForPlaying = 0

    private var unitId: String? = null

    lateinit var countDown: CountDown

    private var currentWordIndex: Int = 0

    private var currentRightWord: String? = null

    private var currentRightCnWord: String? = null

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
                //播放完成
                Glide.with(this@WordPracticeActivity).load(R.mipmap.read_word_default).into(mAudioPlayImageView!!)
            } else {
                stopSts()
            }
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        }
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

    override fun getLayoutId(): Int {
        return R.layout.read_activity_word_practice
    }

    override fun init() {

        val bundle = intent.extras
        if (bundle != null) {
            unitId = bundle.getString("unit_id")
        }

        mPresenter = ReadWordPresenter(this, this)

        toolbar.showNavigationIcon()
        toolbar.setTitle("")
        toolbar.showNavigationIcon()
        toolbar.init(this)
        toolbar.setRightContainerVisible(false)

        mTts = SpeechUtils.getTts(this)

        val layoutManager = GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false)
        mLetterRecyclerView.layoutManager = layoutManager
        mLetterRecyclerView.addItemDecoration(GridSpacingItemDecoration(5, resources.getDimensionPixelSize(R.dimen.t6dp), true))
        mLetterRecyclerView.setHasFixedSize(true)

        mLetterRecyclerView.layoutManager = layoutManager
        mLetterAdapter = ReadLetterItemClickAdapter(null)
        mLetterRecyclerView.adapter = mLetterAdapter
        mLetterAdapter?.setOnItemClickListener { adapter, view, position ->
            mWordInputTextView.text = "${mWordInputTextView.text}${mLetterDatas?.get(position)?.letterName}"
            if (!StringUtils.isEmpty(mWordInputTextView.text)) {
                mWordDeleteImageView.visibility = View.VISIBLE
            }
        }

        countDown = object : CountDown(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mWordCountDownTextView.text = toClock(millisUntilFinished)
            }

            override fun onFinish() {
                super.onFinish()
                if (currentRightWord == mWordInputTextView.text) {
                    mRightLayout.visibility = View.VISIBLE
                    mErrorLayout.visibility = View.INVISIBLE
                    mLetterListLayout.visibility = View.GONE
                    mWordInputTextView.setTextColor(ContextCompat.getColor(this@WordPracticeActivity, R.color.right_word_color))
                } else {
                    ToastUtils.showCenterToast(this@WordPracticeActivity, "你超时啦")
                    mWordErrorTipTextView.text = getString(R.string.word_time_out_error_text)
                    mRightLayout.visibility = View.INVISIBLE
                    mErrorLayout.visibility = View.VISIBLE
                    mLetterListLayout.visibility = View.GONE
                    mRightRemindTextView.text = currentRightWord
                    mWordInputTextView.setTextColor(ContextCompat.getColor(this@WordPracticeActivity, R.color.read_word_share_btn_color))
                    mErrorAgainButton.background = (DrawableUtils.getBgColor(this@WordPracticeActivity, 3, R.color.right_word_btn_again_color))
                }
            }
        }

        //播放
        RxView.clicks(mAudioPlayImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { startSynthesizer(currentRightWord) }

        //检查
        RxView.clicks(mCheckWordButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(Action1<Void> {
            if (StringUtils.isEmpty(mWordInputTextView.text)) {
                ToastUtils.showCenterToast(this@WordPracticeActivity, "请输入单词")
                return@Action1
            }

            if (currentRightWord?.replace(" ".toRegex(), "") == mWordInputTextView.text.toString().replace(" ".toRegex(), "")) {
                mRightLayout.visibility = View.VISIBLE
                mErrorLayout.visibility = View.INVISIBLE
                mLetterListLayout.visibility = View.GONE
                mWordInputTextView.setTextColor(ContextCompat.getColor(this@WordPracticeActivity, R.color.right_word_color))
            } else {
                mRightLayout.visibility = View.INVISIBLE
                mErrorLayout.visibility = View.VISIBLE
                mLetterListLayout.visibility = View.GONE
                mRightRemindTextView.text = currentRightWord
                mWordErrorTipTextView.text = getString(R.string.read_word_again_text)
                mWordInputTextView.setTextColor(ContextCompat.getColor(this@WordPracticeActivity, R.color.read_word_share_btn_color))
                mErrorAgainButton.background = (DrawableUtils.getBgColor(this@WordPracticeActivity, 3, R.color.right_word_btn_again_color))
            }
            //最后一个
            mWordInfoDatas?.let {
                if (currentWordIndex == mWordInfoDatas!!.size - 1) {
                    mErrorNextButton.text = getString(R.string.done)
                    mRightNextButton.text = getString(R.string.done)
                }
            }

        })

        //再做一遍
        RxView.clicks(mErrorAgainButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (currentWordIndex < mWordInfoDatas!!.size) {
                nextWord(currentWordIndex)
            } else {
                finish()
            }
        }

        //正确页面下一题
        RxView.clicks(mRightNextButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            currentWordIndex++
            if (currentWordIndex < mWordInfoDatas!!.size) {
                nextWord(currentWordIndex)
            } else {
                finish()
            }
        }

        //错误页面下一题
        RxView.clicks(mErrorNextButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            currentWordIndex++
            if (currentWordIndex < mWordInfoDatas!!.size) {
                nextWord(currentWordIndex)
            } else {
                finish()
            }
        }

        mPresenter.getWordListByUnitId(0, 0, unitId)
    }

    override fun hide() {
        mStateView.hide()
    }

    override fun showNoNet() {
        mStateView.showNoNet(mLayoutContext, "网络不给力") { mPresenter.getWordListByUnitId(0, 0, unitId) }
    }

    override fun showNoData() {
        mStateView.showNoData(mLayoutContext)
    }

    override fun showLoading() {
        mStateView.showLoading(mLayoutContext)
    }

    fun nextWord(wordPosition: Int) {
        mLetterListLayout.visibility = View.VISIBLE
        mRightLayout.visibility = View.INVISIBLE
        mErrorLayout.visibility = View.INVISIBLE
        mWordInputTextView.text = ""
        mWordInputTextView.setTextColor(ContextCompat.getColor(this, R.color.black_333))

        currentRightWord = mWordInfoDatas?.get(currentWordIndex)?.name
        currentRightCnWord = mWordInfoDatas?.get(currentWordIndex)?.means

        toolbar.setTitle((currentWordIndex + 1).toString() + "/" + mWordInfoDatas!!.size)
        mChineseWordTextView.text = currentRightCnWord
        randomLetterView()
        countDown.start()
    }

    fun startSynthesizer(text: String?) {

        Glide.with(this).load(R.mipmap.read_audio_gif_play).into(mAudioPlayImageView)
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

    @OnClick(R.id.iv_word_delete)
    fun deleteWordInput() {
        mWordInputTextView.text = ""
    }


    override fun onDestroy() {
        super.onDestroy()

        mTts?.stopSpeaking()
        // 退出时释放连接
        mTts?.destroy()
        mTts = null
    }

    override fun showWordListData(list: List<WordInfo>?) {

        if (list != null && list.isNotEmpty()) {
            mWordInfoDatas = list

            toolbar.setTitle((currentWordIndex + 1).toString() + "/" + list.size)

            currentRightWord = list[currentWordIndex].name
            currentRightCnWord = list[currentWordIndex].means
            mChineseWordTextView.text = currentRightCnWord

            randomLetterView()
        }
    }

    fun randomLetterView() {
        if (!StringUtils.isEmpty(currentRightWord)) {
            val randomStr = randomLetters(currentRightWord!!)
            mLetterDatas = ArrayList()

            for (i in 0 until randomStr.length) {
                val letterInfo = LetterInfo(LetterInfo.CLICK_ITEM_VIEW)
                letterInfo.letterName = randomStr[i] + ""
                mLetterDatas?.add(letterInfo)
            }

            mLetterAdapter?.setNewData(mLetterDatas)

            countDown.start()

            startSynthesizer(currentRightWord)
        } else {
            ToastUtils.showCenterToast(this@WordPracticeActivity, "数据异常，请稍后重试")
        }
    }


    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun onPause() {
        super.onPause()
        stopSts()
    }


    /**
     * 根据指定单词随机产生字母字符串
     *
     * @param oldStr
     * @return
     */
    fun randomLetters(oldStr: String): String {

        val letterStr = "abcdefghijklmnopqrstuvwxyz"


        val ts = TreeSet<String>()
        val len = oldStr.length
        for (i in 0 until len) {
            ts.add(oldStr[i] + "")
        }

        val i = ts.iterator()
        val sb = StringBuilder()
        while (i.hasNext()) {
            sb.append(i.next())
        }

        while (true) {
            if (sb.length >= 15) {
                break
            }
            val l = letterStr[Random().nextInt(26)]

            if (sb.toString().indexOf(l) == -1) {
                sb.append(l + "")
            }
        }

        val result = StringBuilder()
        if (sb.isNotEmpty()) {
            while (true) {
                if (result.length >= 15) {
                    break
                }
                val m = sb[Random().nextInt(sb.length)]
                if (result.toString().indexOf(m) == -1) {
                    result.append(m + "")
                }
            }
        }

        return result.toString()
    }


}
