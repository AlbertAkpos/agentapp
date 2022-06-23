package com.youverify.agent_app_android.util

import androidx.lifecycle.Observer

class SingleEvent<T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    override fun toString(): String {
        return "Event(content=$content,hasBeenHandled=$hasBeenHandled)"
    }

    /**
     * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
     * already been handled.
     *
     * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
     */
    class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<SingleEvent<T>> {
        override fun onChanged(event: SingleEvent<T>?) {
            event?.getContentIfNotHandled()?.let { value ->
                onEventUnhandledContent(value)
            }
        }
    }

    companion object {

        // we don't want an event if there's no data
        fun <T> dataEvent(data: T?): SingleEvent<T>? {
            data?.let {
                return SingleEvent(it)
            }
            return null
        }

        // we don't want an event if there is no message
        fun messageEvent(message: String?): SingleEvent<String>? {
            message?.let {
                return SingleEvent(message)
            }
            return null
        }
    }


}
