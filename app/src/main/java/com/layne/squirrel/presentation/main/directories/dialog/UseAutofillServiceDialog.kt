package com.layne.squirrel.presentation.main.directories.dialog

import android.annotation.TargetApi
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.layne.squirrel.R
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.presentation.main.MainViewModel
import javax.inject.Inject

@TargetApi(Build.VERSION_CODES.O)
class UseAutofillServiceDialog : DialogFragment() {

	companion object {

		const val ACTION_REQUEST_AUTOFILL = 0

		fun build(block: () -> Unit) = UseAutofillServiceDialog()
			.setOnPositiveButtonClickListener(block)
	}

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<MainViewModel>({ requireActivity() }) { viewModelFactory }

	private var positiveButtonClickListener: () -> Unit = {}

	init {
		Squirrel.dagger.inject(this)
	}

	fun setOnPositiveButtonClickListener(l: () -> Unit): UseAutofillServiceDialog {
		positiveButtonClickListener = l
		return this
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return AlertDialog.Builder(context ?: throw NullPointerException())
			.setIcon(R.drawable.ic_key)
			.setTitle(getText(R.string.autofill_dialog_title))
			.setMessage(getString(R.string.autofill_dialog_message))
			.setPositiveButton(R.string.autofill_dialog_positive) { _, _ -> positiveButtonClickListener() }
			.setNeutralButton(R.string.autofill_dialog_neutral) { _, _ -> model.setAutofillNeverAskAgain() }
			.setNegativeButton(R.string.autofill_dialog_negative) { _, _ -> model.setAutofillJustAsked() }
			.create()
	}
}