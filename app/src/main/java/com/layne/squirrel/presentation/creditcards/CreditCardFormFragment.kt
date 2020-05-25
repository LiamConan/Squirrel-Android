package com.layne.squirrel.presentation.creditcards

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.layne.squirrel.R
import com.layne.squirrel.core.domain.CreditCard
import com.layne.squirrel.framework.creditCard
import com.layne.squirrel.framework.setAfterTextChangeListener
import com.layne.squirrel.framework.setOnItemSelectedListener
import kotlinx.android.synthetic.main.fragment_form_card_1.*
import kotlinx.android.synthetic.main.fragment_form_card_2.*
import kotlinx.android.synthetic.main.fragment_form_card_3.*
import kotlinx.android.synthetic.main.fragment_form_card_4.*

class CreditCardFormFragment : Fragment(), TextView.OnEditorActionListener {

	private lateinit var cardFragment: CreditCardFragment
	private var creditCard: CreditCard = creditCard { }

	companion object {
		const val TYPE = "type"
		const val CARD = "card"

		fun getInstance(type: Int, creditCard: CreditCard): CreditCardFormFragment {
			val fragment = CreditCardFormFragment()
			fragment.arguments = bundleOf(TYPE to type, CARD to Gson().toJson(creditCard))
			return fragment
		}
	}

	private val layoutId: Int by lazy {
		when (requireArguments().getInt(TYPE)) {
			0 -> R.layout.fragment_form_card_1
			1 -> R.layout.fragment_form_card_2
			2 -> R.layout.fragment_form_card_3
			3 -> R.layout.fragment_form_card_4
			else -> 0
		}
	}

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(layoutId, c, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		cardFragment = parentFragment as CreditCardFragment
		creditCard = Gson().fromJson(requireArguments().getString(CARD), CreditCard::class.java)
		when (requireArguments().getInt(TYPE)) {
			0 -> buildUICardNumber()
			1 -> buildUIExpiry()
			2 -> buildUINameHolder()
			3 -> buildUICVV()
		}
	}

	override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?) =
		(actionId == EditorInfo.IME_ACTION_NEXT).also {
			if (it) {
				cardFragment.fieldFilled()
			}
		}

	private fun buildUICardNumber() {
		editTextNumber.setAfterTextChangeListener { cardFragment.setNumber(it) }
		editTextNumber.setOnEditorActionListener(this)
		editTextNumber.setText(creditCard.number)
	}

	private fun buildUIExpiry() {
		editTextExpiry.setAfterTextChangeListener { cardFragment.setExpiry(it) }
		editTextExpiry.setOnEditorActionListener(this)
		editTextExpiry.setText(creditCard.expiry)
	}

	private fun buildUINameHolder() {
		spinnerDenomination.adapter = ArrayAdapter.createFromResource(
			requireContext(),
			R.array.civil_denomination,
			android.R.layout.simple_spinner_item
		).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
		spinnerDenomination.setOnItemSelectedListener { position ->
			cardFragment.setDenomination(position)
		}
		spinnerDenomination.setSelection(creditCard.denominaton)

		editTextHolderFirstname.setAfterTextChangeListener { cardFragment.setFirstname(it) }
		editTextHolderFirstname.setText(creditCard.firstname)

		editTextHolderName.setAfterTextChangeListener { cardFragment.setName(it) }
		editTextHolderName.setOnEditorActionListener(this)
		editTextHolderName.setText(creditCard.name)
	}

	private fun buildUICVV() {
		editTextCVV.setAfterTextChangeListener { cardFragment.setCVV(it) }
		editTextCVV.setOnEditorActionListener(this)
		editTextCVV.setText(creditCard.cvv)
	}
}