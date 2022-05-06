package com.paylivre.sdk.gateway.android.data.api

import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel

fun addSentryBreadcrumb(category: String?, message: String?) {
    Sentry.setExtra(category.toString(), message.toString())
    val breadcrumb = Breadcrumb()
    breadcrumb.category = category.toString()
    breadcrumb.message = message.toString()
    breadcrumb.level = SentryLevel.INFO
    Sentry.addBreadcrumb(breadcrumb)
}