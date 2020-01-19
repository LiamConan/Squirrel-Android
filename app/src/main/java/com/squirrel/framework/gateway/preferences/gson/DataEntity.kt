package com.squirrel.framework.gateway.preferences.gson

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.squirrel.core.domain.Directory

class DataEntity(
	@SerializedName("dirs")
	var directories: MutableList<DirectoryEntity>
) {

	companion object {
		fun build(directories: List<Directory>): DataEntity {
			return DataEntity(directories.map { DirectoryEntity.build(it) }.toMutableList())
		}
	}

	override fun toString(): String {
		return Gson().toJson(this)
	}

	fun toDirectories(): List<Directory> = directories.map { it.toDirectory() }
}
