package speakap.rijksmuseum.ui.artobjectdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import reactivecircus.flowbinding.appcompat.navigationClicks
import reactivecircus.flowbinding.swiperefreshlayout.refreshes
import speakap.rijksmuseum.R
import speakap.rijksmuseum.commons.BaseFragment
import speakap.rijksmuseum.commons.utils.throttleFirst
import speakap.rijksmuseum.databinding.FragmentArtObjectDetailBinding
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ArtObjectDetailFragment : BaseFragment() {

    private val binding: FragmentArtObjectDetailBinding by viewBinding(
        FragmentArtObjectDetailBinding::bind
    )
    private val viewModel: ArtObjectDetailViewModel by viewModel {
        val objectNumber = if (arguments?.containsKey(KEY_OBJECT_NUMBER) == true) {
            requireArguments().getString(KEY_OBJECT_NUMBER, "")!!
        } else ""

        parametersOf(objectNumber)
    }

    private var errorGetArtObjectDetailSnackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(
            R.layout.fragment_art_object_detail, container, false
        )

    override fun onDestroyView() {
        super.onDestroyView()

        if (errorGetArtObjectDetailSnackbar?.isShown == true) {
            errorGetArtObjectDetailSnackbar?.dismiss()
        }
        errorGetArtObjectDetailSnackbar = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorGetArtObjectDetailSnackbar =
            Snackbar.make(
                view,
                R.string.something_went_wrong,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.btn_retry) {
                    viewModel.intent.refresh()
                }

        binding.toolbar.navigationClicks()
            .throttleFirst(1000L)
            .onEach {
                appNavController.popBackStack()
            }
            .launchIn(lifecycleScope)

        //
        // On Swipe Refresh
        //
        binding.swipeRefresh.refreshes()
            .onEach {
                // Attempt refresh
                viewModel.intent.refresh()
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

        binding.swipeRefresh.isRefreshing = viewState.isLoading

        // Appbar Title
        binding.collapsingToolbarLayout.title = viewState.title

        // Banner Image
        viewState.bannerImageUrl?.let { bannerImageUrl ->
            Glide.with(requireContext())
                .load(bannerImageUrl)
                .into(binding.fragmentDetailImageView)
        }

        // Maker
        binding.fragmentDetailTextViewMaker.text = viewState.principalMaker
        // Description
        binding.fragmentDetailTextViewDescription.text = viewState.description
        // Date
        binding.fragmentDetailTextViewDate.text = viewState.acquisitionDate
            ?.let(ART_DATE_FORMATTER::format)
        // Period Date
        binding.fragmentDetailTextViewPeriod.text = viewState.periodDate

    }

    private suspend fun takeSingleEvent(event: SingleEvent) {
        Timber.d("takeSingleEvent() -> event = $event")

        when (event) {
            is SingleEvent.ErrorGetArtObjectDetail -> {
                // DISPLAY Error Prompt
                errorGetArtObjectDetailSnackbar?.let { snackbar ->
                    with(snackbar) {
                        if (isShown) dismiss()
                        show()
                    }
                }
            }
            is SingleEvent.NavigateQueryByDatingPeriod -> {
                // TODO:: SingleEvent.NavigateQueryByDatingPeriod
            }
            is SingleEvent.NavigateQueryByInvolvedMaker -> {
                // TODO:: SingleEvent.NavigateQueryByInvolvedMaker
            }
        }
    }

    companion object {
        private const val KEY_OBJECT_NUMBER = "keyObjectNumber"

        private val ART_DATE_FORMATTER = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

        fun createInputArguments(
            objectNumber: String
        ): Bundle =
            bundleOf(
                KEY_OBJECT_NUMBER to objectNumber
            )
    }
}
