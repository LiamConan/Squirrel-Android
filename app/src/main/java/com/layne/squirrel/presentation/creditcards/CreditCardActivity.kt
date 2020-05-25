package com.layne.squirrel.presentation.creditcards

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.layne.squirrel.R
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.framework.findNavController
import kotlinx.android.synthetic.main.activity_credit_card.*
import javax.inject.Inject

class CreditCardActivity : AppCompatActivity() {

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<CreditCardViewModel> { viewModelFactory }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Squirrel.dagger.inject(this)
		setContentView(R.layout.activity_credit_card)
		setSupportActionBar(bottomAppBar)

		findNavController(R.id.fragmentContainer).addOnDestinationChangedListener { controller, destination, _ ->
			when (destination.id) {
				R.id.creditCardListFragment -> {
					floatingActionButton.show()
					floatingActionButton.setOnClickListener {
						controller.navigate(R.id.creditCardFragment)
					}
				}
				R.id.creditCardFragment     -> {
					floatingActionButton.hide()
					bottomAppBar.performShow()
				}
			}
		}
	}
}