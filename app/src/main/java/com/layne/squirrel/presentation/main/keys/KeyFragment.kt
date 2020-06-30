package com.layne.squirrel.presentation.main.keys

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter.POSITION_NONE
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.google.gson.Gson
import com.layne.squirrel.R
import com.layne.squirrel.core.domain.Key
import com.layne.squirrel.databinding.FragmentKeyBinding
import com.layne.squirrel.framework.*
import kotlinx.android.synthetic.main.fragment_key.*

class KeyFragment : Fragment() {

	companion object {

		private const val ARG_KEY_ARG = "key"
		private const val ARG_POSITION = "position"

		fun getInstance(key: Key, position: Int): KeyFragment =
			fragmentOf(
				bundleOf(
					ARG_KEY_ARG to Gson().toJson(key),
					ARG_POSITION to position
				)
			)
	}

	var position: Int = -1

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View? =
		FragmentKeyBinding.inflate(i, c, false).apply {
			key = Gson().fromJson(requireArguments().getString(ARG_KEY_ARG), Key::class.java)
			position = requireArguments().getInt(ARG_POSITION, POSITION_NONE)
		}.root

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Squirrel.dagger.inject(this)

		buttonCopyUsername.setOnClickListener { copyToClipboard(editTextUsername.getValue()) }

		buttonCopyEmail.setOnClickListener { copyToClipboard(editTextEmail.getValue()) }

		buttonKey.setOnClickListener {
			show(PasswordGeneratorDialog.build {
				editTextPassword.setText(it)
			})
		}

		buttonCopyPassword.setOnClickListener { copyToClipboard(editTextPassword.getValue()) }

		buttonGotoUrl.setOnClickListener {
			val url = editTextUrl.getUrl()
			if (url.isEmpty())
				Snackbar.make(container, getText(R.string.empty_url), LENGTH_SHORT).show()
			else {
				try {
					startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
				} catch (e: ActivityNotFoundException) {
					Snackbar.make(container, getText(R.string.malformed_url), LENGTH_SHORT).show()
				}
			}
		}

		buttonDelete.setOnClickListener { (parentFragment as KeyListFragment).deleteKey(position) }
	}

	fun getKeyFromUI() = Key(
		editTextUsername.getValue(),
		editTextEmail.getValue(),
		editTextPassword.getValue(),
		editTextUrl.getValue(),
		editTextNote.getValue()
	)

	fun isFormFilled() = (editTextUsername.isNotEmpty()
			|| editTextEmail.isNotEmpty())
			&& editTextPassword.isNotEmpty()
}
