package yc.com.homework.base.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.animation.*
import android.widget.TextView

import yc.com.homework.R

/**
 * Created by wanglin  on 2018/11/20 13:41.
 *
 *
 * 动画合集
 */
object AnimationUtil {

    /**
     * 开始水波纹动画
     */
    fun startRippleAnimation(fromX: Float, toX: Float, fromY: Float, toY: Float, fromAlpha: Float, toAlpha: Float, duration: Int, repeatCount: Int): AnimationSet {
        val `as` = AnimationSet(true)
        //缩放动画，以中心从原始放大到1.4倍
        val scaleAnimation = ScaleAnimation(fromX, toX, fromY, toY,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
        //渐变动画
        val alphaAnimation = AlphaAnimation(fromAlpha, toAlpha)
        scaleAnimation.duration = duration.toLong()
        scaleAnimation.repeatCount = repeatCount
        alphaAnimation.duration = duration.toLong()
        alphaAnimation.repeatCount = repeatCount
        `as`.duration = duration.toLong()
        `as`.addAnimation(scaleAnimation)
        `as`.addAnimation(alphaAnimation)

        return `as`

    }


    /**
     * 启动抖动动画
     *
     * @param context 上下文
     * @param view    开启动画的view ，一般用于文本框 EditText
     */

    fun startShakeAnim(context: Context, view: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.shake)
        view.startAnimation(animation)
    }

    private var objectAnimator: ValueAnimator? = null
    fun startTextAnim(text: String?, textView: TextView) {
        if (TextUtils.isEmpty(text)) return
        clearAnim()
        objectAnimator = ValueAnimator.ofInt(0, text!!.toInt())
        val interpolator = LinearInterpolator()
        objectAnimator!!.interpolator = interpolator
        objectAnimator!!.duration = 800
        objectAnimator!!.addUpdateListener { animation -> textView.text = animation?.animatedValue.toString() }

        objectAnimator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                val trim = textView.text.toString().trim()
                if (!TextUtils.isEmpty(trim)) {
                    textView.text = trim
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        objectAnimator!!.start()
    }

    private fun clearAnim() {
        if (objectAnimator != null) {
            objectAnimator!!.cancel()
            objectAnimator = null
        }
    }


}
