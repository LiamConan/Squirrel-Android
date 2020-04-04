package com.squirrel.presentation.main.accounts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squirrel.R
import com.squirrel.core.domain.Account
import com.squirrel.presentation.main.ItemTouchHelperAdapter

class AccountAdapter(private var accountEntities: MutableList<Account>) :
	RecyclerView.Adapter<AccountAdapter.ViewHolder>(),
	ItemTouchHelperAdapter {

	private var itemClickListener: (Int) -> Unit = {}
	private var contextMenuItemClickListener: (Int, Int, Account) -> Unit = { _, _, _ -> }
	private var dragListener: (RecyclerView.ViewHolder) -> Unit = {}
	private var onItemMoveListener: (Int, Int) -> Unit = { _, _ -> }
	private var onItemDismissListener: (Int) -> Unit = { }

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		var container: LinearLayout = view.findViewById(R.id.linearLayout)
		var textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
		var textViewDate: TextView = view.findViewById(R.id.textViewDate)
		var handler: ImageView = view.findViewById(R.id.handle)
	}


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			LayoutInflater.from(parent.context).inflate(
				R.layout.item_account,
				parent,
				false
			)
		)
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.textViewTitle.text = accountEntities[position].title
		holder.textViewDate.text = accountEntities[position].date
		holder.handler.setOnTouchListener { _, event ->
			if (event.actionMasked == MotionEvent.ACTION_DOWN)
				dragListener(holder)
			true
		}
		holder.container.setOnClickListener {
			itemClickListener(position)
		}
		holder.container.setOnCreateContextMenuListener { menu, _, _ ->
			val copyUser = menu.add(R.string.menu_showkeys_copy_username)
			val copyMail = menu.add(R.string.menu_showkeys_copy_email)
			val copyPass = menu.add(R.string.menu_showkeys_copy_password)
			val goToUrl = menu.add(R.string.menu_showkeys_go_to_url)
			copyUser.setOnMenuItemClickListener {
				contextMenuItemClickListener(0, position, accountEntities[position])
				true
			}
			copyMail.setOnMenuItemClickListener {
				contextMenuItemClickListener(1, position, accountEntities[position])
				true
			}
			copyPass.setOnMenuItemClickListener {
				contextMenuItemClickListener(2, position, accountEntities[position])
				true
			}
			goToUrl.setOnMenuItemClickListener {
				contextMenuItemClickListener(3, position, accountEntities[position])
				true
			}
		}
	}

	override fun getItemCount() = accountEntities.size

	override fun onItemMove(from: Int, to: Int) {
		onItemMoveListener(from, to)
		notifyItemMoved(from, to)
	}

	override fun onItemDismiss(position: Int) {
		onItemDismissListener(position)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, itemCount)
	}

	fun updateData(data: MutableList<Account>) {
		accountEntities = data
		notifyDataSetChanged()
	}

	fun setOnItemClickListener(l: (Int) -> Unit) {
		itemClickListener = l
	}

	fun setOnContextMenuitemClickListener(l: (Int, Int, Account) -> Unit) {
		contextMenuItemClickListener = l
	}

	fun setOnStartDragListener(l: (RecyclerView.ViewHolder) -> Unit) {
		dragListener = l
	}

	fun setOnItemMoveListener(l: (Int, Int) -> Unit) {
		onItemMoveListener = l
	}

	fun setOnItemDismissListener(l: (Int) -> Unit) {
		onItemDismissListener = l
	}
}