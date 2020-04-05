package com.layne.squirrel.presentation.main.accounts

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.layne.squirrel.R
import com.layne.squirrel.R.string.key_deleted
import com.layne.squirrel.core.domain.Key
import com.layne.squirrel.presentation.ItemTouchHelperCallback
import com.layne.squirrel.presentation.main.MainActivity
import com.layne.squirrel.presentation.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_directory.*

class AccountsFragment : Fragment() {
	companion object {
		const val COPIED = "copied"
	}

	private lateinit var model: MainViewModel

	private var viewAdapter: AccountAdapter? = null
	private var touchHelper: ItemTouchHelper? = null
	private var dirIndex = -1

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(R.layout.fragment_directory, c, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		(activity as MainActivity).fab?.show()

		activity?.run {
			model = ViewModelProvider(this).get(MainViewModel::class.java)
		}

		val args: AccountsFragmentArgs by navArgs()
		dirIndex = args.directoryPosition

		activity?.title = model.data.value?.get(dirIndex)?.title

		viewAdapter = AccountAdapter(model.getAccounts(dirIndex)).apply {
			setOnItemClickListener {
				val action =
					AccountsFragmentDirections.openAccount(
						dirIndex,
						it
					)
				NavHostFragment
					.findNavController(this@AccountsFragment)
					.navigate(action)
			}
			setOnContextMenuitemClickListener { menuPosition, position, _ ->
				when (menuPosition) {
					0 -> onContextItemClicked(menuPosition, position)
					1 -> onContextItemClicked(menuPosition, position)
					2 -> onContextItemClicked(menuPosition, position)
					3 -> onContextItemClicked(menuPosition, position)
					4 -> {
						model.deleteAccount(dirIndex, position)
					}
				}
			}
			setOnStartDragListener {
				touchHelper?.startDrag(it)
			}
			setOnItemMoveListener { from, to ->
				if (from < to) {
					for (i in from until to)
						model.swapAccounts(dirIndex, i, i + 1)
				} else {
					for (i in from downTo to + 1)
						model.swapAccounts(dirIndex, i, i - 1)
				}
				model.getAccounts(dirIndex)
			}
			setOnItemDismissListener { pos ->
				val title = model.getAccounts(dirIndex)[pos].title
				val account = model.getAccounts(dirIndex)[pos]
				Snackbar.make(container, getString(key_deleted, title), LENGTH_LONG)
					.setAction(R.string.cancel) {
						model.remitAccount(dirIndex, pos, account)
						viewAdapter?.notifyItemInserted(pos)
						viewAdapter?.notifyItemRangeChanged(pos, itemCount)
					}
					.show()

				model.deleteAccount(dirIndex, pos)
			}

			touchHelper = ItemTouchHelper(ItemTouchHelperCallback(this))
			touchHelper?.attachToRecyclerView(recyclerView)
		}

		recyclerView.apply {
			setHasFixedSize(true)
			layoutManager = LinearLayoutManager(context)
			adapter = viewAdapter
		}

		(activity as MainActivity).fab?.setOnClickListener {
			val action = AccountsFragmentDirections.openAccount(dirIndex, -1)
			NavHostFragment.findNavController(this@AccountsFragment).navigate(action)
		}

		activity?.run {
			model.data.observe(this, Observer {
				if (dirIndex != -1)
					viewAdapter?.updateData(it[dirIndex].accounts)
			})
		}
	}

	private fun onContextItemClicked(menuPosition: Int, position: Int): Boolean {
		val accounts = model.getAccounts(dirIndex)

		if (model.getAccounts(dirIndex).size > 1) {
			context?.run {
				val builder = AlertDialog.Builder(this)
				builder.setTitle(getString(R.string.menu_showkeys_user))

				val arrayAdapter = ArrayAdapter<String>(this,
					android.R.layout.select_dialog_singlechoice,
					accounts[position].keys.map { it.username })

				builder.setNegativeButton(getString(R.string.menu_showkeys_cancel)) { dialog, _ -> dialog.dismiss() }
				builder.setAdapter(arrayAdapter) { _, which ->
					contextAction(accounts[position].keys[which], menuPosition)
				}
				builder.show()
			}
		} else
			contextAction(accounts[position].keys[0], menuPosition)

		return true
	}

	private fun contextAction(key: Key, menuPosition: Int) {
		val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		var clip: ClipData? = null
		when (menuPosition) {
			0 -> clip = ClipData.newPlainText(COPIED, key.username)
			1 -> clip = ClipData.newPlainText(COPIED, key.email)
			2 -> clip = ClipData.newPlainText(COPIED, key.password)
			3 -> {
				try {
					startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(key.url)))
				} catch (e: ActivityNotFoundException) {
					Snackbar.make(container, getString(R.string.no_valid_url), LENGTH_SHORT).show()
				}
			}
		}

		if (clip != null) {
			clipboard.setPrimaryClip(clip)
			Snackbar.make(container, getString(R.string.showkeys_copied), LENGTH_SHORT).show()
		}
	}
}