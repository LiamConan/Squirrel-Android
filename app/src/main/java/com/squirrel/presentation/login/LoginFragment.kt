package com.squirrel.presentation.login

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.squirrel.R
import com.squirrel.framework.Squirrel
import com.squirrel.presentation.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    companion object {
        const val PERMISSION_STORAGE = 1
        const val OPEN_DOCUMENTE_CODE = 2
        const val CREATE_DOCUMENT_CODE = 3
    }

    private var model: LoginViewModel? = null

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
        i.inflate(R.layout.fragment_login, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = activity?.run {
            ViewModelProviders.of(this).get(LoginViewModel::class.java)
        }

        editTextFilePath.setOnClickListener {
            openFile()
        }

        editTextPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                unlock(editTextPassword.text.toString())
                true
            } else false
        }

        buttonLogin.setOnClickListener {
            unlock(editTextPassword.text.toString())
        }

        floatingActionButton.setOnClickListener {
            createFile()
        }

        imageButtonFingerprint.setOnClickListener { showBiometricPrompt() }

        model?.selectedPath?.observe(this, Observer {
            editTextFilePath.setText(model?.selectedPath?.value)

            model?.hasRegisteredPassword(model?.selectedPath?.value ?: "") {
                if (it) {
                    showBiometricPrompt()
                    imageButtonFingerprint.visibility = View.VISIBLE
                } else imageButtonFingerprint.visibility = View.GONE
            }
        })
    }

    override fun onRequestPermissionsResult(code: Int, perms: Array<String>, results: IntArray) {
        if (code == PERMISSION_STORAGE)
            openFile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        val path = resultData?.data?.path

        if (path != null && resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_DOCUMENTE_CODE -> {
                    editTextFilePath.setText(path)
                    model?.selectedPath?.value = resultData.data.toString()
                }
                CREATE_DOCUMENT_CODE -> {
                    editTextFilePath.setText(path)
                    model?.selectedPath?.value = resultData.data.toString()
                    model?.newFile = true
                    buttonLogin.setText(R.string.create_label)
                }
            }
        }
    }

    private fun openFile() {
        model?.newFile = false
        if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                PERMISSION_STORAGE
            )
    }

    private fun createFile() {
        if (activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                PERMISSION_STORAGE
            )
    }

    @TargetApi(Build.VERSION_CODES.P)
    private fun showBiometricPrompt() {
        activity?.let {
            BiometricPrompt.Builder(it)
                .setTitle(getText(R.string.biometric_title))
                .setSubtitle(getText(R.string.biometric_subtitle))
                .setDescription(getText((R.string.biometric_unlock_description)))
                .setNegativeButton(getText(R.string.biometric_cancel),
                    it.mainExecutor,
                    DialogInterface.OnClickListener { _, _ -> }).build()
                .authenticate(CancellationSignal(),
                    it.mainExecutor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                            super.onAuthenticationSucceeded(result)
                            model?.getRegisteredPassword(
                                model?.selectedPath?.value ?: ""
                            ) { password ->
                                unlock(password)
                            }
                        }
                    })
        }
    }

    private fun unlock(password: String) {
        model?.loadData(model?.selectedPath?.value, password, {
            editTextPassword.setText("")
            persistData(password)

            startActivity(
                Intent(activity, MainActivity::class.java)
                    .putExtra("json", Gson().toJson(it))
            )
        }, {
            Snackbar.make(container, R.string.bad_password, Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun persistData(password: String) {
        val uri = Uri.parse(model?.selectedPath?.value)
        context?.contentResolver?.takePersistableUriPermission(
            uri, FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
        )
        (activity?.application as Squirrel)
            .setUriAndPassword(uri, password)
    }
}
