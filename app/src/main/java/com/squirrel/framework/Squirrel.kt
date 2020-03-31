package com.squirrel.framework

import android.app.Application
import android.net.Uri
import com.google.firebase.analytics.FirebaseAnalytics

class Squirrel : Application() {

	companion object {
		lateinit var dagger: Component
		lateinit var analytics: FirebaseAnalytics
	}

	var uri: Uri? = null
	var password: String? = null

	override fun onCreate() {
		super.onCreate()

		analytics = FirebaseAnalytics.getInstance(this)
		dagger = DaggerComponent.builder()
			.useCasesModule(UseCasesModule(this))
			.build()
	}

	fun setUriAndPassword(uri: Uri, password: String) {
		this.uri = uri
		this.password = password
	}
}
