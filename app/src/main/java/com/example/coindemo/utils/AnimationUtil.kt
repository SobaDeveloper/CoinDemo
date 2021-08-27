package com.example.coindemo.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.example.coindemo.utils.ViewExtensions.gone
import com.example.coindemo.utils.ViewExtensions.visible

object AnimationUtil {

    fun View.crossFadeTo(endView: View){
        endView.fadeIn()
        this.fadeOut()
    }

    fun View.fadeIn() =
        this.apply {
            alpha = 0f
            visible()
            animate().alpha(1f)
                .setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
                .setListener(null)
        }

    fun View.fadeOut() =
        this.apply {
            animate().alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
                .setListener(object: AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                        gone()
                    }
                })
        }
}