package speakap.rijksmueum.domain.datamodels.art

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebImg(
    val url: String,
    val width: Int,
    val height: Int
) : Parcelable
