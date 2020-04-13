package com.layne.squirrel.framework.autofill

import android.annotation.TargetApi
import android.app.assist.AssistStructure
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.*
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import android.widget.Toast
import com.layne.squirrel.R
import com.layne.squirrel.core.domain.Directory
import com.layne.squirrel.framework.Squirrel
import com.layne.squirrel.framework.interactor.KeysInteractor
import com.layne.squirrel.framework.interactor.PreferencesInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@TargetApi(Build.VERSION_CODES.O)
class PasswordFillService : AutofillService(), CoroutineScope by MainScope() {

	@Inject
	lateinit var preferences: PreferencesInteractor

	@Inject
	lateinit var keysInteractor: KeysInteractor

	init {
		Squirrel.dagger.inject(this)
	}

	override fun onFillRequest(
		request: FillRequest,
		cancellationSignal: CancellationSignal,
		callback: FillCallback
	) {
		val path = runBlocking { preferences.getFilePath() }
		val isPasswordSaved = runBlocking { preferences.getFilePreferences(path).passwordSaved }
		val structure: AssistStructure = request.fillContexts.last().structure
		val parsedStructure = ParsedStructureFactory(structure).build()

		if (!isPasswordSaved || parsedStructure == null)
			callback.onSuccess(null)
		else
			callback.onSuccess(buildResponse(fetchUserData(structure), parsedStructure))
	}

	@ExperimentalStdlibApi
	override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
		val context: List<FillContext> = request.fillContexts
		val structure: AssistStructure = context[context.size - 1].structure

		if (request.clientState == null)
			Toast.makeText(
				this,
				getString(R.string.no_data_provided, structure.activityComponent.packageName),
				Toast.LENGTH_LONG
			).show()

		callback.onSuccess()
	}

	private fun fetchUserData(structure: AssistStructure): List<UserData> {
		val path = runBlocking { preferences.getFilePath() }
		val preferences = runBlocking { preferences.getFilePreferences(path) }
		val keys: List<Directory> = runBlocking {
			keysInteractor.getKeys(path, preferences.password) ?: listOf()
		}
		val keyword = structure.activityComponent.packageName
		val res = mutableListOf<UserData>()

		keysInteractor.searchKeys(keys, keyword).forEach { account ->
			account.keys.forEach {
				res.add(UserData(it.username, it.password))
			}
		}

		return res
	}

	private fun buildPresentation(text: String): RemoteViews {
		val presentation = RemoteViews(
			packageName,
			android.R.layout.simple_list_item_1
		)
		presentation.setTextViewText(android.R.id.text1, text)
		return presentation
	}

	private fun buildResponse(
		data: List<UserData>,
		parsedStructure: ParsedStructure
	): FillResponse? {
		if (data.isEmpty())
			return buildEmptyResponse(parsedStructure)

		val fillResponse = FillResponse.Builder()
		data.forEach { userData ->
			fillResponse.addDataset(
				Dataset.Builder()
					.setValue(
						parsedStructure.usernameId,
						AutofillValue.forText(userData.username),
						buildPresentation(userData.username)
					)
					.setValue(
						parsedStructure.passwordId,
						AutofillValue.forText(userData.password),
						buildPresentation(getString(R.string.password_for, userData.username))
					)
					.build()
			)
		}
		return fillResponse.build()
	}

	private fun buildEmptyResponse(parsedStructure: ParsedStructure): FillResponse {
		return FillResponse.Builder()
			.setSaveInfo(
				SaveInfo.Builder(
					SaveInfo.SAVE_DATA_TYPE_USERNAME or SaveInfo.SAVE_DATA_TYPE_PASSWORD,
					arrayOf(parsedStructure.usernameId, parsedStructure.passwordId)
				).build()
			).build()
	}
}
