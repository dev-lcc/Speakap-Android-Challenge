package speakap.rijksmuseum.ui.artobjectslist

import speakap.rijksmuseum.commons.BaseView
import speakap.rijksmuseum.domain.ArtObject

interface ArtObjectsView : BaseView {
    fun fillArtObjectsList(artObjects: ArrayList<ArtObject>)
    fun addToArtObjectsList(it: ArrayList<ArtObject>)
}
