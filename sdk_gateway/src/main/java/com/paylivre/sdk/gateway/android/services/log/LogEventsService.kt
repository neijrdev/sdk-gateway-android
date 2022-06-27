package com.paylivre.sdk.gateway.android.services.log

import com.paylivre.sdk.gateway.android.domain.model.Operation
import com.paylivre.sdk.gateway.android.domain.model.Types

interface LogEventsService {
    fun setLogEventAnalytics(eventName: String?)
    fun setLogEventAnalyticsWithParams(
        eventName: String,
        vararg params: Pair<String, String>?,
    )
    fun setLogFinishScreen(
        operation: Operation,
        type: Types,
    )
}