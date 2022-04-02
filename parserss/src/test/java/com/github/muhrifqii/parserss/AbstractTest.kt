package com.github.muhrifqii.parserss

import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.xmlpull.v1.XmlPullParserFactory

@RunWith(value = RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
abstract class AbstractTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun configureClass() {
            ParseRSS.init(XmlPullParserFactory.newInstance())
        }
    }
}
