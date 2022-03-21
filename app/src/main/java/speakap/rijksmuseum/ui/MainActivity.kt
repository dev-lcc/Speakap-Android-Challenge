package speakap.rijksmuseum.ui

import android.os.Bundle
import speakap.rijksmuseum.R
import speakap.rijksmuseum.commons.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(this.applicationContext != null)
        setContentView(R.layout.activity_main)
    }
}
