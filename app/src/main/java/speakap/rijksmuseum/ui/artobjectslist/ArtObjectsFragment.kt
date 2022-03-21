package speakap.rijksmuseum.ui.artobjectslist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import speakap.rijksmuseum.R
import speakap.rijksmuseum.commons.BaseFragment
import speakap.rijksmuseum.commons.utils.EndlessRecyclerViewScrollListener

class ArtObjectsFragment : BaseFragment() {

    private lateinit var artObjectsAdapter: ArtObjectAdapter

//    override fun layoutId(): Int = R.layout.fragment_art_objects

    private var period = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        period = arguments?.getInt(KEY_PERIOD, -1) ?: -1
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        artObjectsAdapter = ArtObjectAdapter(
//            picasso = picasso,
            onClick = {
//                if (activity is MainActivity) {
//                    (activity as MainActivity).openDetailPage(it)
//                }
            }
        )
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        objects.layoutManager = layoutManager
//        objects.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//        objects.setHasFixedSize(true)
//        objects.adapter = artObjectsAdapter
//
//        objects.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
////                getPresenter().loadMore(page + 1, period)
//            }
//        })
    }

//    override fun fillArtObjectsList(artObjects: ArrayList<ArtObject>) {
//        artObjects.addAll(0, artObjectsAdapter.data)
//        artObjectsAdapter.addData(artObjects)
//    }
//
//    override fun addToArtObjectsList(artObjects: ArrayList<ArtObject>) {
//        artObjects.addAll(0, artObjectsAdapter.data)
//        artObjectsAdapter.addData(artObjects)
//    }

    companion object {

        private const val KEY_PERIOD = "keyPeriod"

        fun newInstance(
            period: Int = -1
        ): ArtObjectsFragment = ArtObjectsFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_PERIOD, period)
            }
        }
    }
}
