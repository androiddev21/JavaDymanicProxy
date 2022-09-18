package com.example.javadynamicproxy

import com.example.analyticsproxy.AnalyticsProxy
import com.example.analyticsproxy.AnalyticsProxyInvocationHandler
import com.example.analyticsproxy.create
import kotlin.system.measureNanoTime

fun main() {
    val analyticsProxy = AnalyticsProxy.Builder()
        .analyticsTracker(LogAnalyticsTracker())
        .build()

    val appAnalytics: AppAnalytics = analyticsProxy.create()
    appAnalytics.trackAppStart()
    appAnalytics.trackClickCounter(5)

//    repeat(10){
//        println(measureNanoTime { appAnalytics.trackAppStart() })
//    }
//    repeat(10){
//        println(measureNanoTime { appAnalytics.trackClickCounter(5) })
//    }
}

private class LogAnalyticsTracker : AnalyticsProxyInvocationHandler.AnalyticsTracker {

    override fun trackEvent(eventName: String, params: Map<String, Any>?) {
        if (params.isNullOrEmpty()) {
            println(eventName)
        } else {
            println("$eventName ($params)")
        }
    }

}