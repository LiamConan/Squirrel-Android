package com.layne.squirrel.presentation.main.directories.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.layne.squirrel.R
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.presentation.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import javax.inject.Inject

class CancelBiometricsDialog : DialogFragment(), CoroutineScope by MainScope() {

	companion object {
		fun build() = CancelBiometricsDialog()
	}

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<MainViewModel>({ requireActivity() }) { viewModelFactory }

	init {
		Squirrel.dagger.inject(this)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
		AlertDialog.Builder(context ?: throw NullPointerException())
			.setIcon(R.drawable.ic_fingerprint)
			.setTitle(getText(R.string.biometric_title))
			.setMessage(getString(R.string.cancel_biometrics_dialog_message))
			.setPositiveButton(R.string.cancel_biometrics_dialog_yes) { _, _ -> model.disableBiometrics() }
			.setNegativeButton(R.string.cancel_biometrics_dialog_no, null)
			.create()
}