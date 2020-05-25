package com.layne.squirrel.framework.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class DaggerViewModelFactory @Inject constructor(
	private val viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		val creator = viewModelsMap[modelClass] ?: viewModelsMap.asIterable().firstOrNull {
			modelClass.isAssignableFrom(it.key)
		}?.value ?: throw IllegalArgumentException("Unknown model class $modelClass")

		return runCatching { creator.get() as T }.getOrThrow()
	}
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)