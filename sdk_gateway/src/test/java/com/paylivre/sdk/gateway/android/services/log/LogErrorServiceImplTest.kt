package com.paylivre.sdk.gateway.android.services.log

class LogErrorServiceImplTest : LogErrorService {
    var listOfCapturedMessages = mutableListOf<String>()
    val listOfCapturedExtras = mutableListOf<Pair<String, String>>()
    val listOfAddedSentryBreadcrumb = mutableListOf<Pair<String?, String?>>()
    var logErrorScopeReceived: LogErrorScope? = null

    override fun setExtra(key: String, value: String) {
        listOfCapturedExtras.add(Pair(key, value))
    }

    override fun captureMessage(message: String) {
        listOfCapturedMessages.add(message)
    }

    override fun addSentryBreadcrumb(category: String?, message: String?) {
        listOfAddedSentryBreadcrumb.add(Pair(category, message))
    }

    override fun configureScope(logErrorScope: LogErrorScope) {
        logErrorScopeReceived = logErrorScope
    }

}