package com.layne.squirrel.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.layne.squirrel.R
import com.layne.squirrel.core.domain.Directory
import com.layne.squirrel.presentation.main.directories.dialog.UseAutofillServiceDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	private var model: MainViewModel? = null
	var fab: FloatingActionButton? = null
	var appBar: BottomAppBar? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(bottomAppBar)
		appBar = bottomAppBar

		model = ViewModelProvider(this).get(MainViewModel::class.java)
		fab = floatingActionButton

		val json = intent.getStringExtra("json") ?: "{}"
		model?.uri = Uri.parse(intent.getStringExtra("uri"))
		model?.password = intent.getStringExtra("password") ?: ""

		model?.data?.value =
			Gson().fromJson(json, object : TypeToken<ArrayList<Directory?>?>() {}.type)

		model?.data?.observe(this, Observer {
			model?.saveData()
		})

		model?.isNeededToAskForAutofill {
			if (it) UseAutofillServiceDialog{
				model?.setAutofillJustAsked()
				val uri: Uri = Uri.parse("package:$packageName")
				val i = Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE, uri)
				startActivityForResult(i, UseAutofillServiceDialog.ACTION_REQUEST_AUTOFILL)
			}.show(this)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == android.R.id.home) {
			onBackPressed()
			return true
		}

		return super.onOptionsItemSelected(item)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == UseAutofillServiceDialog.ACTION_REQUEST_AUTOFILL
			&& resultCode == RESULT_OK
		) {
			model?.rememberPassword()
		}
	}
}
