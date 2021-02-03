package com.layne.squirrel.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.core.view.setMargins
import com.google.android.material.card.MaterialCardView
import com.layne.squirrel.R
import com.layne.squirrel.core.domain.CreditCard
import kotlinx.android.synthetic.main.view_credit_card.view.*

class CreditCardView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

	companion object {
		private val amexRegex = "^(34|37)".toRegex()
		private val visaRegex = "^4".toRegex()
		private val mastercardRegex = "^(51|52|53|54|55)".toRegex()
		private const val MARGIN = 10
		private const val RADIUS = 16f
		private const val ELEVATION = 10f
		private const val HEIGHT = 220
	}

	private var onChipTouchListener: ((MotionEvent) -> Unit)? = null

	inline var number: String
		get() = textViewNumber.text.toString()
		set(value) {
			textViewNumber.text = value.chunked(4).joinToString(" ")
			imageViewBrand.setImageResource(brand)
		}
	private inline var nameHolder: String
		get() = textViewName.text.toString()
		set(value) {
			textViewName.text = value
		}
	inline var expiry: String
		get() = textViewExpiry.text.toString()
		set(value) {
			textViewExpiry.text = value.chunked(2).mapIndexed { i, it ->
				if (i == 0 && it.toInt() > 12) "12"
				else it
			}.joinToString("/")
		}
	inline var cvv: String
		get() = textViewCVV.text.toString()
		set(value) {
			textViewCVV.text = value
		}

	val brand: Int
		get() = when {
			amexRegex.containsMatchIn(number)       -> R.drawable.drawable_amex
			visaRegex.containsMatchIn(number)       -> R.drawable.drawable_visa
			mastercardRegex.containsMatchIn(number) -> R.drawable.drawable_mastercard
			else                                    -> 0
		}

	init {
		LayoutInflater.from(context)?.inflate(R.layout.view_credit_card, this)
		val scale = context.resources.displayMetrics.density
		val params = LayoutParams(LayoutParams.MATCH_PARENT, (HEIGHT * scale).toInt())
		params.setMargins((MARGIN * scale).toInt())
		layoutParams = params
		radius = RADIUS
		elevation = ELEVATION
		imageViewHandler.setOnTouchListener { view, event ->
			if (event.action == MotionEvent.ACTION_DOWN)
				(onChipTouchListener != null).also { onChipTouchListener?.invoke(event) }
			else if (event.action == MotionEvent.ACTION_UP)
				view.performClick()
			true
		}
	}

	fun setNameHolder(denom: Int, firstname: String, name: String) {
		nameHolder = String.format(
			context.getString(R.string.full_card_holder_name),
			context.resources.getStringArray(R.array.civil_denomination)[denom],
			"$firstname $name"
		)
	}

	fun setOnChipTouchListener(l: (MotionEvent) -> Unit) {
		onChipTouchListener = l
	}

	fun bindData(card: CreditCard) {
		number = card.number
		expiry = card.expiry
		setNameHolder(card.denominaton, card.firstname, card.name)
		cvv = card.cvv
	}
}