package com.layne.squirrel.framework.autofill

import android.app.assist.AssistStructure

class ParsedStructureFactory(private val structure: AssistStructure) {

	fun build(): ParsedStructure? {
		var parsedStructure = FieldFinder(structure).parsedStructure
		if (parsedStructure == null)
			parsedStructure = DeepFieldsFinder(structure).parsedStructure
		return parsedStructure
	}
}