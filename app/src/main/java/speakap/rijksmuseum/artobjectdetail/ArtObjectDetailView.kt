package speakap.rijksmuseum.artobjectdetail

import speakap.rijksmuseum.commons.BaseView

interface ArtObjectDetailView : BaseView {
    fun setTitels(title: String)
    fun setMaker(makerLine: String)
    fun setDescriptionChars(description: String)
    fun showJpeg(url: String)
    fun setDate(presentingDate: String)
    fun setPeriod(period: Int)
}
