package speakap.rijksmueum.domain.datamodels.art

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtObjectDetail(
    val id: String,
    val webImage: WebImg,
    val label: Labels,
    val dating: Dating
) : Parcelable
