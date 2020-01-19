package com.squirrel.framework

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptUtil {
	@Throws(Exception::class)
	fun encrypt(text: String, password: String): String? {
		val hashedPassword = toHash(password, "MD5")
			.substring(0, 16)
		val iv = toHash(hashedPassword, "SHA1")
			.substring(0, 16)

		val keySpec = SecretKeySpec(hashedPassword.toByteArray(), "AES")
		val ivSpec = IvParameterSpec(iv.toByteArray())
		val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

		val results = cipher.doFinal(text.toByteArray())

		return Base64.encodeToString(results, Base64.NO_WRAP or Base64.DEFAULT)
	}

	@Throws(Exception::class)
	fun decrypt(text: String, password: String): String? {
		val hashedPassword = toHash(password, "MD5")
			.substring(0, 16)
		val iv = toHash(hashedPassword, "SHA1")
			.substring(0, 16)

		val keySpec = SecretKeySpec(hashedPassword.toByteArray(), "AES")
		val ivSpec = IvParameterSpec(iv.toByteArray())
		val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

		return String(cipher.doFinal(Base64.decode(text, Base64.NO_WRAP or Base64.DEFAULT)))
	}

	private fun toHash(string: String, algorithm: String): String {
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
