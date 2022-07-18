package com.paylivre.sdk.gateway.android.services.log

import com.amplitude.api.Amplitude
import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Types
import io.sentry.Sentry
import org.json.JSONException
import org.json.JSONObject

class LogEventsServiceImpl {
    companion object : LogEventsService {
        private val customEventAnalytics = JSONObject()

        override fun setLogEventAnalytics(eventName: String?) {
            if (eventName != null) {
                Amplitude.getInstance().logEvent(eventName)
            }
        }

        override fun setLogEventAnalyticsWithParams(
            eventName: String,
            vararg params: Pair<String, String>?,
        ) {
            try {
                for (param in params) {
                    if (param?.first != null && param?.second != null) {
                        val paramKey = param.first
                        val paramValue = param.second
                        customEventAnalytics.put(paramKey, paramValue)
                        Sentry.setExtra(paramKey, paramKey)
                    }

                }
            } catch (e: JSONException) {
                System.err.println("Invalid JSON")
                e.printStackTrace()
            }
            Amplitude.getInstance().logEvent(eventName, customEventAnalytics)
        }

        override fun setLogFinishScreen(operation: Operation, type: Types) {
            this.setLogEventAnalyticsWithParams(
                "Screen_TransactionFinish",
                Pair("operation", operation.code.toString()),
                Pair("operation_name", operation.toString().lowercase()),
                Pair("selected_type", type.code.toString()),
                Pair("selected_type_name", type.toString().lowercase()),
                Pair("operation_and_selected_type",
                    operation.toString().lowercase() + "_" + type.toString().lowercase()),
            )
        }
    }
}