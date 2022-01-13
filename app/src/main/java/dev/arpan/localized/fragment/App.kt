package dev.arpan.localized.fragment

import android.app.Application
import android.content.Context
import android.content.res.Resources
import java.util.*

// forces local to be in english
class App : Application() {
    private var baseCtx: Context? = null
    private var appCtx: Context? = null
    private var baseRes: Resources? = null

    override fun attachBaseContext(base: Context) {
        val ctx = baseCtx ?: base.createLocalizedContext(Locale.ENGLISH).also {
            baseCtx = it
        }
        super.attachBaseContext(ctx)
    }

    override fun getApplicationContext(): Context {
        return appCtx ?: super.getApplicationContext().createLocalizedContext(Locale.ENGLISH).also {
            appCtx = it
        }
    }

    override fun getResources(): Resources {
        return baseRes ?: baseContext.createLocalizedContext(Locale.ENGLISH).resources.also {
            baseRes = it
        }
    }
}