package speakap.rijksmuseum.ui

import android.os.Bundle
import android.support.v7.widget.Toolbar
import speakap.rijksmuseum.R
import speakap.rijksmuseum.commons.BaseActivity
import speakap.rijksmuseum.artobjectdetail.ArtObjectDetailFragment
import speakap.rijksmuseum.ui.artobjectslist.ArtObjectsFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(this.applicationContext != null)
        setContentView(R.layout.activity_main)

        findViewById<Toolbar>(R.id.mainToolbarForActivity).apply {
            setSupportActionBar(this)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(
                            R.id.activityMainFrameLayoutContainerView,
                            ArtObjectsFragment.newInstance()
                    )
                    .commit()
        }
    }

    fun openDetailPage(objectNumber: String) {
        supportFragmentManager.beginTransaction()
                .replace(
                        R.id.activityMainFrameLayoutContainerView,
                        ArtObjectDetailFragment.newInstance(objectNumber)
                )
                .addToBackStack(null)
                .commit()
    }

    fun openListingPage(period: Int) {
        supportFragmentManager.beginTransaction()
                .replace(
                        R.id.activityMainFrameLayoutContainerView,
                        ArtObjectsFragment.newInstance(period)
                )
                .addToBackStack(null)
                .commit()
    }
}
