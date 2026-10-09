package jp.kukv.portfolio.screens.about

import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AboutContentTest {
    @Test
    fun decodesAllFields() {
        val text =
            """
            {
              "about": "First paragraph.\n\nSecond paragraph.",
              "skills": [
                { "label": "Languages", "items": ["Kotlin", "TypeScript"] }
              ],
              "experiences": [
                {
                  "period": "2022.04 — Present",
                  "role": "Software Engineer",
                  "organization": "SaaS company (300 employees)",
                  "description": "Built things.",
                  "technologies": ["Kotlin"]
                }
              ]
            }
            """.trimIndent()

        val expected =
            AboutContent(
                about = "First paragraph.\n\nSecond paragraph.",
                skills = listOf(SkillCategory(label = "Languages", items = listOf("Kotlin", "TypeScript"))),
                experiences =
                    listOf(
                        Experience(
                            period = "2022.04 — Present",
                            role = "Software Engineer",
                            organization = "SaaS company (300 employees)",
                            description = "Built things.",
                            technologies = listOf("Kotlin"),
                        ),
                    ),
            )
        assertEquals(expected, decodeAboutContent(text))
    }

    @Test
    fun missingFieldFails() {
        val text = """{ "about": "Hi", "skills": [] }"""

        assertFailsWith<SerializationException> { decodeAboutContent(text) }
    }

    @Test
    fun unknownFieldFails() {
        val text = """{ "about": "Hi", "skills": [], "experiences": [], "company": "X" }"""

        assertFailsWith<SerializationException> { decodeAboutContent(text) }
    }
}
