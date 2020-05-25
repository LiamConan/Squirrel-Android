package com.layne.squirrel.presentation.login

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.layne.squirrel.R
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.dataHolder
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.presentation.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment() {

	companion object {
		const val PERMISSION_STORAGE_CREATE = 1
		const val PERMISSION_STORAGE_OPEN = 2
		const val OPEN_DOCUMENTE_CODE = 1
		const val CREATE_DOCUMENT_CODE = 2
		const val MANUAL_LOGIN = "manual"
		const val BIOMETRICS_LOGIN = "biometrics"
	}

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<LoginViewModel> { viewModelFactory }

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(R.layout.fragment_login, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Squirrel.dagger.inject(this)

		editTextFilePath.setOnClickListener {
			openFile()
		}

		editTextPassword.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				unlock(editTextPassword.text.toString(), MANUAL_LOGIN)
				true
			} else false
		}

		buttonLogin.setOnClickListener {
			unlock(editTextPassword.text.toString(), MANUAL_LOGIN)
		}

		floatingActionButton.setOnClickListener {
			createFile()
		}

		imageButtonFingerprint.setOnClickListener { showBiometricPrompt() }
	}

	override fun onResume() {
		super.onResume()

		model.selectedPath.observe(requireActivity(), Observer { path ->
			editTextFilePath.setText(path)

			if (model.getFilePreferences().biometricsSaved) {
				imageButtonFingerprint.visibility = View.VISIBLE
				showBiometricPrompt()
			} else imageButtonFingerprint.visibility = View.INVISIBLE
		})
	}

	override fun onRequestPermissionsResult(code: Int, perms: Array<String>, results: IntArray) {
		if (code == PERMISSION_STORAGE_CREATE)
			createFile()
		else if (code == PERMISSION_STORAGE_OPEN)
			openFile()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
		super.onActivityResult(requestCode, resultCode, resultData)

		val path = resultData?.data?.path

		if (path != null && resultCode == Activity.RESULT_OK) {
			when (requestCode) {
				OPEN_DOCUMENTE_CODE -> {
					model.selectedPath.value = resultData.data.toString()
				}
				CREATE_DOCUMENT_CODE -> {
					model.selectedPath.value = resultData.data.toString()
					model.newFile = true
					buttonLogin.setText(R.string.create_label)
				}
			}
		}
	}

	private fun openFile() {
		model.newFile = false
		if (requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
				addCategory(Intent.CATEGORY_OPENABLE)
				type = "*/*"
			}
			startActivityForResult(
				intent,
				OPEN_DOCUMENTE_CODE
			)
		} else
			requestPermissions(
				arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
				PERMISSION_STORAGE_OPEN
			)
	}

	private fun createFile() {
		if (requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
				addCategory(Intent.CATEGORY_OPENABLE)
				type = "text/plain"
				putExtra(Intent.EXTRA_TITLE, "keys.sq")
			}
			startActivityForResult(
				intent,
				CREATE_DOCUMENT_CODE
			)
		} else
			requestPermissions(
				arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
				PERMISSION_STORAGE_CREATE
			)
	}

	@TargetApi(Build.VERSION_CODES.P)
	private fun showBiometricPrompt() {
		BiometricPrompt.Builder(requireContext())
			.setTitle(getText(R.string.biometric_title))
			.setSubtitle(getText(R.string.biometric_subtitle))
			.setDescription(getText((R.string.biometric_unlock_description)))
			.setNegativeButton(getText(R.string.biometric_cancel),
				requireActivity().mainExecutor,
				DialogInterface.OnClickListener { _, _ -> }).build()
			.authenticate(CancellationSignal(),
				requireActivity().mainExecutor,
				object : BiometricPrompt.AuthenticationCallback() {
					override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
						super.onAuthenticationSucceeded(result)
						unlock(model.getFilePreferences().password, BIOMETRICS_LOGIN)
					}
				})
	}

	private fun unlock(password: String, type: String) {
		model.loadData(model.selectedPath.value, password, {
			editTextPassword.setText("")
			requireContext().contentResolver.takePersistableUriPermission(
				Uri.parse(model.selectedPath.value),
				FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
			)

			Squirrel.analytics.logEvent(
				FirebaseAnalytics.Event.LOGIN, bundleOf(
					FirebaseAnalytics.Param.CONTENT_TYPE to type
				)
			)

			(requireActivity().application as Squirrel).dataHolder = dataHolder {
				data = it
				this.password = password
				uri = model.selectedPath.value ?: ""
			}

			startActivity(Intent(activity, MainActivity::class.java))
		}, {
			Snackbar.make(container, R.string.bad_password, Snackbar.LENGTH_SHORT).show()
		})
	}
}
