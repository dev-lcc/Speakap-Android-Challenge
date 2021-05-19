package speakap.rijksmuseum.interactors

import speakap.rijksmuseum.data.RijksmuseumInternet
import speakap.rijksmuseum.di.scopes.FragmentScope
import speakap.rijksmuseum.domain.ArtObject
import speakap.rijksmuseum.domain.ArtObjectDetail
import io.reactivex.Single
import javax.inject.Inject

@FragmentScope
class ArtObjectInteractor @Inject constructor(private val rijksmuseumApi: RijksmuseumInternet) : BaseInteractor() {


    fun getArtObject(pages: Int, period: Int): Single<ArrayList<ArtObject>> {

        var periodParam: Int? = null
        if (period != -1) {
            periodParam = period
        }

        return rijksmuseumApi.getArtObjects(pages, periodParam)
                .map { it.artObjects }
    }

    fun getObjectsDetail(objectNumber: String): Single<ArtObjectDetail> {
        return rijksmuseumApi.getArtObjectDetail(objectNumber)
                .map { it.artObject }
    }

}
