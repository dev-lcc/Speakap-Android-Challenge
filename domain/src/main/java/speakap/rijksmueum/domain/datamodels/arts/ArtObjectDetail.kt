package speakap.rijksmueum.domain.datamodels.arts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtObjectDetail(
    val id: String? = null,
    val webImage: WebImg? = null,
    val principalMaker: String? = null,
    val label: Labels? = null,
    val acquisition: Acquisition? = null,
    val dating: Dating? = null
) : Parcelable {

    @Parcelize
    data class Acquisition(
        val method: String? = null,
        val date: String? = null,   // ISODate format ex. "1888-02-01T00:00:00"
        val creditLine: String? = null,
    ): Parcelable

}
