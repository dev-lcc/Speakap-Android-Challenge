package speakap.rijksmuseum.ui.artobjectquery

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import speakap.rijksmueum.domain.datamodels.arts.Dating

sealed class QueryType(open val query: String): Parcelable {

    @Parcelize
    data class ByInvolvedMaker(
        override val query: String,
    ): QueryType(query)

    @Parcelize
    data class ByDatingPeriod(
        val dating: Dating,
        ): QueryType(dating.presentingDate ?: "")

}
