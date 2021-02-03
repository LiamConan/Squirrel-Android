package com.layne.squirrel.core.usecases.data.key

import com.google.gson.Gson
import com.layne.squirrel.core.domain.Data
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Decrypt {
	operator fun invoke(data: String, password: String): Data {
		val hash = Hash()
		val hashedPassword = hash(password, "MD5")
			.substring(0, 16)
		val iv = hash(hashedPassword, "SHA1")
			.substring(0, 16)

		val keySpec = SecretKeySpec(hashedPassword.toByteArray(), "AES")
		val ivSpec = IvParameterSpec(iv.toByteArray())
		val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

		val decrypted = String(cipher.doFinal(Base64.getDecoder().decode(data)))
		return Gson().fromJson(decrypted, Data::class.java)
	}
}