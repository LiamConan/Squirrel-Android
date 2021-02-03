package com.layne.squirrel.usecases

import com.google.gson.Gson
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.core.usecases.data.key.Decrypt
import com.layne.squirrel.core.usecases.data.key.Encrypt
import org.junit.Assert
import org.junit.Test

class CryptUtilTest {

	@Test
	fun `GIVEN json WHEN encrypting and decrypting THEN returns the same json`() {
		val data = Gson().fromJson(
			"{\"hits\":[{\"country\":\"France\",\"is_country\":false}]}",
			Data::class.java
		)
		val password = "test"
		val encrypt = Encrypt()
		val decrypt = Decrypt()

		val encrypted = String(encrypt(data, password)!!)
		val decrypted = decrypt(encrypted, password)

		Assert.assertEquals(decrypted, data)
	}
}