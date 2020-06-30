package com.layne.squirrel.presentation.main.keys

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.layne.squirrel.R
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.presentation.main.MainViewModel
import javax.inject.Inject

class PasswordGeneratorDialog : DialogFragment() {

	companion object {

		const val MIN_PASSWORD_SIZE = 1
		const val MAX_PASSWORD_SIZE = 1024

		fun build(block: (String) -> Unit) = PasswordGeneratorDialog()
			.setOnPositiveButtonClickListener(block)
	}

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<MainViewModel>({ requireActivity() }) { viewModelFactory }

	private var numberPicker: NumberPicker? = null
	private var checkBoxLowercase: CheckBox? = null
	private var checkBoxUppercase: CheckBox? = null
	private var checkBoxNumbers: CheckBox? = null
	private var checkBoxSpaces: CheckBox? = null
	private var checkBoxSpecials: CheckBox? = null
	private var buttonGenerate: Button? = null
	private var editTextPassword: EditText? = null
	private var positiveClickListener: (String) -> Unit = {}

	fun setOnPositiveButtonClickListener(l: (String) -> Unit): PasswordGeneratorDialog {
		positiveClickListener = l
		return this
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		Squirrel.dagger.inject(this)
		val view = View.inflate(context, R.layout.dialog_password_generator, null)

		numberPicker = view.findViewById(R.id.numberPicker)
		numberPicker?.minValue = MIN_PASSWORD_SIZE
		numberPicker?.maxValue = MAX_PASSWORD_SIZE
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
			editTextPassword?.error = null
			if (isFormValid())
				editTextPassword?.setText(
					model.generatePassword(
						numberPicker?.value ?: 1,
						checkBoxLowercase?.isChecked == true,
						checkBoxUppercase?.isChecked == true,
						checkBoxNumbers?.isChecked == true,
						checkBoxSpaces?.isChecked == true,
						checkBoxSpecials?.isChecked == true
					)
				)
			else editTextPassword?.error = getText(R.string.mustChooseOneOption)
		}
	}

	private fun isFormValid() = checkBoxLowercase?.isChecked == true
			|| checkBoxUppercase?.isChecked == true
			|| checkBoxNumbers?.isChecked == true
			|| checkBoxSpaces?.isChecked == true
			|| checkBoxSpecials?.isChecked == true
}