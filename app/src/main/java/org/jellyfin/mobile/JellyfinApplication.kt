package org.jellyfin.mobile

import android.app.Application
import android.webkit.WebView
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class JellyfinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // Setup logging
            Timber.plant(Timber.DebugTree())

            // Enable WebView debugging
            WebView.setWebContentsDebuggingEnabled(true)
        }

        startKoin {
            androidContext(this@JellyfinApplication)
            modules(applicationModule)
        }
    }
}
