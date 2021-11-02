package francisco.visintini.pmaxml.presentation.formatting.drawable

import francisco.visintini.pmaxml.presentation.formatting.AndroidXmlConstants
import francisco.visintini.pmaxml.presentation.formatting.layout.LayoutFormattingConfig
import java.io.*
import kotlin.system.exitProcess
import org.jdom2.input.SAXBuilder
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter
import org.jdom2.output.support.AbstractXMLOutputProcessor

@Suppress("UNCHECKED_CAST")
class DrawableFormatter(
    private val drawableFileProvider: DrawableFileProvider,
    private val drawableOutputProcessor: DrawableOutputProcessor
) : AbstractXMLOutputProcessor() {

    fun formatDocuments(rootPath: String) {
        try {
            drawableFileProvider.getDrawableFiles(rootPath).forEach {
                formatDocument(it.absolutePath)
            }
        } catch (io: Exception) {
            println("An exception occurred while formating the file")
            println(io.localizedMessage)
            exitProcess(1)
        }
    }

    private fun formatDocument(filePath: String) {
        val doc = SAXBuilder().build(FileInputStream(filePath))
        val outputter =
            XMLOutputter(
                Format.getPrettyFormat().apply {
                    indent = LayoutFormattingConfig.INDENT_SPACE
                    lineSeparator = AndroidXmlConstants.LINE_BREAK
                    encoding = AndroidXmlConstants.UTF_8
                },
                drawableOutputProcessor)
        val stream = ByteArrayOutputStream()
        outputter.output(doc, stream)
        val content = stream.toByteArray()
        FileOutputStream(filePath).write(content, 0, content.size - 1)
    }
}