package speakap.rijksmuseum.ui.artobjectquery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
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
import org.koin.core.parameter.parametersOf
import reactivecircus.flowbinding.appcompat.navigationClicks
import reactivecircus.flowbinding.swiperefreshlayout.refreshes
import speakap.rijksmueum.domain.datamodels.arts.ArtObject
import speakap.rijksmueum.domain.datamodels.arts.Dating
import speakap.rijksmuseum.R
import speakap.rijksmuseum.commons.BaseFragment
import speakap.rijksmuseum.commons.utils.EndlessRecyclerViewScrollListener
import speakap.rijksmuseum.commons.utils.throttleFirst
import speakap.rijksmuseum.databinding.FragmentArtObjectQueryBinding
import speakap.rijksmuseum.ui.artobjectdetail.ArtObjectDetailFragment
import timber.log.Timber

class ArtObjectQueryFragment : BaseFragment() {

    private val binding: FragmentArtObjectQueryBinding by viewBinding(FragmentArtObjectQueryBinding::bind)
    private val viewModel: ArtObjectQueryViewModel by viewModel {
        val queryType = if (arguments?.containsKey(KEY_QUERY_TYPE) == true) {
            requireArguments().getParcelable<QueryType>(KEY_QUERY_TYPE)!!
        } else {
            QueryType.ByDatingPeriod(
                dating = Dating(
                    presentingDate = "",
                    period = 0
                )
            )
        }

        parametersOf(queryType)
    }

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
            R.layout.fragment_art_object_query, container, false
        )

    override fun onDestroyView() {
        super.onDestroyView()

        if (scrollListener != null) {
            binding.fragmentArtObjectsRecyclerView.removeOnScrollListener(scrollListener!!)
        }
        scrollListener = null

        artObjectsAdapter = null

        if (snackbarErrorFetchNextPage?.isShown == true) {
            snackbarErrorFetchNextPage?.dismiss()
        }
        snackbarErrorFetchNextPage = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                if (appNavController.currentDestination?.id == R.id.fragmentArtObjectQuery) {
                    appNavController.navigate(
                        R.id.action_fragmentArtObjectQuery_to_fragmentArtObjectDetail,
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

        binding.toolbar.navigationClicks()
            .throttleFirst(1000L)
            .onEach {
                appNavController.popBackStack()
            }
            .launchIn(lifecycleScope)

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

        // Query Type
        when (viewState.queryType) {
            is QueryType.ByInvolvedMaker -> {
                binding.toolbar.title = "\"${viewState.queryType.query}\""
                with(binding.chipQueryType) {
                    setChipBackgroundColorResource(R.color.colorAccent)
                    text = getString(R.string.art_objects_query_by_involved_maker)
                }
            }
            is QueryType.ByDatingPeriod -> {
                binding.toolbar.title = "\"${viewState.queryType.dating.presentingDate ?: ""}\""
                with(binding.chipQueryType) {
                    setChipBackgroundColorResource(R.color.colorAccentSecondary)
                    text = getString(R.string.art_objects_query_by_dating_period)
                }
            }
        }

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

    companion object {
        private val KEY_QUERY_TYPE = "key-query-type"

        fun createInputArguments(
            queryType: QueryType,
        ): Bundle =
            bundleOf(
                KEY_QUERY_TYPE to queryType
            )

    }

}