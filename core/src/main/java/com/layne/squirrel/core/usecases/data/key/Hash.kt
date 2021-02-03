package com.layne.squirrel.core.usecases.data.key

import java.security.MessageDigest

class Hash {
	operator fun invoke(string: String, algorithm: String): String {
		val digest = MessageDigest.getInstance(algorithm)
		digest.update(string.toByteArray())
		return bytesToHex(digest.digest())
	}

	private fun bytesToHex(bytes: ByteArray): String {
		val hexArray = "0123456789abcdef".toCharArray()
		val hexChars = CharArray(bytes.size * 2)

		for (j in bytes.indices) {
			val v = bytes[j].toInt() and 0xFF
			hexChars[j * 2] = hexArray[v.ushr(4)]
			hexChars[j * 2 + 1] = hexArray[v and 0x0F]
		}

		return String(hexChars)
	}
}
