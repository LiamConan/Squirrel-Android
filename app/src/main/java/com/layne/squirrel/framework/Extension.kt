package com.layne.squirrel.framework

import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import com.layne.squirrel.R
import com.layne.squirrel.core.domain.CreditCard
import com.layne.squirrel.core.domain.DataHolder
import com.layne.squirrel.core.domain.Key
import com.layne.squirrel.framework.autofill.ParsedStructure
import java.util.*

fun parsedStructured(block: ParsedStructure.() -> Unit) = ParsedStructure().apply(block)

fun String.containsOneOf(vararg strings: String): Boolean =
	strings.any { this.contains(it, ignoreCase = true) }

fun CharSequence.containsOneOf(vararg strings: String): Boolean = strings.any { this.contains(it) }

fun AppCompatSpinner.setOnItemSelectedListener(block: (Int) -> Unit) {
	onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
		override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
			block(pos)
		}

		override fun onNothingSelected(parent: AdapterView<*>?) {}
	}
}

fun dataHolder(block: DataHolder.() -> Unit) = DataHolder().apply(block)
fun creditCard(block: CreditCard.() -> Unit) = CreditCard().apply(block)
fun key(block: Key.() -> Unit) = Key().apply(block)

fun Context.now(): String {
	val calendar = Calendar.getInstance()
	val dayOfWeek =
		resources.getStringArray(R.array.days)[calendar.get(Calendar.DAY_OF_WEEK) - 1]
	val day = calendar.get(Calendar.DATE)
	val month = resources.getStringArray(R.array.months)[calendar.get(Calendar.MONTH)]
	val year = calendar.get(Calendar.YEAR)

	return "$dayOfWeek $day $month $year"
}

fun <T> List<T>.indexOfFirstOr(value: Int, predicate: (T) -> Boolean): Int {
	val res = indexOfFirst { predicate(it) }
	return if (res == -1) value else res
}