package com.example.analyticsproxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class AnalyticsProxyInvocationHandler(private val analyticsTracker: AnalyticsTracker) :
    InvocationHandler {

    //args null when app has no parameters
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        //need to turn off in runtime
        checkAnalyticsMethod(method)

        val eventName: EventName = method.annotations.firstNotNullOf { it as? EventName }
        val analyticsEventName = eventName.value
        if (method.parameterCount == 0) {
            analyticsTracker.trackEvent(analyticsEventName)
        } else {
            checkNotNull(args)
            val annotations = method.parameterAnnotations
            val analyticsParametersName = annotations.map { paramAnnotations ->
                paramAnnotations.firstNotNullOf { it as? Param }.value
            }
            val analyticsParam = buildMap {
                repeat(method.parameterCount) { index ->
                    put(analyticsParametersName[index], args[index])
                }
            }
            analyticsTracker.trackEvent(analyticsEventName, analyticsParam)
        }
        return Unit
    }

    private fun checkAnalyticsMethod(method: Method) {
        check(method.annotations.any { it is EventName }) {
            "Analytics function has no EventName annotation"
        }
        method.parameterAnnotations.forEach { paramAnnotations ->
            check(paramAnnotations.any { it is Param }) {
                "Analytics function has no Param annotation"
            }
        }
    }

    interface AnalyticsTracker {

        fun trackEvent(eventName: String, params: Map<String, Any>? = null)
    }
}