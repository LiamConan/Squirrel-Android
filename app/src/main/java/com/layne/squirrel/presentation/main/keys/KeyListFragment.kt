package com.layne.squirrel.presentation.main.keys

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.layne.squirrel.R
import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.framework.*
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.presentation.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_list_key.*
import javax.inject.Inject

class KeyListFragment : Fragment() {

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<MainViewModel>({ requireActivity() }) { viewModelFactory }

	private lateinit var adapter: KeyPagerAdapter

	private var dirIndex = 0
	private var accountIndex = 0

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(R.layout.fragment_list_key, c, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Squirrel.dagger.inject(this)
		setHasOptionsMenu(true)

		val args: KeyListFragmentArgs by navArgs()
		dirIndex = args.directoryPosition
		accountIndex = args.accountPosition

		val keys = if (dirIndex !in model.data.directories.indices
			|| accountIndex !in model.data.directories[dirIndex].accounts.indices
		) {
			listOf(key { })
		} else {
			editTextTitle.setText(model.data.directories[dirIndex].accounts[accountIndex].title)
			model.data.directories[dirIndex].accounts[accountIndex].keys
		}


		adapter = KeyPagerAdapter(childFragmentManager, keys)
		pager.adapter = adapter

		runMainActivity {
			setFabOnClickListener { addKey() }
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		inflater.inflate(R.menu.keys, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.save -> {
				if (editTextTitle.isNotEmpty() && adapter.isFormsFilled()) {
					val account = Account(
						editTextTitle.getValue(),
						requireContext().now(),
						adapter.keys()
					)

					if (accountIndex == -1)
						model.addAccount(dirIndex, account)
					else
						model.updateAccount(dirIndex, accountIndex, account)

					requireActivity().onBackPressed()
				} else
					Toast.makeText(context, R.string.key_not_filled, Toast.LENGTH_LONG).show()
			}
		}

		return super.onOptionsItemSelected(item)
	}

	private fun addKey() {
		adapter.add()
		pager.offscreenPageLimit = adapter.count
		pageIndicatorView.count = pager.childCount
		pager.currentItem = pager.childCount - 1
	}

	fun deleteKey(position: Int) {
		if (adapter.count > 1) {
			model.deleteKey(dirIndex, accountIndex, position)
			adapter.remove(pager.currentItem)
			pageIndicatorView.count = pager.childCount
		} else {
			model.deleteAccount(dirIndex, accountIndex)
			requireActivity().onBackPressed()
		}
	}
}
