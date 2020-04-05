package com.layne.squirrel

import com.layne.squirrel.framework.CryptUtil
import org.junit.Assert
import org.junit.Test

class CryptUtilTest {

	@Test
	fun given_json_WHEN_encrypt_and_decrypt_THEN_returns_the_same_json() {

		val json = "{\"hits\":[{\"country\":\"France\",\"is_country\":false}]}"
		val password = "test"

		val encrypted = CryptUtil.encrypt(json, password)
		val decrypted = CryptUtil.decrypt(encrypted!!, password)
		
		Assert.assertEquals(decrypted, json)
	}
}