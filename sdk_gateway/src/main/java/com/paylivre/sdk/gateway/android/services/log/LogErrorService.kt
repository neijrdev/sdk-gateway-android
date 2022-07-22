package com.paylivre.sdk.gateway.android.services.log


interface LogErrorScope {
    fun getTags(): List<Pair<String, String>>
    fun getContexts(): List<Pair<String, Any>>
    fun setTag(key: String, value: String)
    fun setContexts(key: String, value: Any)
}

interface LogErrorService {
    fun setExtra(key: String, value: String)
    fun captureMessage(message: String)
    fun addSentryBreadcrumb(category: String?, message: String?)
    fun configureScope(logErrorScope: LogErrorScope)
}
