package com.layne.squirrel.presentation.main.directories

import android.annotation.SuppressLint
import android.view.ContextMenu
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.layne.squirrel.core.domain.Directory
import com.layne.squirrel.presentation.ItemTouchHelperCallback
import com.layne.squirrel.presentation.main.ItemTouchHelperAdapter
import com.layne.squirrel.presentation.widget.DirectoryView

class DirectoryAdapter(private var items: List<Directory>) :
	RecyclerView.Adapter<DirectoryAdapter.ViewHolder>(),
	ItemTouchHelperAdapter {

	private var touchHelper: ItemTouchHelper? = null
	private var itemClickListener: (Int) -> Unit = {}
	private var createContextMenuListener: (ContextMenu, Directory, Int) -> Unit = { _, _, _ -> }
	private var onItemMoveListener: (Int, Int) -> Unit = { _, _ -> }
	private var onItemDismissListener: (Int) -> Unit = { }

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		ViewHolder(DirectoryView(parent.context))

	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		super.onAttachedToRecyclerView(recyclerView)

		touchHelper = ItemTouchHelper(ItemTouchHelperCallback(this)).apply {
			attachToRecyclerView(recyclerView)
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.view.bindData(items[position])
		holder.view.setOnHandlerTouchListener { event ->
			if (event.actionMasked == MotionEvent.ACTION_DOWN)
				touchHelper?.startDrag(holder)
		}
		holder.view.setOnClickListener { itemClickListener(holder.adapterPosition) }
		holder.view.setOnCreateContextMenuListener { menu, _, _ ->
			createContextMenuListener(menu, items[position], position)
		}
	}

	override fun getItemCount() = items.size

	override fun onItemMove(from: Int, to: Int) {
		onItemMoveListener(from, to)
		notifyItemMoved(from, to)
	}

	override fun onItemDismiss(position: Int) {
		onItemDismissListener(position)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, itemCount)
	}

	fun updateData(data: List<Directory>) {
		items = data
		notifyDataSetChanged()
		notifyItemInserted(data.size - 1)
	}

	fun setOnItemClickListener(l: (Int) -> Unit) {
		itemClickListener = l
	}

	fun setOnItemMoveListener(l: (Int, Int) -> Unit) {
		onItemMoveListener = l
	}

	fun setOnItemDismissListener(l: (Int) -> Unit) {
		onItemDismissListener = l
	}

	fun setOnCreateContextMenuListener(l: (ContextMenu, Directory, Int) -> Unit) {
		createContextMenuListener = l
	}

	class ViewHolder(val view: DirectoryView) : RecyclerView.ViewHolder(view)
}