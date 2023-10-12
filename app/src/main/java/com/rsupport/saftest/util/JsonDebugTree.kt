package com.rsupport.saftest.util

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

class JsonDebugTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isJson(message)) {
            try {
                val json = JSONObject(message)
                val formatted = json.toString(4) // 4 is the number of spaces to use for indentation
                super.log(priority, tag, formatted, t)
                return
            } catch (e: JSONException) {
                // It's not a JSON object, try parsing it as an array
            }

            try {
                val jsonArray = JSONArray(message)
                val formatted = jsonArray.toString(4) // 4 is the number of spaces to use for indentation
                super.log(priority, tag, formatted, t)
            } catch (e: JSONException) {
                super.log(priority, tag, message, t)
            }
        } else {
            super.log(priority, tag, message, t)
        }
    }

    private fun isJson(message: String): Boolean {
        return message.trim { it <= ' ' }.startsWith("{") || message.trim { it <= ' ' }.startsWith("[")
    }
}