package com.layne.squirrel.presentation.creditcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.layne.squirrel.R
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.di.DaggerViewModelFactory
import com.layne.squirrel.presentation.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.fragment_credit_cards_list.*
import javax.inject.Inject

class CreditCardListFragment : Fragment() {

	@Inject
	lateinit var viewModelFactory: DaggerViewModelFactory
	private val model by viewModels<CreditCardViewModel>({ requireActivity() }) { viewModelFactory }

	private var cardsAdapter: CreditCardAdapter? = null
	private var touchHelper: ItemTouchHelper? = null

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(R.layout.fragment_credit_cards_list, c, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Squirrel.dagger.inject(this)

		cardsAdapter = CreditCardAdapter(model.data.creditCards).apply {
			setOnItemClickListener { view, position ->
				val action = CreditCardListFragmentDirections.openCreditCard(position)
				val extras = FragmentNavigatorExtras(
					view.apply { transitionName = "creditCard" } to "creditCard"
				)
				findNavController().navigate(action, extras)
			}
			setOnStartDragListener { touchHelper?.startDrag(it) }
			setOnItemMoveListener { from, to -> model.swapCreditCards(from, to) }
			setOnItemDismissListener { i ->
				val card = model.data.creditCards[i]
				val message = getString(R.string.credit_card_deleted, card.number.subSequence(0, 4))

				Snackbar.make(container, message, LENGTH_LONG)
					.setAction(R.string.cancel) {
						model.addCreditCard(card, i)
						notifyItemInserted(i)
						notifyItemRangeChanged(i, itemCount)
					}.show()
				model.deleteCreditCard(i)
			}
			touchHelper = ItemTouchHelper(ItemTouchHelperCallback(this))
			touchHelper?.attachToRecyclerView(recyclerView)
		}
		recyclerView.layoutManager = LinearLayoutManager(context)
		recyclerView.adapter = cardsAdapter
	}
}