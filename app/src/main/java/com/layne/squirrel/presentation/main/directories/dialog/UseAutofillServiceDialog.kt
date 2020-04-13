package com.layne.squirrel.presentation.main.directories.dialog

import android.annotation.TargetApi
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.layne.squirrel.R
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.show
import com.layne.squirrel.presentation.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

@TargetApi(Build.VERSION_CODES.O)
class UseAutofillServiceDialog(private val onPositiveClick: () -> Unit) : DialogFragment(),
	CoroutineScope by MainScope() {

	companion object {
		const val ACTION_REQUEST_AUTOFILL = 0
	}

	private var model: MainViewModel? = null

	init {
		Squirrel.dagger.inject(this)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		model = activity?.run {
			ViewModelProvider(this).get(MainViewModel::class.java)
		}

		return AlertDialog.Builder(context ?: throw NullPointerException())
			.setIcon(R.drawable.ic_key)
			.setTitle(getText(R.string.autofill_dialog_title))
			.setMessage(getString(R.string.autofill_dialog_message))
			.setPositiveButton(R.string.autofill_dialog_positive) { _, _ -> onPositiveClick() }
			.setNeutralButton(R.string.autofill_dialog_neutral) { _, _ -> model?.setAutofillNeverAskAgain() }
			.setNegativeButton(R.string.autofill_dialog_negative) { _, _ -> model?.setAutofillJustAsked() }
			.create()
	}

	fun show(activity: FragmentActivity) {
		this.show("autofillDialog", activity)
	}
}