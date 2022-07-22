package com.paylivre.sdk.gateway.android.services.log

import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel

class LogErrorServiceImpl : LogErrorService {
    override fun setExtra(key: String, value: String) {
        Sentry.setExtra(key, value)
    }

    override fun captureMessage(message: String) {
        Sentry.captureMessage(message)
    }

    override fun addSentryBreadcrumb(category: String?, message: String?) {
        Sentry.setExtra(category.toString(), message.toString())
        val breadcrumb = Breadcrumb()
        breadcrumb.category = category.toString()
        breadcrumb.message = message.toString()
        breadcrumb.level = SentryLevel.INFO
        Sentry.addBreadcrumb(breadcrumb)
    }

    override fun configureScope(logErrorScope: LogErrorScope) {
        Sentry.configureScope { scope ->
            logErrorScope.getTags().map { tag ->
                scope.setTag(tag.first, tag.second)
            }

            logErrorScope.getContexts().map { context ->
                scope.setContexts(context.first, context.second)
            }
        }
    }

}


class LogErrorScopeImpl : LogErrorScope {
    private val listTags = mutableListOf<Pair<String, String>>()
    private val listContexts = mutableListOf<Pair<String, Any>>()

    override fun setTag(key: String, value: String) {
        listTags.add(Pair(key, value))
    }

    override fun setContexts(key: String, value: Any) {
        listContexts.add(Pair(key, value))
    }

    override fun getTags(): List<Pair<String, String>> {
        return listTags
    }

    override fun getContexts(): List<Pair<String, Any>> {
        return listContexts
    }
}