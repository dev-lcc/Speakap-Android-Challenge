package speakap.rijksmuseum.commons

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

abstract class BasePresenter<V : BaseView> : MvpBasePresenter<V>()
