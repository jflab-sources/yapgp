package tech.kocel.yapgp

import com.github.michaelbull.retry.policy.RetryPolicy
import com.github.michaelbull.retry.policy.fullJitterBackoff
import com.github.michaelbull.retry.policy.stopAtAttempts
import com.github.michaelbull.retry.retry
import kotlinx.coroutines.runBlocking
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.Option
import net.sourceforge.plantuml.OptionFlags
import net.sourceforge.plantuml.Run
import net.sourceforge.plantuml.zopfli.Options
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

class DiagramDownloader(private val serverPath: String = "https://www.plantuml.com/plantuml/") {
    fun downloadFile(
        sourceFile: File,
        fileFormat: FileFormat,
        outputFile: File,
    ) {
        runBlocking {
            val policy: RetryPolicy<Throwable> = RetryPolicy(stopAtAttempts(10), fullJitterBackoff(min = 10L, max = 5000L))
            retry(policy) {
                downloadContent(sourceFile, fileFormat, outputFile)
            }
        }
    }

    private fun downloadContent(
        sourceFile: File,
        fileFormat: FileFormat,
        outputFileName: File,
    ) {
        OptionFlags.getInstance().isSystemExit = false
        Run.main(arrayOf(
            "-headless",
            "-t"+fileFormat.name.lowercase(),
            "-ofile",
            outputFileName.absolutePath,
            sourceFile.absolutePath
        ))

//        url(diagramContent, fileFormat).openStream().use {
//            Channels.newChannel(it).use { rbc ->
//                FileOutputStream(outputFileName).use { fos ->
//                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
//                }
//            }
//        }
    }

    fun url(
        diagramContent: String,
        fileFormat: FileFormat,
    ): URL {
        return URL("$serverPath${fileFormat.name.lowercase()}/~1$diagramContent")
    }
}
