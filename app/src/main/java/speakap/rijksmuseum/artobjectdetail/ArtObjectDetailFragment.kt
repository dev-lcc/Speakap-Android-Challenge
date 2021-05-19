package speakap.rijksmuseum.artobjectdetail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.squareup.picasso.Picasso
import speakap.rijksmuseum.R
import speakap.rijksmuseum.commons.BaseFragment
import speakap.rijksmuseum.ui.MainActivity
import javax.inject.Inject

class ArtObjectDetailFragment : BaseFragment<ArtObjectDetailView, ArtObjectDetailPresenter>(), ArtObjectDetailView {

    @BindView(R.id.fragmentDetailTextViewMaker)
    lateinit var textViewMaker: TextView

    @BindView(R.id.fragmentDetailTextViewDate)
    lateinit var textViewDate: TextView

    @BindView(R.id.fragmentDetailTextViewDescription)
    lateinit var textViewDescription: TextView

    @BindView(R.id.fragmentDetailImageView)
    lateinit var imageViewArt: ImageView

    @BindView(R.id.fragmentDetailTextViewPeriod)
    lateinit var textViewPeriod: TextView

    @Inject
    lateinit var picasso: Picasso

    private var period = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        objectNumber = arguments!!.getString(KEY_OBJECT_NUMBER) ?: ""
    }

    private var objectNumber = ""

    override fun layoutId(): Int = R.layout.fragment_art_object_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPresenter().onViewCreated(objectNumber)
    }

    override fun setTitels(title: String) {
    }

    override fun setMaker(makerLine: String) {
        textViewMaker.text = makerLine
    }

    override fun setDescriptionChars(description: String) {
        textViewDescription.text = description
    }

    override fun showJpeg(url: String) {
        picasso.load(url).into(imageViewArt)
    }

    override fun setDate(presentingDate: String) {
        textViewDate.text = presentingDate
    }

    override fun setPeriod(period: Int) {
        this.period = period
        textViewPeriod.text = "$period"
    }

    companion object {
        private const val KEY_OBJECT_NUMBER = "keyObjectNumber"

        fun newInstance(objectNumber: String): ArtObjectDetailFragment {
            return ArtObjectDetailFragment().apply {
                arguments = Bundle().apply {
                    this.putString(KEY_OBJECT_NUMBER, objectNumber)
                }
            }
        }
    }

    @OnClick(R.id.fragmentDetailTextViewPeriod)
    fun onPeriodClicked() {
        (activity as? MainActivity)!!.openListingPage(period)
    }
}
