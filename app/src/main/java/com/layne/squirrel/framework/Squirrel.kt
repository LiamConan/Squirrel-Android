package com.layne.squirrel.framework

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.layne.squirrel.core.domain.DataHolder
import com.layne.squirrel.framework.di.Component
import com.layne.squirrel.framework.di.ContextModule
import com.layne.squirrel.framework.di.DaggerComponent
import com.layne.squirrel.framework.di.InteractorModule

class Squirrel : Application() {

	companion object {
		lateinit var dagger: Component
		lateinit var analytics: FirebaseAnalytics
	}

	var dataHolder: DataHolder = dataHolder { }
	var clipboard: ClipboardManager? = null

	override fun onCreate() {
		super.onCreate()

		analytics = FirebaseAnalytics.getInstance(this)
		dagger = DaggerComponent.builder()
			.contextModule(ContextModule(this))
			.interactorModule(InteractorModule(this))
			.build()
		clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
	}
}
