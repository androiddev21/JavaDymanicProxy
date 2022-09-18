package com.example.analyticsproxy

import java.lang.reflect.Proxy
import kotlin.properties.Delegates.notNull

class AnalyticsProxy private constructor(private val analyticsTracker: AnalyticsProxyInvocationHandler.AnalyticsTracker) {

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> create(clazz: Class<T>): T{
        return Proxy.newProxyInstance(
            clazz.classLoader,
            arrayOf(clazz),
            AnalyticsProxyInvocationHandler(analyticsTracker)
        ) as T
    }

    class Builder{

        //can use delegate to not make nullable
        private var analyticsTracker: AnalyticsProxyInvocationHandler.AnalyticsTracker by notNull()

        fun analyticsTracker(analyticsTracker: AnalyticsProxyInvocationHandler.AnalyticsTracker): Builder{
            this.analyticsTracker = analyticsTracker
            return this
        }

        fun build(): AnalyticsProxy {
            return  AnalyticsProxy(analyticsTracker)
        }
    }
}

inline fun <reified T: Any> AnalyticsProxy.create(): T {
    return create(T::class.java)
}