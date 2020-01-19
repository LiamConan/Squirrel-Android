package com.squirrel.core

fun <T> MutableList<T>.swap(from: Int, to: Int) {
	if (from < to) {
		for (i in from until to) {
			val tmp = this[i] // 'this' corresponds to the list
			this[i] = this[i+1]
			this[i+1] = tmp
		}
	} else {
		for (i in from downTo to + 1){
			val tmp = this[i] // 'this' corresponds to the list
			this[i] = this[i-1]
			this[i-1] = tmp
		}
	}
}