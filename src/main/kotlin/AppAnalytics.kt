package com.example.javadynamicproxy

import com.example.analyticsproxy.EventName
import com.example.analyticsproxy.Param

interface AppAnalytics {

    @EventName("appStart")
    fun trackAppStart()

    @EventName("clickCount")
    fun trackClickCounter(@Param("count") count: Int)
}