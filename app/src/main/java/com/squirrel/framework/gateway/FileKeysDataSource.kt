package com.squirrel.framework.gateway

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.squirrel.core.data.KeysDataSource
import com.squirrel.core.domain.Directory
import com.squirrel.framework.CryptUtil
import com.squirrel.framework.Squirrel
import com.squirrel.framework.gateway.file.gson.DataEntity
import java.io.*

class FileKeysDataSource(private val context: Context) : KeysDataSource {

	override suspend fun read(path: String, password: String): List<Directory>? {
		val uri = Uri.parse(path)
		uri?.let {
			try {
				context.contentResolver?.openFileDescriptor(uri, "r")?.use { fd ->
					FileInputStream(fd.fileDescriptor).use { fis ->
						val reader = BufferedReader(InputStreamReader(fis))
						val builder = StringBuilder()
						var line = reader.readLine()

						while (line != null) {
							builder.append(line)
							line = reader.readLine()
						}

						val decrypted = CryptUtil.decrypt(builder.toString(), password)

						return Gson().fromJson(decrypted, DataEntity::class.java).toDirectories()
					}
				}
			} catch (e: FileNotFoundException) {
				e.printStackTrace()
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}

		return null
	}

	override suspend fun write(directories: List<Directory>) {
		val squirrel = context.applicationContext as Squirrel
		val uri = squirrel.uri
		val password = squirrel.password
		uri?.let {
			try {
				context.contentResolver.openFileDescriptor(uri, "rwt")?.use {
					FileOutputStream(it.fileDescriptor).use { fos ->
						val data = DataEntity.build(directories)
						CryptUtil.encrypt(Gson().toJson(data), password ?: "")?.let { content ->
							fos.write(content.toByteArray())
						}
					}
				}
			} catch (e: FileNotFoundException) {
				e.printStackTrace()
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
	}
}