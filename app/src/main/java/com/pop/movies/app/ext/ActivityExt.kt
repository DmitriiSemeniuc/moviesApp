package com.pop.movies.app.ext

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.google.android.material.snackbar.Snackbar
import com.pop.movies.app.R

fun AppCompatActivity.addFragment(
    containerViewId: Int,
    fragment: Fragment,
    tag: String,
    addToBackStack: Boolean,
    withAnimation: Boolean = false
) {

    if (withAnimation) {
        fragment.enterTransition = Fade(Fade.IN)
        fragment.exitTransition = Fade(Fade.OUT)
    }

    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(containerViewId, fragment, tag)
    if(addToBackStack) {
        transaction.addToBackStack(tag)
    }
    transaction.commitAllowingStateLoss()
}

fun AppCompatActivity.replaceFragment(
    containerViewId: Int,
    fragment: Fragment,
    tag: String,
    addToBackStack: Boolean,
    withAnimation: Boolean = false
) {

    if (withAnimation) {
        fragment.enterTransition = Fade(Fade.IN)
        fragment.exitTransition = Fade(Fade.OUT)
    }

    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(containerViewId, fragment, tag)
    if(addToBackStack) {
        transaction.addToBackStack(tag)
    }
    transaction.commitAllowingStateLoss()
}

fun <T> AppCompatActivity.findFragmentByTag(bundle: Bundle, tag: String) : T? {
    supportFragmentManager.getFragment(bundle, tag)?.let {
        return it as T
    }
    return null
}

fun AppCompatActivity.saveFragmentsState(tag: String, bundle: Bundle) {
    supportFragmentManager.findFragmentByTag(tag)?.let { f ->
        supportFragmentManager.putFragment(bundle, tag, f)
    }
}

fun AppCompatActivity.replaceOrShowFragment(container: Int, fragment: Fragment, tag: String, addToBackStack: Boolean = true) {

    fragment.enterTransition = Fade(Fade.IN)
    fragment.exitTransition = Fade(Fade.OUT)

    supportFragmentManager.beginTransaction().apply {

        if (fragment.isAdded) {
            show(fragment)
        } else {
            replaceFragment(container, fragment, tag, addToBackStack)
        }

    }.commit()
}

fun AppCompatActivity.addOrShowFragment(container: Int, fragment: Fragment, tag: String, addToBackStack: Boolean = true) {

    fragment.enterTransition = Fade(Fade.IN)
    fragment.exitTransition = Fade(Fade.OUT)

    supportFragmentManager.beginTransaction().apply {

        if (fragment.isAdded) {
            show(fragment)
        } else {
            addFragment(container, fragment, tag, addToBackStack)
        }

    }.commit()
}

fun AppCompatActivity.removeFragment(fragmentTag: String) {
    for (fragment in supportFragmentManager.fragments) {
        if (fragment != null && fragment.tag == fragmentTag) {
            supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
            return
        }
    }
}

fun AppCompatActivity.isLandscape() : Boolean {
    return this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun AppCompatActivity.isTablet(): Boolean {
    return resources.configuration.smallestScreenWidthDp >= 600
}

fun AppCompatActivity.makeSnackBar(
    parentView: View,
    text: String,
    actionText: String,
    onAction: () -> Unit,
    duration: Int = Snackbar.LENGTH_INDEFINITE
): Snackbar {
    val bar = Snackbar.make(parentView, "", duration)
    val barLayout: Snackbar.SnackbarLayout = bar.view as Snackbar.SnackbarLayout

    val layout = layoutInflater.inflate(R.layout.layout_snackbar, null).apply {
        findViewById<TextView>(R.id.tv_message).text = text
        val actionButton = findViewById<Button>(R.id.btn_action)
        with(actionButton) {
            setText(actionText)
            setOnClickListener {
                onAction()
                bar.dismiss()
            }
        }
    }

    with(barLayout) {
        val snackBarPadding = resources.getDimensionPixelSize(R.dimen.snack_bar_padding)
        val snackBarPaddingBottom = resources.getDimensionPixelSize(R.dimen.snack_bar_padding_bottom)
        setPadding(snackBarPadding, snackBarPadding, snackBarPadding, snackBarPaddingBottom)
        setBackgroundColor(Color.TRANSPARENT)
        addView(layout)
    }

    return bar
}

