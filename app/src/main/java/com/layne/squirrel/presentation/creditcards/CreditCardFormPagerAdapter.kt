package com.layne.squirrel.presentation.creditcards

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.layne.squirrel.core.domain.CreditCard

class CreditCardFormPagerAdapter(fm: FragmentManager, private val creditCard: CreditCard) :
	FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

	override fun getItem(position: Int): Fragment {
		return CreditCardFormFragment.getInstance(position, creditCard)
	}

	override fun getCount(): Int {
		return 4
	}
}