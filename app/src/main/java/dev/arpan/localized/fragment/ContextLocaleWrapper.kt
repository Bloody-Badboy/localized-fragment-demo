package dev.arpan.localized.fragment

import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.content.res.Resources
import android.view.LayoutInflater
import java.util.*

class ContextLocaleWrapper(base: Context, private val locale: Locale) : ContextWrapper(base) {
    private var _inflater: LayoutInflater? = null
    private var _resources: Resources? = null
    private var _theme: Resources.Theme? = null

    override fun getResources(): Resources {
        return _resources ?: run {
            val baseRes = super.getResources()
            val resContext = createConfigurationContext(
                baseRes.createLocalizedConfiguration(locale)
            )
            return@run resContext.resources
        }.also {
            _resources = it
        }
    }

    override fun getSystemService(name: String): Any {
        if (LAYOUT_INFLATER_SERVICE == name) {
            return _inflater ?: run {
                LayoutInflater.from(baseContext).cloneInContext(this)
            }.also {
                _inflater = it
            }
        }
        return super.getSystemService(name)
    }

    override fun getTheme(): Resources.Theme {
        return _theme ?: resources.newTheme().apply {
            setTo(super.getTheme())
        }.also {
            _theme = it
        }
    }

    override fun getAssets(): AssetManager {
        return resources.assets
    }
}