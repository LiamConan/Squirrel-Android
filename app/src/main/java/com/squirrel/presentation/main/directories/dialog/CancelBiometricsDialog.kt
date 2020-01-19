package com.squirrel.presentation.main.directories.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.squirrel.R
import com.squirrel.framework.PasswordUseCases
import com.squirrel.framework.Squirrel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CancelBiometricsDialog : DialogFragment() {

	@Inject
	lateinit var passwordUseCases: PasswordUseCases

	init {
		Squirrel.dagger.inject(this)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return AlertDialog.Builder(context ?: throw NullPointerException())
			.setIcon(R.drawable.ic_fingerprint_black)
			.setTitle(getText(R.string.biometric_title))
			.setMessage(getString(R.string.cancel_biometrics_dialog_message))
			.setPositiveButton(R.string.cancel_biometrics_dialog_yes) { _, _ ->
				val uri = (activity?.application as Squirrel).uri.toString()
				GlobalScope.launch {
					passwordUseCases.deletePassword(uri)
				}
			}
			.setNegativeButton(R.string.cancel_biometrics_dialog_no, null)
			.create()
	}
}