package com.layne.squirrel.framework.gateway

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.layne.squirrel.core.data.KeysDataSource
import com.layne.squirrel.core.domain.Data
import com.layne.squirrel.framework.CryptUtil
import com.layne.squirrel.framework.gateway.file.gson.DataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*

class KeysLocalFileDataSource(private val context: Context) : KeysDataSource {

	override suspend fun read(path: String, password: String): Data? {
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

						return Gson().fromJson(decrypted, DataEntity::class.java).toData()
					}
				}
			} catch (e: FileNotFoundException) {
				Log.e("KeysLocalFileDataSource", "read:", e)
			} catch (e: IOException) {
				Log.e("KeysLocalFileDataSource", "read:", e)
			} catch (e: SecurityException) {
				Log.e("KeysLocalFileDataSource", "read:", e)
			}
		}

		return null
	}

	override suspend fun write(data: Data, uri: String, password: String) {
		try {
			withContext(Dispatchers.IO) {
				context.contentResolver.openFileDescriptor(Uri.parse(uri), "rwt")?.use {
					FileOutputStream(it.fileDescriptor).use { fos ->
						val entity = DataEntity.build(data)
						CryptUtil.encrypt(Gson().toJson(entity), password)?.let { content ->
							fos.write(content.toByteArray())
						}
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