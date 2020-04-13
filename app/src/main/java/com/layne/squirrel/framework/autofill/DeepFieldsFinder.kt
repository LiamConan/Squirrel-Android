package com.layne.squirrel.framework.autofill

import android.annotation.TargetApi
import android.app.assist.AssistStructure
import android.os.Build
import com.layne.squirrel.framework.containsOneOf
import com.layne.squirrel.framework.parsedStructured

@TargetApi(Build.VERSION_CODES.O)
class DeepFieldsFinder(structure: AssistStructure) {

	companion object {
		val loginTags = arrayOf("login", "connection", "connexion", "log")
	}

	val parsedStructure: ParsedStructure? by lazy {
		val root = structure.getWindowNodeAt(0).rootViewNode
		if (isLoginForm(root)) {
			val fields = findEditTexts(root)

			if (fields.isEmpty()) null
			else {
				parsedStructured {
					fields[0].autofillId?.let {
						usernameId = it

					}
					if (fields.size > 1)
						fields[1].autofillId?.let { passwordId = it }
				}
			}
		} else null
	}

	private fun isLoginForm(node: AssistStructure.ViewNode): Boolean {
		return if (node.idEntry != null && node.idEntry.containsOneOf(*loginTags)) true
		else dispatchIsLoginForm(node)
	}

	private fun dispatchIsLoginForm(node: AssistStructure.ViewNode): Boolean {
		var res = false
		for (i in 0 until node.childCount) {
			res = res || isLoginForm(node.getChildAt(i))
		}
		return res
	}

	private fun findEditTexts(node: AssistStructure.ViewNode): List<AssistStructure.ViewNode> {
		return when {
			node.childCount > 0                 -> {
				var res = listOf<AssistStructure.ViewNode>()
				for (i in 0 until node.childCount) {
					res = res + findEditTexts(node.getChildAt(i))
				}
				return res
			}
			node.className.contains("EditText") -> listOf(node)
			else                                -> listOf()
		}
	}
}