package speakap.rijksmueum.domain.datamodels.arts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtObjectCollection(
    val count: Int? = null,
    val artObjects: List<ArtObject> = listOf(),
) : Parcelable
