package com.github.paolorotolo.appintro

import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.FontRes
import com.github.paolorotolo.appintro.model.SliderPage

class AppIntro3Fragment : AppIntroBaseFragment() {

    override val layoutId: Int get() = R.layout.appintro_fragment_intro3

    companion object {

        /**
         * Generates a new instance for [AppIntro3Fragment]
         *
         * @param title                CharSequence which will be the slide title
         * @param description          CharSequence which will be the slide description
         * @param titleTypefaceFontRes @FontRes (Integer) custom title typeface obtained
         *                             from Resources
         * @param descTypefaceFontRes  @FontRes (Integer) custom description typeface obtained
         *                             from Resources
         * @param imageDrawable        @DrawableRes (Integer) the image that will be
         *                             displayed, obtained from Resources
         * @param bgColor              @ColorInt (Integer) custom background color
         * @param titleColor           @ColorInt (Integer) custom title color
         * @param descColor            @ColorInt (Integer) custom description color
         *
         * @return An [AppIntro3Fragment] created instance
         */
        @JvmOverloads
        @JvmStatic
        fun newInstance(
                title: CharSequence? = null,
                description: CharSequence? = null,
                @FontRes titleTypefaceFontRes: Int = 0,
                @FontRes descTypefaceFontRes: Int = 0,
                @DrawableRes imageDrawable: Int = 0,
                @ColorInt bgColor: Int = 0,
                @ColorInt titleColor: Int = 0,
                @ColorInt descColor: Int = 0
        ): AppIntro3Fragment {
            return newInstance(
                    SliderPage(
                            title = title,
                            description = description,
                            imageDrawable = imageDrawable,
                            bgColor = bgColor,
                            titleColor = titleColor,
                            descColor = descColor,
                            titleTypefaceFontRes = titleTypefaceFontRes,
                            descTypefaceFontRes = descTypefaceFontRes
                    )
            )
        }

        /**
         * Generates an [AppIntro3Fragment] from a given [SliderPage]
         *
         * @param sliderPage the [SliderPage] object which contains all attributes for
         * the current slide
         *
         * @return An [AppIntro3Fragment] created instance
         */
        @JvmStatic
        fun newInstance(sliderPage: SliderPage): AppIntro3Fragment {
            val slide = AppIntro3Fragment()
            slide.arguments = sliderPage.toBundle()
            return slide
        }
    }
}
