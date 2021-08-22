package com.example.coindemo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

class CustomScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

    var enableScrolling = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean =
        if (enableScrolling) super.onTouchEvent(ev)
        else false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean =
        if (enableScrolling) super.onInterceptTouchEvent(ev)
        else false
}