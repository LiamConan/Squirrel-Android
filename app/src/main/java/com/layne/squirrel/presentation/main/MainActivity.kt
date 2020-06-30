package com.layne.squirrel.presentation.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.layne.squirrel.R
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.framework.findNavController
import com.layne.squirrel.framework.requestAutofillService
import com.layne.squirrel.framework.show
import com.layne.squirrel.presentation.main.accounts.AccountListFragmentDirections
import com.layne.squirrel.presentation.main.directories.dialog.UseAutofillServiceDialog
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<MainViewModel> { viewModelFactory }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Squirrel.dagger.inject(this)
		setContentView(R.layout.activity_main)
		setSupportActionBar(bottomAppBar)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			model.isNeededToAskForAutofill {
				show(UseAutofillServiceDialog.build {
					model.setAutofillJustAsked()
					requestAutofillService()
				})
			}
		}

		findNavController(R.id.fragmentContainer).addOnDestinationChangedListener { controller, destination, args ->
			when (destination.id) {
				R.id.directoryListFragment -> {
					bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
					bottomAppBar.performShow()
				}
				R.id.accountListFragment   -> {
					bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
					floatingActionButton.setOnClickListener {
						args?.getInt("directoryPosition")?.let { pos ->
							controller.navigate(AccountListFragmentDirections.openAccount(pos, -1))
						}
					}
				}
				R.id.keyListFragment       -> {
					bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
					bottomAppBar.performShow()
				}
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == UseAutofillServiceDialog.ACTION_REQUEST_AUTOFILL
			&& resultCode == RESULT_OK
		) {
			model.rememberPassword()
		}
	}

	fun setFabOnClickListener(block: () -> Unit) {
		floatingActionButton.setOnClickListener {
			block()
		}
	}
}
