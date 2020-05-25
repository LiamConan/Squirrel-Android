package com.layne.squirrel.presentation.main.keys

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.layne.squirrel.core.domain.Key
import com.layne.squirrel.framework.indexOfFirstOr

class KeyPagerAdapter(
	fm: FragmentManager,
	data: List<Key>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

	private val registeredFragments = data.mapIndexed { i: Int, key: Key ->
		KeyFragment.getInstance(key, i)
	}.toMutableList()

	override fun getItem(position: Int): Fragment {
		return registeredFragments[position]
	}

	override fun getCount(): Int = registeredFragments.filter { it.position != POSITION_NONE }.size

	override fun getItemPosition(obj: Any): Int {
		val fragment = obj as KeyFragment
		return fragment.position
	}

	private fun realPosition(position: Int): Int =
		registeredFragments.indexOfFirstOr(POSITION_NONE) { it.position == position }

	fun isFormsFilled(): Boolean = registeredFragments.mapNotNull {
		if (it.position == PagerAdapter.POSITION_NONE)
			null
		else it.isFormFilled()
	}.all { it }

	fun add() {
		registeredFragments.add(KeyFragment.getInstance(Key(), registeredFragments.size))
		notifyDataSetChanged()
	}

	fun remove(index: Int) {
		val position = realPosition(index)

		registeredFragments[position].position = POSITION_NONE

		for (i in position until registeredFragments.size) {
			if (registeredFragments[i].position != POSITION_NONE)
				registeredFragments[i].position--
		}

		notifyDataSetChanged()
	}

	fun keys(): List<Key> = registeredFragments.mapNotNull {
		if (it.position == PagerAdapter.POSITION_NONE)
			null
		else it.getKeyFromUI()
	}
}