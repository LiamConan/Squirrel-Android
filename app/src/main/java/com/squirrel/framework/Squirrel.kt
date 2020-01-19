package com.squirrel.framework

import android.app.Application
import android.net.Uri

class Squirrel : Application() {

	companion object {
		lateinit var dagger: Component
	}

	var uri: Uri? = null
	var password: String? = null

	override fun onCreate() {
		super.onCreate()

		dagger = DaggerComponent.builder()
			.useCasesModule(UseCasesModule(this))
			.build()
	}

	fun setUriAndPassword(uri: Uri, password: String) {
		this.uri = uri
		this.password = password
	}
}
