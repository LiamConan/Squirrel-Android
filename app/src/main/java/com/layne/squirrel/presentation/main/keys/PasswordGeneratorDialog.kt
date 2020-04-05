package com.layne.squirrel.presentation.main.keys

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.layne.squirrel.R
import com.layne.squirrel.framework.PasswordUseCases
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.presentation.main.MainViewModel
import javax.inject.Inject

class PasswordGeneratorDialog : DialogFragment() {

	@Inject
	lateinit var useCases: PasswordUseCases

	private var model: MainViewModel? = null
	private var editTextCaracNumber: EditText? = null
	private var checkBoxLowercase: CheckBox? = null
	private var checkBoxUppercase: CheckBox? = null
	private var checkBoxNumbers: CheckBox? = null
	private var checkBoxSpaces: CheckBox? = null
	private var checkBoxSpecials: CheckBox? = null
	private var buttonGenerate: Button? = null
	private var editTextPassword: EditText? = null
	private var positiveClickListener: (String) -> Unit = {}

	init {
		Squirrel.dagger.inject(this)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val view = View.inflate(context, R.layout.dialog_password_generator, null)

		model = ViewModelProvider(this).get(MainViewModel::class.java)

		editTextCaracNumber = view.findViewById(R.id.editTextCaracNumber)
		checkBoxLowercase = view.findViewById(R.id.checkBoxLowercase)
		checkBoxUppercase = view.findViewById(R.id.checkBoxUppercase)
		checkBoxNumbers = view.findViewById(R.id.checkBoxNumbers)
		checkBoxSpaces = view.findViewById(R.id.checkBoxSpaces)
		checkBoxLowercase = view.findViewById(R.id.checkBoxLowercase)
		checkBoxSpecials = view.findViewById(R.id.checkBoxSpecials)
		buttonGenerate = view.findViewById(R.id.buttonGenerate)
		editTextPassword = view.findViewById(R.id.editTextPassword)
		return AlertDialog.Builder(context ?: throw NullPointerException())
			.setView(view)
			.setTitle(R.string.generatePassword)
			.setPositiveButton(R.string.validate) { _, _ ->
				positiveClickListener(editTextPassword?.text.toString())
			}
			.setNegativeButton(R.string.cancel) { _, _ -> }
			.create()
	}

	override fun onStart() {
		super.onStart()

		buttonGenerate?.setOnClickListener {
			if (isFormValid())
				editTextPassword?.setText(
					useCases.generatePassword(
						editTextCaracNumber?.text.toString().toInt(),
						checkBoxLowercase?.isChecked == true,
						checkBoxUppercase?.isChecked == true,
						checkBoxNumbers?.isChecked == true,
						checkBoxSpaces?.isChecked == true,
						checkBoxSpecials?.isChecked == true
					)
				)
			else
				showFormErrors()
		}
	}

	fun setOnPositiveButtonClickListener(l: (String) -> Unit): PasswordGeneratorDialog {
		positiveClickListener = l
		return this
	}

	private fun isFormValid() =
		editTextCaracNumber?.text?.isNotEmpty() == true
				&& (checkBoxLowercase?.isChecked == true
				|| checkBoxUppercase?.isChecked == true
				|| checkBoxNumbers?.isChecked == true
				|| checkBoxSpaces?.isChecked == true
				|| checkBoxSpecials?.isChecked == true)
				&& editTextCaracNumber?.text.toString().toInt() > 0
				&& editTextCaracNumber?.text.toString().toInt() < 1024

	private fun showFormErrors() {
		if (editTextCaracNumber?.text?.isEmpty() == true) {
			editTextPassword?.error = getText(R.string.mustFillNumberCarac)
		} else if (checkBoxLowercase?.isChecked == false
			&& checkBoxUppercase?.isChecked == false
			&& checkBoxNumbers?.isChecked == false
			&& checkBoxSpaces?.isChecked == false
			&& checkBoxSpecials?.isChecked == false
		) {
			editTextPassword?.error = getText(R.string.mustChooseOneOption)
		} else {
			val n = editTextCaracNumber?.text.toString().toInt()
			if (n <= 0 || n > 1024)
				editTextPassword?.error = getText(R.string.wrong_password_length)
		}
	}
}