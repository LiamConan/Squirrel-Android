package com.squirrel.presentation.main.directories.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.squirrel.R

class DeleteDirectoryDialog : DialogFragment() {

	private var positiveButtonClickListener: () -> Unit = {}

	fun addPositiveButtonClick(l: () -> Unit): DeleteDirectoryDialog {
		positiveButtonClickListener = l
		return this
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return AlertDialog.Builder(context ?: throw NullPointerException())
			.setTitle(getString(R.string.delete_dir))
			.setIcon(R.drawable.ic_folder)
			.setMessage(R.string.confirm_directory_deleting)
			.setPositiveButton(getString(R.string.yes)) { _, _ ->
				positiveButtonClickListener()
			}
			.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
			.create()
	}
}