package dev.arpan.localized.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils
import java.util.*

fun Int.isDarkColor() = ColorUtils.calculateLuminance(this) < 0.35

fun Context.getThemeColorAttribute(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    val typedArray = obtainStyledAttributes(typedValue.data, intArrayOf(resId))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}


fun Context.createLocalizedConfiguration(locale: Locale): Configuration {
    val configuration = Configuration(resources.configuration)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList.getDefault().prepend(locale))
    } else {
        configuration.setLocale(locale)
    }
    return configuration
}

val Context.currentLocal: Locale
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0) ?: Locale.getDefault()
        } else {
            resources.configuration.locale
        }
    }

@RequiresApi(Build.VERSION_CODES.N)
private fun LocaleList.prepend(locale: Locale): LocaleList {
    val locales = mutableSetOf(locale)
    (0 until size()).forEach {
        locales.add(get(it))
    }
    return LocaleList(*locales.toTypedArray())
}

fun Context.createLocalizedContext(locale: Locale): Context {
    return createConfigurationContext(createLocalizedConfiguration(locale))
}
