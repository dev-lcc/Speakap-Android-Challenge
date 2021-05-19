package speakap.rijksmuseum.ui.artobjectslist

import speakap.rijksmuseum.commons.BasePresenter
import speakap.rijksmuseum.di.scopes.FragmentScope
import speakap.rijksmuseum.domain.ArtObject
import speakap.rijksmuseum.interactors.ArtObjectInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@FragmentScope
class ArtObjectsPresenter @Inject constructor(private val artObjectInteractor: ArtObjectInteractor) : BasePresenter<ArtObjectsView>() {


    fun onPageCreated(period: Int) {
        artObjectInteractor.getArtObject(1, period)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    fillList(it)
                }, {
                    showError()
                })
    }

    private fun showError() {
        ifViewAttached {
            it.showError()
        }
    }

    private fun fillList(artObjects: ArrayList<ArtObject>) {
        ifViewAttached {
            it.fillArtObjectsList(artObjects)
        }
    }

    fun loadMore(page: Int, period: Int) {
        artObjectInteractor.getArtObject(page, period)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    addToList(it)
                }, {
                    showError()
                })
    }

    private fun addToList(artObjects: ArrayList<ArtObject>) {
        ifViewAttached {
            it.addToArtObjectsList(artObjects)
        }
    }
}
