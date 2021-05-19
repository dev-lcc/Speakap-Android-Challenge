package speakap.rijksmuseum.di

import android.app.Activity
import android.content.Context
import speakap.rijksmuseum.di.scopes.ActivityScope
import speakap.rijksmuseum.di.scopes.FragmentScope
import speakap.rijksmuseum.ui.MainActivity
import speakap.rijksmuseum.artobjectdetail.ArtObjectDetailFragment
import speakap.rijksmuseum.ui.artobjectslist.ArtObjectsFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModol {

    @Binds
    @ActivityScope
    abstract fun activityContext(activity: Activity): Context

    @Binds
    @ActivityScope
    abstract fun activity(mainActivity: MainActivity): Activity

    @FragmentScope
    @ContributesAndroidInjector(modules = [(BaseFragmentModule::class)])
    abstract fun artObjectsFragment(): ArtObjectsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [(BaseFragmentModule::class)])
    abstract fun artObjectDetailFragment(): ArtObjectDetailFragment
}
