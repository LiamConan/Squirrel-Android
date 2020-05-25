package com.layne.squirrel.framework.autofill

import android.annotation.TargetApi
import android.app.assist.AssistStructure
import android.content.pm.PackageManager
import android.os.Build
import com.layne.squirrel.framework.containsOneOf

fun AssistStructure.getAllEditTextFields(): List<AssistStructure.ViewNode> =
	getWindowNodeAt(0).rootViewNode.pickFieldsByExploringTree()

private fun AssistStructure.ViewNode.pickFieldsByExploringTree(): List<AssistStructure.ViewNode> =
	when {
		childCount > 0 -> (0 until childCount).map {
			getChildAt(it).pickFieldsByExploringTree()
		}.flatten()
		isEditText()   -> listOf(this)
		else           -> listOf()
	}

@TargetApi(Build.VERSION_CODES.O)
private fun AssistStructure.ViewNode.isEditText(): Boolean =
	autofillHints != null || (className != null && className.contains("EditText"))

fun PackageManager.getNameFromPackage(packageName: String): String = runCatching {
	getApplicationInfo(packageName, 0)
}.getOrNull()?.let {
	getApplicationLabel(it).toString()
} ?: "Unknown"

fun AssistStructure.containsLoginForm(): Boolean = getWindowNodeAt(0).rootViewNode.isLoginForm()

private fun AssistStructure.ViewNode.isLoginForm(): Boolean {
	return if (idEntry != null && idEntry.containsOneOf("login", "connection", "connexion"))
		true
	else
		(0 until childCount).map {
			getChildAt(it).isLoginForm()
		}.any { it }
}