package com.rsupport.saftest.util

import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

class JsonDebugTree(private val logCollector: MutableStateFlow<String>) : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isJson(message)) {
            try {
                logCollector.value += "___________________________file___________________________ + \n"
                val json = JSONObject(message)
                val formatted = json.toString(4)
                logCollector.value += formatted
                logCollector.value += "\n"
                super.log(priority, tag, formatted, t)
                return
            } catch (e: JSONException) {

            }

            try {
                val jsonArray = JSONArray(message)
                val formatted = jsonArray.toString(4)
                logCollector.value += formatted
                logCollector.value += "\n"
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