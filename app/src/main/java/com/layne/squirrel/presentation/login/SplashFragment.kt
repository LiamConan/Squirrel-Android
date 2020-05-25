package com.layne.squirrel.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.layne.squirrel.R

class SplashFragment : Fragment() {

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(R.layout.fragment_splash, c, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		Thread {
			Thread.sleep(2000)
			val options = NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
			findNavController().navigate(R.id.loginFragment, null, options)

		}.start()
	}
}