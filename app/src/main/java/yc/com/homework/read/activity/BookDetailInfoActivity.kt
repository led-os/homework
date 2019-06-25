package yc.com.homework.read.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.LogUtil
import kotlinx.android.synthetic.main.activity_book_detail_info.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.mine.utils.ImageUtils
import yc.com.homework.read.adapter.BookDetailInfoAdapter
import yc.com.homework.read.domain.bean.BookUnitInfo
import yc.com.homework.read.fragment.BookDetailInfoFragment
import yc.com.homework.read.fragment.BookReadUnlockFragment
import yc.com.homework.read.utils.AVMediaManager
import yc.com.homework.read.utils.DataManager
import java.util.*
import java.util.concurrent.TimeUnit


/**
 *
 * Created by wanglin  on 2019/1/14 13:59.
 */
class BookDetailInfoActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>(), Observer {


    private var bookId: Int = 0
    private var page: Int = 0
    private var pageTotal: Int = 0
    private var pageIndex: Int = 0
    private var singleTotal: Int = 0

    private var fragmentList: ArrayList<Fragment>? = null
    private var canScrollCount: Int = 0//可滑动的页数
    private var bookUnitInfos: List<BookUnitInfo>? = null

    private var currentPos: Int = 0
    private var isClick = false


    override fun getLayoutId(): Int {
        return R.layout.activity_book_detail_info
    }

    override fun beforeInit() {
        StatusBarUtil.setStatusColor(R.color.app_selected_color)
    }

