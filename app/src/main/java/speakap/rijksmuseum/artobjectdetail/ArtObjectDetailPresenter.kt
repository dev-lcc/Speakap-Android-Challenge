package speakap.rijksmuseum.artobjectdetail

import speakap.rijksmuseum.commons.BasePresenter
import speakap.rijksmuseum.di.scopes.FragmentScope
import speakap.rijksmuseum.domain.ArtObjectDetail
import speakap.rijksmuseum.interactors.ArtObjectInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@FragmentScope
class ArtObjectDetailPresenter @Inject constructor(private val artObjectInteractor: ArtObjectInteractor) : BasePresenter<ArtObjectDetailView>() {

    fun onViewCreated(objectNumber: String) {
        if (objectNumber.isNotEmpty()) {
            artObjectInteractor.getObjectsDetail(objectNumber)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        fillPage(it)
                    },{ showError() })
        }
    }

    private fun showError() {
        ifViewAttached {
            it.showError()
        }
    }

    private fun fillPage(artObjectDetail: ArtObjectDetail) {
        ifViewAttached {
            it.setTitels(artObjectDetail.label.title)
            it.setMaker(artObjectDetail.label.makerLine)
            it.setDescriptionChars(artObjectDetail.label.description)
            it.showJpeg(artObjectDetail.webImage.url)
            it.setDate(artObjectDetail.dating.presentingDate)
            it.setPeriod(artObjectDetail.dating.period)
        }
    }

}
