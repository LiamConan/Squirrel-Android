package com.squirrel.presentation.main

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squirrel.R
import com.squirrel.core.domain.Directory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	private var model: MainViewModel? = null
	var fab: FloatingActionButton? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(findViewById(R.id.bottomAppBar))

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
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == android.R.id.home) {
			onBackPressed()
			return true
		}

		return super.onOptionsItemSelected(item)
	}
}