    override fun init() {
        if (intent != null) {
            bookId = intent.getIntExtra("bookId", 0)
            page = intent.getIntExtra("page", 0)
            pageTotal = intent.getIntExtra("pageTotal", 0)
            pageIndex = intent.getIntExtra("pageIndex", 0)
            singleTotal = intent.getIntExtra("singleTotal", 0)

            bookUnitInfos = intent.getParcelableArrayListExtra("classList")
            canScrollCount = pageIndex + singleTotal

        }

        ObservableManager.get().addMyObserver(this)
        initFragments()
        LogUtil.msg("bookId=$bookId  page=$page  pageTotal=$pageTotal  pageIndex=$pageIndex  singleTotal:$singleTotal  bookUnitInfos:${bookUnitInfos?.size}")



        // window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //保持屏幕常亮
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    private fun initFragments() {
        fragmentList = arrayListOf()

        pageTotal.let {
            for (i in 0..pageTotal) {
                val bookDetailInfoFragment = BookDetailInfoFragment()
                val bundle = Bundle()
                bundle.putInt("bookId", bookId)
                bundle.putInt("singleTotal", singleTotal)
                if (i == pageIndex)
                    bookDetailInfoFragment.setCurrentPage(page)

                bookDetailInfoFragment.arguments = bundle
                fragmentList?.add(bookDetailInfoFragment)
            }
        }
        viewPager.adapter = BookDetailInfoAdapter(fragmentList, supportFragmentManager)
        viewPager.setScrollable(true)
        viewPager.currentItem = pageIndex
        currentPos = pageIndex
//        if (singleTotal > 1) {
        viewPager.offscreenPageLimit = singleTotal
//        viewPager.offscreenPageLimit = 1
//        }
        initListener()

    }


    companion object {
        var isScroll = false
    }

    private var bookReadUnlockFragment: BookReadUnlockFragment? = null
    private fun initListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                isScroll = true

                if (currentPos < position) {//向后滑
                    if (page < pageTotal) {
                        page++
                    }

                } else {//向前滑
                    if (page > 0)
                        page--

                }
                var canSee: Int? = 0

                bookUnitInfos?.forEach {
                    if (it.page == page) {
                        canSee = it.can_see
                    }
                }
//                LogUtil.msg("position: $position  mPage: $page  pageIndex: $pageIndex  currentPos:$currentPos  scrollCount:$canScrollCount  cansee:$canSee")

//                if (nextCanSee == 1 || position < canScrollCount) {
                if (canSee == 1) {
                    val fragment = fragmentList?.get(position) as BookDetailInfoFragment
                    fragment.setCurrentPage(page)
                    currentPos = position
                } else {

                    if (bookReadUnlockFragment == null)
                        bookReadUnlockFragment = BookReadUnlockFragment()
                    if (!bookReadUnlockFragment!!.isVisible) {
                        bookReadUnlockFragment?.show(supportFragmentManager, "")
                    }
//
                    viewPager.currentItem = currentPos
                }

            }
        })

        RxView.clicks(iv_book_read_add).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

            isClick = !isClick

            if (isClick) {
                //1.添加蒙版
                addView()
            } else {
                hideView()
            }

        }

        RxView.clicks(iv_book_read_click).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //点读模式
            isClick = false
            hideView()

            ObservableManager.get().notifyMyObserver(false)

        }
        RxView.clicks(iv_book_read_succession).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //连读模式
            isClick = false
            hideView()
            iv_book_read_add.visibility = View.GONE
            tv_stop.visibility = View.VISIBLE
            fl_play_controller.visibility = View.VISIBLE
            viewPager.setScrollable(false)
            ObservableManager.get().notifyMyObserver(true)

        }
        RxView.clicks(flurView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            isClick = false
            hideView()
        }
        RxView.clicks(tv_stop).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //1.停止播放

            ObservableManager.get().notifyMyObserver("stop")

            hidePlayState()
        }
        RxView.clicks(flurView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            isClick = false
            hideView()
        }
        RxView.clicks(iv_state_play).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            ObservableManager.get().notifyMyObserver("start")
            iv_state_pause.visibility = View.VISIBLE
            iv_state_play.visibility = View.GONE
        }

        RxView.clicks(iv_state_pause).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            iv_state_play.visibility = View.VISIBLE
            iv_state_pause.visibility = View.GONE
            ObservableManager.get().notifyMyObserver("pause")
        }

    }


    fun hidePlayState() {
        //2.隐藏
        tv_stop.visibility = View.GONE
        fl_play_controller.visibility = View.GONE
        iv_book_read_add.visibility = View.VISIBLE
        iv_state_pause.visibility = View.VISIBLE
        iv_state_play.visibility = View.GONE
        viewPager.setScrollable(true)
    }

    private fun addView() {

        window.decorView.isDrawingCacheEnabled = true
        val bm = window.decorView.drawingCache

        flurView.visibility = View.VISIBLE
        val result = ImageUtils.blurBitmap(this, bm)

        flurView.setImageBitmap(result)
        window.decorView.isDrawingCacheEnabled = false

        //2.播放动画
        playAnimation()

    }


    private fun hideView() {
        flurView.visibility = View.GONE
        stopAnimation()
    }

    private fun playAnimation() {
        iv_book_read_click.animate().translationY(-160f).alpha(1f).setDuration(80).start()
        iv_book_read_succession.animate().translationX(160f).alpha(1f).setDuration(80).start()
        iv_book_read_add.setImageResource(R.mipmap.book_read_close)
    }

    private fun stopAnimation() {
        iv_book_read_click.animate().translationY(160f).alpha(0f).setDuration(80).start()
        iv_book_read_succession.animate().translationX(-160f).alpha(0f).setDuration(80).start()
        iv_book_read_add.setImageResource(R.mipmap.read_add_icon)
    }

    private var scrollPage = 1

    private fun setNextPage() {

        val index = if (isScroll) {
//            LogUtil.msg("position:    mPage: $page  pageIndex: $pageIndex  currentPos:$currentPos")
            currentPos + 1
        } else {
            pageIndex + 1
        }
        scrollPage++
        val fragment = fragmentList?.get(index) as BookDetailInfoFragment
        fragment.setCurrentPage(page + 1)
        viewPager.currentItem = index

    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg != null) {
            when (arg) {
                is String -> {
                    if (TextUtils.equals("next", arg)) {
                        setNextPage()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AVMediaManager.getInstance().releaseAudioManager()
        ObservableManager.get().deleteMyObserver(this)
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        DataManager.instance.clearDatas()
    }
}