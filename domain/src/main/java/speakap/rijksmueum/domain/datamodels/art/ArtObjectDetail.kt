package speakap.rijksmueum.domain.datamodels.art

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtObjectDetail(
    val id: String? = null,
    val webImage: WebImg? = null,
    val label: Labels? = null,
    val dating: Dating? = null
) : Parcelable
