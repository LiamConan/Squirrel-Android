package com.layne.squirrel.presentation.main.accounts

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.google.firebase.analytics.FirebaseAnalytics
import com.layne.squirrel.R
import com.layne.squirrel.R.string.key_deleted
import com.layne.squirrel.core.domain.Account
import com.layne.squirrel.core.domain.Key
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.addWithOnClick
import com.layne.squirrel.framework.copyToClipboard
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.framework.show
import com.layne.squirrel.presentation.main.MainViewModel
import com.layne.squirrel.presentation.main.accounts.dialog.SelectUserDialog
import kotlinx.android.synthetic.main.fragment_list_account.*
import javax.inject.Inject

class AccountListFragment : Fragment() {

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<MainViewModel>({ requireActivity() }) { viewModelFactory }

	private var viewAdapter: AccountAdapter? = null
	private var dirIndex = -1

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(R.layout.fragment_list_account, c, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Squirrel.dagger.inject(this)

		val args: AccountListFragmentArgs by navArgs()
		dirIndex = args.directoryPosition

		val accounts = if (dirIndex !in model.data.directories.indices) {
			Squirrel.analytics.logEvent(
				FirebaseAnalytics.Event.VIEW_ITEM_LIST,
				bundleOf("dir_index" to dirIndex)
			)
			groupError.visibility = View.VISIBLE
			listOf()
		} else {
			model.data.directories[dirIndex].accounts
		}

		viewAdapter = AccountAdapter(accounts).apply {
			setOnItemClickListener {
				val action = AccountListFragmentDirections.openAccount(dirIndex, it)
				NavHostFragment.findNavController(this@AccountListFragment).navigate(action)
			}
			setOnCreateContextMenuListener { menu, account, position ->
				menu.addWithOnClick(R.string.menu_showkeys_copy_username) {
					selectUser(account) { copyToClipboard(it.username) }
				}
				menu.addWithOnClick(R.string.menu_showkeys_copy_email) {
					selectUser(account) { copyToClipboard(it.email) }
				}
				menu.addWithOnClick(R.string.menu_showkeys_copy_password) {
					selectUser(account) { copyToClipboard(it.password) }
				}
				menu.addWithOnClick(R.string.menu_showkeys_go_to_url) {
					selectUser(account) {
						try {
							startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
						} catch (e: ActivityNotFoundException) {
							Snackbar.make(container, getString(R.string.no_valid_url), LENGTH_SHORT)
								.show()
						}
					}
				}
				menu.addWithOnClick(R.string.menu_showkeys_delete) {
					model.deleteAccount(dirIndex, position)
				}
			}
			setOnItemMoveListener { from, to ->
				if (from < to) {
					for (i in from until to)
						model.swapAccounts(dirIndex, i, i + 1)
				} else {
					for (i in from downTo to + 1)
						model.swapAccounts(dirIndex, i, i - 1)
				}
			}
			setOnItemDismissListener { position ->
				val account = model.data.directories[dirIndex].accounts[position]
				Snackbar.make(container, getString(key_deleted, account.title), LENGTH_LONG)
					.setAction(R.string.cancel) {
						model.addAccount(dirIndex, account, position)
						viewAdapter?.notifyItemInserted(position)
						viewAdapter?.notifyItemRangeChanged(position, itemCount)
					}.show()

				model.deleteAccount(dirIndex, position)
			}
		}

		recyclerView.apply {
			setHasFixedSize(true)
			layoutManager = LinearLayoutManager(context)
			adapter = viewAdapter
		}
	}

	private fun selectUser(account: Account, block: (Key) -> Unit) {
		if (account.keys.size > 1) {
			show(SelectUserDialog.build(account.keys.map { it.username }) {
				block(account.keys[it])
			})
		} else block(account.keys.first())
	}
}