package speakap.rijksmueum.domain.datamodels.art

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebImg(
    val url: String? = null,
    val width: Int? = null,
    val height: Int? = null,
) : Parcelable
