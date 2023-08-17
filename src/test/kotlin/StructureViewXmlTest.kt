import org.junit.Test

class StructureViewXmlTest : StructureViewBaseTest() {

    @Test
    fun `baseXmlTest`(): Unit = doTest(
        "TestXML.xml", ".customBaseXmlSV.json",
        """
        file text=TestXML.xml /src
         tag text=bookstore specialty='novel'
          tag text=book style='autobiography'
          tag text=book style='textbook'
          tag text=book style='novel' id='myfave'
        """.trimIndent()
    )

    override fun getTestDataPath(): String {
        return "src/test/testData/Xml/"
    }
}