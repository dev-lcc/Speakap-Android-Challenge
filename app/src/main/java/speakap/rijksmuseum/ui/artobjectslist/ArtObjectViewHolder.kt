package speakap.rijksmuseum.ui.artobjectslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import speakap.rijksmuseum.R
import speakap.rijksmueum.domain.datamodels.art.ArtObject

class ArtObjectViewHolder(
    val parent: ViewGroup,
    val onClick: (objectNumber: String) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(
            R.layout.item_art_object,
            parent,
            false
        )
) {

    /*@BindView(R.id.itemArtObjectImageView)*/
    lateinit var imageViewArt: ImageView

    /*@BindView(R.id.itemArtObjectTextViewMaker)*/
    lateinit var textViewMaker: TextView

    /*@BindView(R.id.itemArtObjectTextViewDescription)*/
    lateinit var textViewDescription: TextView

    private var artObject: speakap.rijksmueum.domain.datamodels.art.ArtObject? = null

    fun bindTo(artObject: speakap.rijksmueum.domain.datamodels.art.ArtObject?) {
        this.artObject = artObject

        artObject?.let {
            textViewMaker.text = it.principalOrFirstMaker
            textViewDescription.text = it.title

            val webImage = it.webImage
//            picasso.load(webImage.url).into(imageViewArt)
        }

    }

    /*@OnClick(R.id.itemArtObjectContainer)*/
    fun onItemClicked() {
        onClick.invoke(artObject?.objectNumber ?: "")
    }


}
