package com.layne.squirrel.core.usecases.password

import java.util.*

class GeneratePassword {
	operator fun invoke(
		n: Int,
		lower: Boolean,
		upper: Boolean,
		numbers: Boolean,
		spaces: Boolean,
		specials: Boolean
	): String {
		val random = Random()
		val password = StringBuilder()

		var characters = ""

		if (lower) characters += "abcdefghijklmnopqrstuvwxyz"
		if (upper) characters += "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		if (numbers) characters += "1234567890"
		if (spaces) characters += " "
		if (specials) characters += ",;:!?./§ù*%µ{([])}+-*&"

		for (i in 0 until n)
			password.append(characters[random.nextInt(characters.length)])

		return password.toString()
	}
}