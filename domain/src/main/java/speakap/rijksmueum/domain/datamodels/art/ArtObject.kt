package speakap.rijksmueum.domain.datamodels.art

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtObject(
    val id: String,
    val title: String,
    val principalOrFirstMaker: String,
    val webImage: WebImg,
    val objectNumber: String
) : Parcelable
