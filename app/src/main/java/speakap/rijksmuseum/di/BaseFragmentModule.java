package speakap.rijksmuseum.di;

import android.support.v4.app.Fragment;

import speakap.rijksmuseum.commons.BaseFragment;
import speakap.rijksmuseum.di.scopes.FragmentScope;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class BaseFragmentModule {

    @Binds
    @FragmentScope
    abstract Fragment fragment(BaseFragment baseFragment);
}
