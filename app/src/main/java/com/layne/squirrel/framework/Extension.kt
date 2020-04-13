package com.layne.squirrel.framework

import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.layne.squirrel.framework.autofill.ParsedStructure
import com.layne.squirrel.presentation.main.keys.KeyFragment

fun fragmentOf(bundle: Bundle): KeyFragment {
	val fragment = KeyFragment()
	fragment.arguments = bundle
	return fragment
}

fun Fragment.withArgs(bundle: Bundle): Fragment {
	this.arguments = bundle
	return this
}

fun EditText.getValue(): String = text.toString()

fun DialogFragment.show(tag: String, activity: FragmentActivity) {
	val ft = activity.supportFragmentManager.beginTransaction()
	val prev = activity.supportFragmentManager.findFragmentByTag(tag)
	prev?.also { ft.remove(it) }
	ft.addToBackStack(null)

	show(ft, tag)
}

fun parsedStructured(block: ParsedStructure.() -> Unit) = ParsedStructure().apply(block)

fun String.containsOneOf(vararg strings: String): Boolean = strings.any { this.contains(it) }
fun CharSequence.containsOneOf(vararg strings: String): Boolean = strings.any { this.contains(it) }
