package com.squirrel.presentation.main.directories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squirrel.R
import com.squirrel.core.domain.Directory
import com.squirrel.presentation.main.ItemTouchHelperAdapter

class DirectoryAdapter(private var dirs: MutableList<Directory>) :
	RecyclerView.Adapter<DirectoryAdapter.ViewHolder>(),
	ItemTouchHelperAdapter {

	private var itemClickListener: (Int) -> Unit = {}
	private var contextMenuItemClickListener: (Int, Int, Directory) -> Unit = { _, _, _ -> }
	private var dragListener: (RecyclerView.ViewHolder) -> Unit = {}
	private var onItemMoveListener: (Int, Int) -> MutableList<Directory> =
		{ _, _ -> mutableListOf() }
	private var onItemDismissListener: (Int) -> Unit = { }

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		var textView: TextView = view.findViewById(R.id.text)
		var handler: ImageView = view.findViewById(R.id.handle)
	}


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		ViewHolder(
			LayoutInflater.from(parent.context).inflate(
				R.layout.item_directory,
				parent,
				false
			)
		)

	@SuppressLint("ClickableViewAccessibility")
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.textView.text = dirs[position].title
		holder.textView.setOnClickListener {
			itemClickListener(position)
		}
		holder.handler.setOnTouchListener { _, event ->
			if (event.actionMasked == MotionEvent.ACTION_DOWN)
				dragListener(holder)
			true
		}
		holder.textView.setOnCreateContextMenuListener { menu, _, _ ->
			val rename = menu.add(R.string.dir_rename)
			rename.setOnMenuItemClickListener {
				contextMenuItemClickListener(0, position, dirs[position])
				true
			}
			val delete = menu.add(R.string.dir_delete)
			delete.setOnMenuItemClickListener {
				contextMenuItemClickListener(1, position, dirs[position])
				true
			}
		}
	}

	override fun getItemCount() = dirs.size

	override fun onItemMove(from: Int, to: Int) {
		onItemMoveListener(from, to)
		notifyItemMoved(from, to)
	}

	override fun onItemDismiss(position: Int) {
		onItemDismissListener(position)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, itemCount)
	}

	fun updateData(data: MutableList<Directory>) {
		dirs = data
		notifyDataSetChanged()
	}

	fun setOnItemClickListener(l: (Int) -> Unit) {
		itemClickListener = l
	}

	fun setOnContextMenuitemClickListener(l: (Int, Int, Directory) -> Unit) {
		contextMenuItemClickListener = l
	}

	fun setOnStartDragListener(l: (RecyclerView.ViewHolder) -> Unit = {}) {
		dragListener = l
	}

	fun setOnItemMoveListener(l: (Int, Int) -> MutableList<Directory>) {
		onItemMoveListener = l
	}

	fun setOnItemDismissListener(l: (Int) -> Unit) {
		onItemDismissListener = l
	}
}