package com.github.muhrifqii.parserss

import org.junit.Before
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.xmlpull.v1.XmlPullParserFactory

@RunWith(value = RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
abstract class AbstractTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun configureClass() {
            ParseRSS.init(XmlPullParserFactory.newInstance())
        }
    }

    @Before
    abstract fun configure()
}
