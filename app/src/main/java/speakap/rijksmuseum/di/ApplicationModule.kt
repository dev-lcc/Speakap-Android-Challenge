package speakap.rijksmuseum.di

import android.app.Application
import speakap.rijksmuseum.App
import speakap.rijksmuseum.di.scopes.ActivityScope
import speakap.rijksmuseum.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module(includes = [(AndroidInjectionModule::class)])
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun application(app: App): Application

    @ActivityScope
    @ContributesAndroidInjector(modules = [(MainActivityModol::class)])
    abstract fun mainActivity(): MainActivity

}
