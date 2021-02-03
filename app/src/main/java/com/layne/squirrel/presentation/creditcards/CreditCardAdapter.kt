package com.layne.squirrel.presentation.creditcards

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.layne.squirrel.core.domain.CreditCard
import com.layne.squirrel.presentation.main.ItemTouchHelperAdapter
import com.layne.squirrel.presentation.widget.CreditCardView

class CreditCardAdapter(private var cards: List<CreditCard>) :
	RecyclerView.Adapter<CreditCardAdapter.ViewHolder>(),
	ItemTouchHelperAdapter {

	private var itemClickListener: (View, Int) -> Unit = { _, _ -> }
	private var dragListener: (RecyclerView.ViewHolder) -> Unit = {}
	private var onItemMoveListener: (Int, Int) -> Unit = { _, _ -> }
	private var onItemDismissListener: (Int) -> Unit = { }

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
		ViewHolder(CreditCardView(parent.context))

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.view.setOnClickListener { itemClickListener(it, holder.adapterPosition) }
		holder.view.bindData(cards[position])
		holder.view.setOnChipTouchListener { event ->
			if (event.actionMasked == MotionEvent.ACTION_DOWN)
				dragListener(holder)
		}
	}

	override fun getItemCount() = cards.size

	override fun onItemMove(from: Int, to: Int) {
		onItemMoveListener(from, to)
		notifyItemMoved(from, to)
	}

	override fun onItemDismiss(position: Int) {
		onItemDismissListener(position)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, itemCount)
	}

	fun setOnItemClickListener(l: (View, Int) -> Unit) {
		itemClickListener = l
	}

	fun setOnStartDragListener(l: (RecyclerView.ViewHolder) -> Unit = {}) {
		dragListener = l
	}

	fun setOnItemMoveListener(l: (Int, Int) -> Unit) {
		onItemMoveListener = l
	}

	fun setOnItemDismissListener(l: (Int) -> Unit) {
		onItemDismissListener = l
	}

	class ViewHolder(val view: CreditCardView) : RecyclerView.ViewHolder(view)
}