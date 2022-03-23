package speakap.rijksmuseum.ui.artobjectquery

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import speakap.rijksmueum.domain.datamodels.arts.ArtObject
import speakap.rijksmuseum.databinding.ItemArtObjectBinding
import speakap.rijksmuseum.databinding.ItemListLoadMoreBinding

typealias OnTapArtObject = ((ArtObject) -> Unit)?

class ArtObjectAdapter(
    val onTapArtObject: OnTapArtObject? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<ArtObject>()
    private var loadingNextPage: Boolean = false

    override fun getItemCount(): Int = when (loadingNextPage) {
        true -> items.size + 1
        false -> items.size
    }

    /**
     * Each time data is set, we update this variable so that if DiffUtil calculation returns
     * after repetitive updates, we can ignore the old calculation
     */
    private var dataVersion = 0

    @Suppress("DEPRECATION")
    @SuppressLint("StaticFieldLeak")
    @MainThread
    fun replace(update: List<ArtObject>) {
        dataVersion++
        if (items.isEmpty()) {
            items = update
            notifyDataSetChanged()
        } else if (update.isEmpty()) {
            val oldSize = items.size
            items = listOf()
            notifyItemRangeRemoved(0, oldSize)
        } else {
            val startVersion = dataVersion
            val oldItems = ArrayList(items)

            object : AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                override fun doInBackground(vararg voids: Void): DiffUtil.DiffResult {
                    return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                        override fun getOldListSize(): Int = oldItems.size
                        override fun getNewListSize(): Int = update.size

                        override fun areItemsTheSame(
                            oldItemPosition: Int,
                            newItemPosition: Int
                        ): Boolean {
                            val oldItem = oldItems[oldItemPosition]
                            val newItem = update[newItemPosition]
                            return oldItem.id == newItem.id
                        }

                        override fun areContentsTheSame(
                            oldItemPosition: Int,
                            newItemPosition: Int
                        ): Boolean {
                            val oldItem = oldItems[oldItemPosition]
                            val newItem = update[newItemPosition]
                            return oldItem == newItem
                        }
                    })
                }

                override fun onPostExecute(diffResult: DiffUtil.DiffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return
                    }
                    items = update
                    diffResult.dispatchUpdatesTo(this@ArtObjectAdapter)
                }
            }
                .execute()
        }
    }

    fun setLoading(isLoading: Boolean) {
        val prevState = loadingNextPage
        loadingNextPage = isLoading

        try {
            if (!prevState && isLoading) {
                notifyItemInserted(items.size)
            } else if (!isLoading) {
                notifyItemRemoved(items.size)
            }
        } catch (_: Throwable) {
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (loadingNextPage) {
            true -> {
                if (position < items.size) VIEW_TYPE_ITEM
                else VIEW_TYPE_LOAD_MORE
            }
            false -> VIEW_TYPE_ITEM
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_ITEM -> ArtObjectViewHolder(
                binding = ItemArtObjectBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onTapArtObject = onTapArtObject,
            )
            VIEW_TYPE_LOAD_MORE -> LoadMoreViewHolder(
                ItemListLoadMoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
            else -> object : RecyclerView.ViewHolder(parent) {}
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is LoadMoreViewHolder -> {
            }
            is ArtObjectViewHolder -> {
                position.takeIf { it < items.size } ?: return

                val item = items[position]
                holder.bindTo(item)
            }
        }
    }

    inner class ArtObjectViewHolder(
        private val binding: ItemArtObjectBinding,
        private val onTapArtObject: OnTapArtObject? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(artObject: ArtObject) {
            binding.itemArtObjectTextViewMaker.text = artObject.principalOrFirstMaker
            binding.itemArtObjectTextViewDescription.text = artObject.title

            // Load Art Object Image
            artObject.webImage?.url?.let { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .into(binding.itemArtObjectImageView)
            }

            // On-tap item
            binding.root.setOnClickListener { onTapArtObject?.invoke(artObject) }
        }

    }

    inner class LoadMoreViewHolder(
        binding: ItemListLoadMoreBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_ITEM = 0x00
        private const val VIEW_TYPE_LOAD_MORE = 0x01
    }

}
