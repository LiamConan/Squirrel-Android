package com.layne.squirrel.framework.autofill

import android.annotation.TargetApi
import android.app.assist.AssistStructure
import android.os.Build
import com.layne.squirrel.framework.containsOneOf
import com.layne.squirrel.framework.parsedStructured
import java.util.*

@TargetApi(Build.VERSION_CODES.O)
class FieldFinder(structure: AssistStructure) {

	companion object {
		val usernameTags = arrayOf("username", "id", "identifiant", "identifier", "userid", "user", "utilisateur")
		val passwordTags = arrayOf("password", "mdp", "mot de passe", "pass", "passwd")
	}

	val parsedStructure: ParsedStructure? by lazy {
		val fields = findAutofillFields(structure.getWindowNodeAt(0).rootViewNode)
		val usernameField = findFieldByTag(fields, usernameTags)
		val passwordField = findFieldByTag(fields, passwordTags)

		if (usernameField == null && passwordField == null)
			null
		else {
			parsedStructured {
				findFieldByTag(fields, usernameTags)?.autofillId?.let { usernameId = it }
				findFieldByTag(fields, passwordTags)?.autofillId?.let { passwordId = it }
			}
		}
	}

	private fun findAutofillFields(node: AssistStructure.ViewNode): List<AssistStructure.ViewNode> =
		when {
			node.childCount > 0 -> dispatchNode(node)
			matchField(node)    -> listOf(node)
			else                -> listOf()
		}

	private fun dispatchNode(node: AssistStructure.ViewNode): List<AssistStructure.ViewNode> {
		val res = mutableListOf<AssistStructure.ViewNode>()
		for (i in 0 until node.childCount) {
			res += findAutofillFields(node.getChildAt(i))
		}
		return res
	}

	private fun findFieldByTag(
		nodes: List<AssistStructure.ViewNode>,
		tags: Array<String>
	): AssistStructure.ViewNode? {
		return nodes.firstOrNull { node ->
			node.autofillHints?.any {
				it.toLowerCase(Locale.ENGLISH).containsOneOf(*tags)
			} == true || node.contentDescription?.containsOneOf(*tags) == true
		}
	}

	private fun matchField(node: AssistStructure.ViewNode): Boolean =
		node.autofillHints != null || node.className.contains("EditText")
}