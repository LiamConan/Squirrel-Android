package com.layne.squirrel.framework

import android.annotation.TargetApi
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.layne.squirrel.R
import com.layne.squirrel.presentation.main.MainActivity
import com.layne.squirrel.presentation.main.directories.dialog.UseAutofillServiceDialog
import com.layne.squirrel.presentation.main.keys.KeyFragment
import kotlinx.android.synthetic.main.fragment_list_account.*

fun AppCompatActivity.findNavController(id: Int): NavController =
	(supportFragmentManager.findFragmentById(id) as NavHostFragment).navController

fun fragmentOf(bundle: Bundle): KeyFragment {
	val fragment = KeyFragment()
	fragment.arguments = bundle
	return fragment
}

inline fun <reified T> Fragment.withArgs(bundle: Bundle): T {
	this.arguments = bundle
	return this as T
}

fun Fragment.runActivity(block: FragmentActivity.() -> Unit) {
	(activity as FragmentActivity).run(block)
}

fun Fragment.runMainActivity(block: MainActivity.() -> Unit) {
	(activity as MainActivity).run(block)
}

fun FragmentActivity.show(dialog: DialogFragment) {
	val tag = dialog::class.java.canonicalName
	val ft = supportFragmentManager.beginTransaction()
	val prev = supportFragmentManager.findFragmentByTag(tag)
	prev?.also { ft.remove(it) }
	ft.addToBackStack(null)

	dialog.show(ft, tag)
}

fun Fragment.show(dialog: DialogFragment) {
	runActivity { show(dialog) }
}

fun TextView.setAfterTextChangeListener(block: (String) -> Unit) {
	addTextChangedListener(object : TextWatcher {
		override fun afterTextChanged(s: Editable?) {
			block(s.toString())
		}

		override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
		override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
	})
}

fun EditText.isNotEmpty() = text.isNotEmpty()

fun EditText.getValue(): String = text.toString()

@RequiresApi(Build.VERSION_CODES.O)
fun Activity.requestAutofillService() {
	val uri: Uri = Uri.parse("package:$packageName")
	val i = Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE, uri)
	startActivityForResult(i, UseAutofillServiceDialog.ACTION_REQUEST_AUTOFILL)
}

fun Menu.addWithOnClick(stringId: Int, block: (MenuItem) -> Unit) {
	val item = add(stringId)
	item.setOnMenuItemClickListener {
		block(it)
		true
	}
}

fun Fragment.copyToClipboard(value: String) {
	runActivity {
		(application as Squirrel).clipboard?.let {
			it.setPrimaryClip(ClipData.newPlainText("copied", value))
			Snackbar.make(container, getString(R.string.showkeys_copied), Snackbar.LENGTH_SHORT)
				.show()
		}
	}
}

fun EditText.getUrl(): String {
	val url = getValue()

	return if (!url.contains("http://"))
		"http://$url"
	else url
}

@TargetApi(Build.VERSION_CODES.P)
fun Fragment.showBiometricPrompt(block: (BiometricPrompt.AuthenticationResult?) -> Unit) {
	runActivity {
		if (FingerprintManagerCompat.from(this).hasEnrolledFingerprints()) {
			BiometricPrompt.Builder(context)
				.setTitle(getText(R.string.biometric_title))
				.setSubtitle(getText(R.string.biometric_subtitle))
				.setDescription(getText((R.string.biometric_activation_description)))
				.setNegativeButton(getText(R.string.biometric_cancel),
					mainExecutor, { _, _ -> })
				.build()
				.authenticate(CancellationSignal(),
					mainExecutor,
					object : BiometricPrompt.AuthenticationCallback() {
						override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
							super.onAuthenticationSucceeded(result)
							block(result)
						}
					})
		} else
			Snackbar.make(
				container, getText(R.string.no_fingerprint_detected),
				Snackbar.LENGTH_LONG
			)
				.show()
	}
}