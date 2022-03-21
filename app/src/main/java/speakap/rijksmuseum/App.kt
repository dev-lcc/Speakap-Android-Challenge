package speakap.rijksmuseum

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init Koin Dependency here...
        startKoin {
            androidContext(applicationContext)
        }

        Timber.plant(Timber.DebugTree())

    }

    companion object {
        private val TAG = App::class.java.simpleName
    }
}
