package com.squirrel.presentation.main.keys

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.google.gson.Gson
import com.squirrel.R
import com.squirrel.core.domain.Key
import com.squirrel.databinding.FragmentKeyBinding
import com.squirrel.framework.fragmentOf
import com.squirrel.framework.getValue
import com.squirrel.presentation.main.keys.KeysFragment.Companion.COPIED
import com.squirrel.presentation.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_key.*

class KeyFragment : Fragment() {

	companion object {
		fun getInstance(key: Key, dirIndex: Int, accountIndex: Int): KeyFragment =
			fragmentOf(
				bundleOf(
					"item_key" to Gson().toJson(key),
					"dir_index" to dirIndex,
					"account_index" to accountIndex
				)
			)
	}

	private var binding: FragmentKeyBinding? = null
	private var model: MainViewModel? = null
	var tag: Int = -1
	private var clipboard: ClipboardManager? = null

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View? {
		binding = DataBindingUtil.inflate(i, R.layout.fragment_key, c, false)
				as FragmentKeyBinding
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		model = activity?.run {
			ViewModelProviders.of(this).get(MainViewModel::class.java)
		}

		val key = Gson().fromJson(arguments?.getString("item_key"), Key::class.java)
		binding?.key = key
		clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

		buttonCopyUsername.setOnClickListener {
			if (editTextUsername.text?.isNotEmpty() == true) {
				clipboard?.run {
					setPrimaryClip(ClipData.newPlainText(COPIED, editTextUsername.text))
				}
				Snackbar.make(container, getText(R.string.showkey_copied), LENGTH_SHORT)
					.show()
			}
		}

		buttonCopyEmail.setOnClickListener {
			if (editTextEmail.text?.isNotEmpty() == true) {
				clipboard?.run {
					setPrimaryClip(ClipData.newPlainText(COPIED, editTextEmail.text))
				}
				Snackbar.make(container, getText(R.string.showkey_copied), LENGTH_SHORT).show()
			}
		}

		buttonKey.setOnClickListener {
			showPasswordDialog {
				editTextPassword.setText(it)
			}
		}

		buttonCopyPassword.setOnClickListener {
			if (editTextPassword.text?.isNotEmpty() == true) {
				clipboard?.run {
					setPrimaryClip(ClipData.newPlainText(COPIED, editTextPassword.text))
				}
				Snackbar.make(container, getText(R.string.showkey_copied), LENGTH_SHORT).show()
			}
		}

		buttonCopyUrl.setOnClickListener {
			var address = editTextUrl.text.toString()
			if (address == "")
				Snackbar.make(container, getText(R.string.empty_url), LENGTH_SHORT).show()
			else {
				if (!address.contains("http://"))
					address = "http://$address"

				try {
					val intent = Intent(Intent.ACTION_VIEW)
					intent.data = Uri.parse(address)
					startActivity(intent)
				} catch (e: ActivityNotFoundException) {
					Snackbar.make(
						container,
						getText(R.string.malformed_url),
						LENGTH_SHORT
					).show()
				}
			}
		}

		buttonDelete.setOnClickListener {
			model?.deleteSubkey(
				arguments?.getInt("dir_index") ?: 0,
				arguments?.getInt("account_index") ?: 0
			)
		}
	}

	private fun showPasswordDialog(l: (String) -> Unit) {
		activity?.supportFragmentManager?.let {
			PasswordGeneratorDialog().setOnPositiveButtonClickListener(l).show(it, "tag")
		}
	}

	fun getSubkeyFromUI() = Key(
		editTextUsername.getValue(),
		editTextEmail.getValue(),
		editTextPassword.getValue(),
		editTextUrl.getValue(),
		editTextNote.getValue()
	)

	fun fieldsFilled() = (editTextUsername.text?.isNotEmpty() == true
			|| editTextEmail.text?.isNotEmpty() == true)
			&& editTextPassword.text?.isNotEmpty() == true
}
