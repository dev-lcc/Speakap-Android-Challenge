package speakap.rijksmuseum

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import speakap.rijksmuseum.data.di.RepositoryModule
import speakap.rijksmuseum.data.di.UseCaseModule
import speakap.rijksmuseum.di.ViewModelModules
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init Koin Dependency here...
        startKoin {
            androidContext(applicationContext)

            loadKoinModules(
                RepositoryModule(
                    restApiUrlEndpoint = BuildConfig.REST_API_URL_ENDPOINT,
                    apiKey = BuildConfig.API_KEY_RIJKSMUSUEM,
                    isDebug = BuildConfig.DEBUG,
                ).build()
            )
            loadKoinModules(ViewModelModules.build())
            loadKoinModules(
                listOf(
                    UseCaseModule.Arts.build(),
                )
            )
        }

        Timber.plant(Timber.DebugTree())

    }

    companion object {
        private val TAG = App::class.java.simpleName
    }
}
