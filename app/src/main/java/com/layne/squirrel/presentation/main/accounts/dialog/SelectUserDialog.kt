package com.layne.squirrel.presentation.main.accounts.dialog

import android.R.layout.select_dialog_singlechoice
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.layne.squirrel.R
import com.layne.squirrel.framework.withArgs

class SelectUserDialog : DialogFragment() {

	companion object {

		private const val ARG_USERNAMES = "usernames"

		fun build(usernames: List<String>, block: (Int) -> Unit) = SelectUserDialog()
			.withArgs<SelectUserDialog>(bundleOf(ARG_USERNAMES to ArrayList(usernames)))
			.setOnPositiveButtonClickListener(block)
	}

	private var positiveButtonClickListener: (Int) -> Unit = {}

	fun setOnPositiveButtonClickListener(l: (Int) -> Unit): SelectUserDialog {
		positiveButtonClickListener = l
		return this
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val usernames: List<String> = requireArguments().getStringArrayList(ARG_USERNAMES)
			?: listOf()
		val arrayAdapter = ArrayAdapter(requireContext(), select_dialog_singlechoice, usernames)

		return AlertDialog.Builder(requireContext())
			.setTitle(getString(R.string.menu_showkeys_user))
			.setNegativeButton(getString(R.string.menu_showkeys_cancel)) { dialog, _ -> dialog.dismiss() }
			.setAdapter(arrayAdapter) { _, which ->
				positiveButtonClickListener(which)
			}
			.create()
	}
}