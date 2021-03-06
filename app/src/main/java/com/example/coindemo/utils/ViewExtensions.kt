package com.example.coindemo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import com.example.coindemo.R
import com.google.android.material.snackbar.Snackbar

object ViewExtensions {

    @ColorInt
    @SuppressLint("Recycle")
    fun Context.themeColor(
        @AttrRes themeAttrId: Int
    ): Int {
        return obtainStyledAttributes(
            intArrayOf(themeAttrId)
        ).use {
            it.getColor(0, Color.MAGENTA)
        }
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }

    fun Context.color(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

    fun String.appendPercentage() = "$this %"

    val Int.dp
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun Float.dp(displayMetrics: DisplayMetrics) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)

    fun String.showErrorSnackBar(view: View) =
        Snackbar.make(view, this, Snackbar.LENGTH_LONG).show()

    fun TextView.setColor(bullish: Boolean) {
        if (bullish) {
            setTextColor(context.getColor(R.color.green))
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_green, 0)
        } else {
            setTextColor(context.getColor(R.color.red))
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_red, 0)
        }
    }
}