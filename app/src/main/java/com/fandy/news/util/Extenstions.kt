package com.fandy.news.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fandy.news.R
import java.text.SimpleDateFormat
import java.util.*


/**
 *  Helper function that removes time from a given date-time String.
 */
fun String.formatDateRemoveTime(): String {
    var toBeReturned = this
    val key = "T"

    if (this.isNotEmpty()) {
        val index = if (this.contains(key)) this.indexOf(key) else this.lastIndex
        toBeReturned = substring(0, index)
    }
    return toBeReturned
}

fun String.formatDate(): String {
    var toBeReturned = this

    if (this.isNotEmpty()) {
        val date = getDate(this);
        if(date == null) {
            return toBeReturned
        }
        toBeReturned = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH)
            .format(date)
    }
    return toBeReturned
}

private fun getDate(dateStr: String): Date? {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val mDate = formatter.parse(dateStr) // this never ends while debugging
        mDate
    } catch (e: Exception) {
        null
    }
}

/**
 *  Helper function that removes unnecessary text from the article content.
 */
fun String.formatContent(): String {
    var toBeReturned = this
    val key = "["

    if (this.isNotEmpty()) {
        val index = if (this.contains(key)) this.indexOf(key) else this.lastIndex
        toBeReturned = substring(0, index)
    }
    return toBeReturned
}

/**
 *  Helper function that removes unnecessary text from the article title.
 */
fun String.formatTitle(): String {
    var toBeReturned = this
    val key = " - "

    if (this.isNotEmpty()) {
        val index = if (this.contains(key)) this.indexOf(key) else this.lastIndex
        toBeReturned = substring(0, index)
    }
    return toBeReturned
}

/**
 *  Helper function that either load text into the
 *  TextView or hides the view, if the text is empty
 */
fun TextView.loadOrGone(dataText: String) {
    if (dataText.isNotEmpty())
        text = dataText
    else this.isGone = true
}

/**
 *  Helper function that either loads article image
 *  or loads the default logo, if the image url is
 *  not available.
 */
fun ImageView.loadImageOrDefault(imgUrl: String) {
    if (imgUrl.isNotEmpty())
        Glide.with(context)
            .load(imgUrl)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_load))
            .into(this)
    else
        Glide.with(this).asGif().load(R.drawable.ic_loading).into(this);
}