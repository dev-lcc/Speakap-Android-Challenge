package speakap.rijksmueum.domain.datamodels.arts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtObject(
    val id: String? = null,
    val title: String? = null,
    val principalOrFirstMaker: String? = null,
    val webImage: WebImg? = null,
    val objectNumber: String? = null,
) : Parcelable
