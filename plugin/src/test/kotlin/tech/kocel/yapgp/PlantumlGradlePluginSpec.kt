package tech.kocel.yapgp

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import org.gradle.testfixtures.ProjectBuilder

/**
 * A simple unit test for the yapgp plugin.
 */
class PlantumlGradlePluginSpec : StringSpec({
    "plugin registers task" {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("ru.jflab.gradle.yapgp")

        // Verify the result
        project.tasks.findByName("plantumlAll").shouldNotBeNull()
    }
})
