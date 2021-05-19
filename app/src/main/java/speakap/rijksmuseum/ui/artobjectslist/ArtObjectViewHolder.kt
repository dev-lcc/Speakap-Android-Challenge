package speakap.rijksmuseum.ui.artobjectslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.squareup.picasso.Picasso
import speakap.rijksmuseum.R
import speakap.rijksmuseum.domain.ArtObject

class ArtObjectViewHolder(
        val parent: ViewGroup,
        val picasso: Picasso,
        val onClick: (objectNumber: String) -> Unit
) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.item_art_object,
                        parent,
                        false
                )) {

    @BindView(R.id.itemArtObjectImageView)
    lateinit var imageViewArt: ImageView

    @BindView(R.id.itemArtObjectTextViewMaker)
    lateinit var textViewMaker: TextView

    @BindView(R.id.itemArtObjectTextViewDescription)
    lateinit var textViewDescription: TextView

    private var artObject: ArtObject? = null

    init {
        ButterKnife.bind(this, itemView)
    }

    fun bindTo(artObject: ArtObject?) {
        this.artObject = artObject

        artObject?.let {
            textViewMaker.text = it.principalOrFirstMaker
            textViewDescription.text = it.title

            val webImage = it.webImage
            picasso.load(webImage.url).into(imageViewArt)
        }

    }

    @OnClick(R.id.itemArtObjectContainer)
    fun onItemClicked() {
        onClick.invoke(artObject?.objectNumber ?: "")
    }


}
