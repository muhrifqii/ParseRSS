package com.github.muhrifqii.parserss

import com.github.muhrifqii.parserss.element.toPrefixNamedElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object ModelTest {

    class RSSFeedTest {

        @Test
        fun `RSSFeedObject initialization`() {
            val feed = RSSFeedObject(
                title = "Test Feed",
                description = "Description"
            )

            assertEquals("Test Feed", feed.title)
            assertEquals("Description", feed.description)
            assertTrue(feed.items.isEmpty())
        }

        @Test
        fun `RSSItemObject initialization`() {
            val item = RSSItemObject(
                title = "Item Title",
                link = "https://example.com"
            )

            assertEquals("Item Title", item.title)
            assertEquals("https://example.com", item.link)
            assertTrue(item.media.isEmpty())
            assertTrue(item.category.isEmpty())
        }

        @Test
        fun `MediaType enum conversion`() {
            assertEquals(MediaType.Image, MediaType.from("image"))
            assertEquals(MediaType.Video, MediaType.from("video"))
            assertEquals(MediaType.Unspecified, MediaType.from("unknown"))
        }

        @Test
        fun `GUId data class`() {
            val guid = GUId("https://example.com/1", isPermaLink = true)
            assertEquals("https://example.com/1", guid.value)
            assertTrue(guid.isPermaLink)
        }

        @Test
        fun `RSSFeedObject can add items`() {
            val feed = RSSFeedObject()
            val item = RSSItemObject(title = "Item 1")

            feed.items.add(item)

            assertEquals(1, feed.items.size)
            assertEquals("Item 1", feed.items.first().title)
        }
    }

    class XmlElementTest {
        @Test
        fun `should correctly get the namespace over an element name`() {
            assertEquals("", "item".toPrefixNamedElement().prefix)
            assertEquals("xmlns", "xmlns:rdf".toPrefixNamedElement().prefix)
            assertEquals("abc_def", "abc_def:rdf".toPrefixNamedElement().prefix)
        }

        @Test
        fun `should correctly get the element name`() {
            assertEquals("item", "item".toPrefixNamedElement().name)
            assertEquals("rdf", "xmlns:rdf".toPrefixNamedElement().name)
        }
    }
}