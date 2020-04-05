package com.layne.squirrel.framework.gateway

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.layne.squirrel.core.data.KeysDataSource
import com.layne.squirrel.core.domain.Directory
import com.layne.squirrel.framework.CryptUtil
import com.layne.squirrel.framework.gateway.file.gson.DataEntity
import java.io.*

class KeysLocalFileDataSource(private val context: Context) : KeysDataSource {

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

	override suspend fun write(data: List<Directory>, uri: String, password: String) {
		try {
			context.contentResolver.openFileDescriptor(Uri.parse(uri), "rwt")?.use {
				FileOutputStream(it.fileDescriptor).use { fos ->
					val entity = DataEntity.build(data)
					CryptUtil.encrypt(Gson().toJson(entity), password)?.let { content ->
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