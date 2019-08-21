package com.github.muhrifqii.parserss.sample

import android.app.Application
import android.util.Log
import com.github.muhrifqii.parserss.ParseRSS
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // initalize ParseRSS with XMLPullParser
        try {
            ParseRSS.init(XmlPullParserFactory.newInstance())
        } catch (e: XmlPullParserException) {
            Log.e("Init App", "Xml Pull Parser newInstance threw an error: ${e.message}")
        }
    }
}