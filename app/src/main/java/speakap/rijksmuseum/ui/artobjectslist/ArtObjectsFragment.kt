package speakap.rijksmuseum.ui.artobjectslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.swiperefreshlayout.refreshes
import speakap.rijksmueum.domain.datamodels.arts.ArtObject
import speakap.rijksmuseum.R
import speakap.rijksmuseum.commons.BaseFragment
import speakap.rijksmuseum.commons.utils.EndlessRecyclerViewScrollListener
import speakap.rijksmuseum.databinding.FragmentArtObjectsBinding
import speakap.rijksmuseum.ui.artobjectdetail.ArtObjectDetailFragment
import timber.log.Timber

class ArtObjectsFragment : BaseFragment() {

    private val binding: FragmentArtObjectsBinding by viewBinding(FragmentArtObjectsBinding::bind)
    private val viewModel: ArtObjectsViewModel by viewModel()

    private var artObjectsAdapter: ArtObjectAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    private val attemptNextPageFlow = MutableSharedFlow<Int>(extraBufferCapacity = 64)

    private var snackbarErrorFetchNextPage: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(
            R.layout.fragment_art_objects, container, false
        )

    override fun onDestroyView() {
        super.onDestroyView()

        if (scrollListener != null) {
            binding.fragmentArtObjectsRecyclerView.removeOnScrollListener(scrollListener!!)
        }
        scrollListener = null

        artObjectsAdapter = null

        if(snackbarErrorFetchNextPage?.isShown == true) {
            snackbarErrorFetchNextPage?.dismiss()
        }
        snackbarErrorFetchNextPage = null
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        snackbarErrorFetchNextPage = Snackbar.make(
            view,
            R.string.something_went_wrong,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.btn_retry) {
                // Retry attempt fetch next page
                viewModel.intent.fetchNextPage()
            }

        artObjectsAdapter = ArtObjectAdapter(
            onTapArtObject = { item: ArtObject ->
                Timber.d("onTapArtObject() -> item = $item")

                val objectNumber = item.objectNumber ?: return@ArtObjectAdapter
                if (appNavController.currentDestination?.id == R.id.fragmentArtObjects) {
                    appNavController.navigate(
                        R.id.action_fragmentArtObjects_to_fragmentArtObjectDetail,
                        ArtObjectDetailFragment.createInputArguments(objectNumber = objectNumber)
                    )
                }

            }
        )
        val rvLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        with(binding.fragmentArtObjectsRecyclerView) {
            layoutManager = rvLayoutManager
            setHasFixedSize(true)
            adapter = artObjectsAdapter
        }

        scrollListener = object : EndlessRecyclerViewScrollListener(rvLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                attemptNextPageFlow.tryEmit(page)
            }
        }

        binding.swipeRefresh.refreshes()
            .onEach {
                // Attempt refresh
                viewModel.intent.refresh()
            }
            .launchIn(lifecycleScope)

        attemptNextPageFlow
            .debounce(350)
            .onEach { page ->
                Timber.d("attemptNextPageFlow() -> page = $page")
                viewModel.intent.fetchNextPage()
            }
            .launchIn(lifecycleScope)

        ////////////////////////////////////
        // Observe ViewState
        ////////////////////////////////////
        viewModel.viewState
            .onEach(::render)
            .launchIn(lifecycleScope)

        ////////////////////////////////////
        // Observe Single Event(s)
        ////////////////////////////////////
        viewModel.singleEvent
            .onEach(::takeSingleEvent)
            .launchIn(lifecycleScope)

    }

    private suspend fun render(viewState: ViewState) {
        Timber.d("render() -> viewState = $viewState")

        // Progress Indicator
        binding.swipeRefresh.isRefreshing = viewState.isLoading
        artObjectsAdapter?.setLoading(viewState.isLoadingNextPage)

        if (!viewState.isEmpty) {
            artObjectsAdapter?.replace(viewState.artObjects)

            binding.fragmentArtObjectsRecyclerView.clearOnScrollListeners()
            if (viewState.hasMoreToFetch) {
                if (scrollListener != null) {
                    binding.fragmentArtObjectsRecyclerView.addOnScrollListener(scrollListener!!)
                }
            }

            binding.actvEmptyStateInfo.visibility = View.GONE
            binding.fragmentArtObjectsRecyclerView.visibility = View.VISIBLE
        } else {
            binding.fragmentArtObjectsRecyclerView.visibility = View.GONE
            binding.actvEmptyStateInfo.visibility = View.VISIBLE
        }

    }

    private suspend fun takeSingleEvent(event: SingleEvent) {
        Timber.d("takeSingleEvent() -> event = $event")

        when (event) {
            is SingleEvent.ErrorGetArtObjectCollections -> {
                // DISPLAY Error Prompt
                Toast.makeText(
                    requireContext(),
                    R.string.something_went_wrong,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is SingleEvent.ErrorGetArtObjectCollectionsNextPage -> {
                // DISPLAY Error Snackbar prompt
                snackbarErrorFetchNextPage ?: return
                snackbarErrorFetchNextPage?.let {
                    with(it) {
                        if (isShown) dismiss()
                        show()
                    }
                }
            }
        }
    }

}
