package com.squirrel.framework

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.squirrel.presentation.main.keys.KeyFragment

fun preferences(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


fun SharedPreferences.savePassword(uri: String, password: String) {
	with(edit())
	{
		putString(uri, password)
		apply()
	}
}

fun fragmentOf(bundle: Bundle): KeyFragment {
	val fragment = KeyFragment()
	fragment.arguments = bundle
	return fragment
}

fun EditText.getValue(): String = text.toString()

fun DialogFragment.show(tag: String, activity: FragmentActivity) {
	val ft = activity.supportFragmentManager.beginTransaction()
	val prev = activity.supportFragmentManager.findFragmentByTag(tag)
	prev?.also { ft.remove(it) }
	ft.addToBackStack(null)

	show(ft, tag)
}