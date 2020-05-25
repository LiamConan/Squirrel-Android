package com.layne.squirrel.framework.autofill

import android.annotation.TargetApi
import android.app.assist.AssistStructure
import android.os.Build
import com.layne.squirrel.framework.containsOneOf
import com.layne.squirrel.framework.parsedStructured

@TargetApi(Build.VERSION_CODES.O)
class FieldFinder(structure: AssistStructure) {

	companion object {
		val usernameTags =
			arrayOf("username", "id", "identifiant", "identifier", "userid", "user", "utilisateur")
		val passwordTags = arrayOf("password", "mdp", "mot de passe", "pass", "passwd")
	}

	val parsedStructure: ParsedStructure? by lazy {
		val edittexts = structure.getAllEditTextFields()
		if (edittexts.isEmpty()) null
		else {
			val usernameFieldIds = findFieldByTag(edittexts, usernameTags)?.autofillId
			val passwordFieldIds = findFieldByTag(edittexts, passwordTags)?.autofillId

			if (usernameFieldIds == null && passwordFieldIds == null) {
				if (structure.containsLoginForm()) {
					parsedStructured {
						edittexts[0].autofillId?.let { usernameId = it }
						if (edittexts.size > 1)
							edittexts[1].autofillId?.let { passwordId = it }
					}
				} else null
			} else {
				parsedStructured {
					usernameFieldIds?.let { usernameId = it }
					passwordFieldIds?.let { passwordId = it }
				}
			}
		}
	}

	private fun findFieldByTag(
		nodes: List<AssistStructure.ViewNode>,
		tags: Array<String>
	): AssistStructure.ViewNode? {
		return nodes.firstOrNull { node ->
			node.autofillHints?.any { it.containsOneOf(*tags) } == true
					|| node.contentDescription?.containsOneOf(*tags) == true
		}
	}
}