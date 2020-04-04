package com.squirrel.framework

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics

class Squirrel : Application() {

	companion object {
		lateinit var dagger: Component
		lateinit var analytics: FirebaseAnalytics
	}

	override fun onCreate() {
		super.onCreate()

		analytics = FirebaseAnalytics.getInstance(this)
		dagger = DaggerComponent.builder()
			.useCasesModule(UseCasesModule(this))
			.build()
	}
}
