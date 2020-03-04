package com.squirrel.presentation.main.keys

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.squirrel.R
import com.squirrel.core.domain.Account
import com.squirrel.core.domain.Key
import com.squirrel.framework.getValue
import com.squirrel.presentation.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_account.*
import java.util.*

class KeysFragment : Fragment() {
	companion object {
		const val COPIED = "copied"
	}

	private var model: MainViewModel? = null
	private lateinit var adapter: KeyPagerAdapter

	private var keys: MutableList<Key> = mutableListOf()
	private var dirIndex = 0
	private var accountIndex = 0

	override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
		i.inflate(R.layout.fragment_account, c, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setHasOptionsMenu(true)

		model = activity?.run {
			ViewModelProvider(this).get(MainViewModel::class.java)
		}
		val args: KeysFragmentArgs by navArgs()
		dirIndex = args.directoryPosition
		accountIndex = args.accountPosition

		keys = if (accountIndex != -1) {
			editTextTitle.setText(
				model?.data?.value?.get(dirIndex)?.accounts?.get(accountIndex)?.title
			)
			model?.data?.value?.get(dirIndex)?.accounts?.get(accountIndex)?.keys ?: mutableListOf()
		} else
			mutableListOf(Key("", "", "", "", ""))

		pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
			override fun onPageScrollStateChanged(state: Int) {
				// Not used
			}

			override fun onPageScrolled(pos: Int, offset: Float, pixels: Int) {
				// Not used
			}

			override fun onPageSelected(position: Int) {
				model?.currentKey = position
				pageIndicatorView.setSelected(position)
			}
		})

		activity?.supportFragmentManager?.let {
			adapter = KeyPagerAdapter(it, keys)
			pager.adapter = adapter
			model?.setOnSubkeyDeleted {
				adapter.remove(pager.currentItem, keys)
				pageIndicatorView.count = pager.childCount
			}
			model?.setOnKeyDeleted {
				activity?.onBackPressed()
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		inflater.inflate(R.menu.showkey, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.save -> {
				if (editTextTitle.text?.isNotEmpty() == true && adapter.fragmentsFieldsFilled()) {
					val array = mutableListOf<Key>()

					adapter.registeredFragments.forEach { array.add(it.getSubkeyFromUI()) }
					val account = Account(
						editTextTitle.getValue(),
						now(),
						array
					)

					if (accountIndex == -1)
						model?.addAccount(dirIndex, account)
					else
						model?.updateAccount(dirIndex, accountIndex, account)

					activity?.onBackPressed()
				} else
					Snackbar.make(container, R.string.key_not_filled, Snackbar.LENGTH_LONG).show()
			}
		}

		return super.onOptionsItemSelected(item)
	}

	private fun now(): String {
		val calendar = Calendar.getInstance()
		val dayOfWeek =
			resources.getStringArray(R.array.days)[calendar.get(Calendar.DAY_OF_WEEK) - 1]
		val day = calendar.get(Calendar.DATE)
		val month = resources.getStringArray(R.array.months)[calendar.get(Calendar.MONTH)]
		val year = calendar.get(Calendar.YEAR)

		return "$dayOfWeek $day $month $year"
	}

	inner class KeyPagerAdapter(
		fm: FragmentManager,
		data: MutableList<Key>
	) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

		private var keys = data
		val registeredFragments = mutableListOf<KeyFragment>()

		init {
			keys.forEachIndexed { i, key ->
				registeredFragments.add(
					i,
					KeyFragment.getInstance(
						key,
						dirIndex,
						accountIndex
					).apply { tag = i })
			}
			pager.offscreenPageLimit = keys.size
		}

		override fun getItem(position: Int): Fragment {
			return registeredFragments[position]
		}

		override fun getCount(): Int {
			return keys.size
		}

		override fun getItemPosition(obj: Any): Int {
			val fragment = obj as KeyFragment
			return fragment.tag
		}

		fun fragmentsFieldsFilled(): Boolean {
			var filled = true
			adapter.registeredFragments.forEach { filled = filled && it.fieldsFilled() }
			return filled
		}

		fun remove(index: Int, data: MutableList<Key>) {
			keys = data
			val position = realPosition(index)

			registeredFragments[position].tag = PagerAdapter.POSITION_NONE

			for (i in position until registeredFragments.size) {
				if (registeredFragments[i].tag != PagerAdapter.POSITION_NONE)
					registeredFragments[i].tag--
			}

			notifyDataSetChanged()
			finishUpdate(container)
		}

		private fun realPosition(tag: Int): Int {
			for (i in 0 until registeredFragments.size) {
				if (registeredFragments[i].tag == tag)
					return i
			}
			return PagerAdapter.POSITION_NONE
		}
	}
}
