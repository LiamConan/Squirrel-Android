package com.layne.squirrel.presentation.main.directories

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.layne.squirrel.R
import com.layne.squirrel.R.string.key_deleted
import com.layne.squirrel.core.domain.Directory
import com.layne.squirrel.framework.*
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.presentation.creditcards.CreditCardActivity
import com.layne.squirrel.presentation.main.MainViewModel
import com.layne.squirrel.presentation.main.directories.dialog.*
import kotlinx.android.synthetic.main.fragment_list_directory.*
import javax.inject.Inject

class DirectoryListFragment : Fragment() {

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<MainViewModel>({ requireActivity() }) { viewModelFactory }

	private var viewAdapter: DirectoryAdapter? = null

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(R.layout.fragment_list_directory, c, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Squirrel.dagger.inject(this)
		setHasOptionsMenu(true)

		viewAdapter = DirectoryAdapter(model.data.directories).apply {
			setOnItemClickListener {
				NavHostFragment
					.findNavController(this@DirectoryListFragment)
					.navigate(DirectoryListFragmentDirections.openDirectory(it))
			}
			setOnCreateContextMenuListener { menu, directory, position ->
				menu.addWithOnClick(R.string.dir_rename) {
					show(RenameDirectoryDialog.build(directory.title) {
						viewAdapter?.updateData(model.renameDirectory(position, it).directories)
					})
				}
				menu.addWithOnClick(R.string.dir_delete) {
					show(DeleteDirectoryDialog.build {
						viewAdapter?.updateData(model.deleteDirectory(position).directories)
					})
				}
			}
			setOnItemMoveListener { from, to ->
				model.swapDirectories(from, to)
			}
			setOnItemDismissListener { i ->
				val directory = model.data.directories[i]
				Snackbar.make(container, getString(key_deleted, directory.title), LENGTH_LONG)
					.setAction(R.string.cancel) {
						viewAdapter?.updateData(model.addDirectory(directory, i).directories)
						notifyItemInserted(i)
						notifyItemRangeChanged(i, itemCount)
					}.show()
				viewAdapter?.updateData(model.deleteDirectory(i).directories)
			}
		}

		recyclerView.apply {
			setHasFixedSize(true)
			layoutManager = LinearLayoutManager(context)
			adapter = viewAdapter
		}

		runMainActivity {
			setFabOnClickListener {
				show(CreateDirectoryDialog.build {
					viewAdapter?.updateData(model.addDirectory(Directory(it)).directories)
				})
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.home, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {

		when (item.itemId) {
			R.id.biometrics     -> {
				if (model.isBiometricsEnabled())
					show(CancelBiometricsDialog.build())
				else
					showBiometricPrompt { model.enableBiometrics() }
			}
			R.id.changePassword -> show(ChangePasswordDialog.build(model.password) {
				model.changePassword(it)
			})
			R.id.creditCard     -> {
				startActivity(Intent(activity, CreditCardActivity::class.java))
			}
		}

		return true
	}
}
