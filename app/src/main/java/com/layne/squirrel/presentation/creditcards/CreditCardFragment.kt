package com.layne.squirrel.presentation.creditcards

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.layne.squirrel.R
import com.layne.squirrel.core.domain.CreditCard
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.creditCard
import kotlinx.android.synthetic.main.fragment_credit_card.*
import javax.inject.Inject


class CreditCardFragment : Fragment() {

	enum class Goal { CREATION, EDITION }

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory
	private val model by viewModels<CreditCardViewModel>({ requireActivity() }) { viewModelFactory }

	private var goal: Goal = Goal.CREATION
	private var cardIndex = -1
	private var creditCard: CreditCard = creditCard { }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		sharedElementEnterTransition =
			TransitionInflater.from(context).inflateTransition(android.R.transition.move)
	}

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(R.layout.fragment_credit_card, c, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Squirrel.dagger.inject(this)
		setHasOptionsMenu(true)

		val index = requireArguments().getInt("creditCardPosition")
		if (index > -1) {
			goal = Goal.EDITION
			cardIndex = index
			creditCard = model.data.creditCards[index].copy()
		}
		creditCardView.bindData(creditCard)

		pager.adapter = CreditCardFormPagerAdapter(childFragmentManager, creditCard)
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.creditcard, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.save -> {
				if (goal == Goal.CREATION)
					model.addCreditCard(creditCard)
				else
					model.updateCreditCard(cardIndex, creditCard)
				requireActivity().onBackPressed()
			}
		}
		return true
	}

	fun setNumber(number: String) {
		creditCard.number = number
		creditCardView.number = creditCard.number
	}

	fun setExpiry(expiry: String) {
		creditCard.expiry = expiry
		creditCardView.expiry = creditCard.expiry
	}

	fun setDenomination(denomination: Int) {
		creditCard.denominaton = denomination
		creditCardView.setNameHolder(denomination, creditCard.firstname, creditCard.name)
	}

	fun setName(name: String) {
		creditCard.name = name
		creditCardView.setNameHolder(creditCard.denominaton, creditCard.firstname, name)
	}

	fun setFirstname(firstname: String) {
		creditCard.firstname = firstname
		creditCardView.setNameHolder(creditCard.denominaton, firstname, creditCard.name)
	}

	fun setCVV(cvv: String) {
		creditCard.cvv = cvv
		creditCardView.cvv = creditCard.cvv
	}

	fun fieldFilled() {
		pager.currentItem = pager.currentItem + 1
	}
}