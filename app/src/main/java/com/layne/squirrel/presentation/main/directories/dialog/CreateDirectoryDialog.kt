package com.layne.squirrel.presentation.main.directories.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.layne.squirrel.R
import com.layne.squirrel.framework.getValue

class CreateDirectoryDialog : DialogFragment() {

	companion object {
		fun build(block: (String) -> Unit) = CreateDirectoryDialog()
			.setOnPositiveButtonClickListener(block)
	}

	private var positiveButtonClickListener: (String) -> Unit = {}

	fun setOnPositiveButtonClickListener(l: (String) -> Unit): CreateDirectoryDialog {
		positiveButtonClickListener = l
		return this
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val dialogView = View.inflate(context, R.layout.dialog_newdir, null)
		val editText = dialogView?.findViewById(R.id.editTextDirName) as EditText

		return AlertDialog.Builder(context ?: throw NullPointerException())
			.setTitle(getString(R.string.new_dir))
			.setView(dialogView)
			.setIcon(R.drawable.ic_folder)
			.setPositiveButton(getString(R.string.validate)) { _, _ ->
				positiveButtonClickListener(editText.getValue())
			}
			.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
			.create()
	}
}