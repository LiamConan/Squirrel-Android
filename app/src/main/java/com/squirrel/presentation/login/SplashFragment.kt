package com.squirrel.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.squirrel.R

class SplashFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
			inflater.inflate(R.layout.fragment_splash, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		Thread {
			Thread.sleep(2000)
			val options = NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
			NavHostFragment.findNavController(this)
					.navigate(R.id.loginFragment, null, options)

		}.start()
	}
}