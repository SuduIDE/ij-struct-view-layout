import org.junit.Test

class StructureViewXmlTest : StructureViewBaseTest() {

    @Test
    fun `baseXmlTest`() : Unit = doTest("TestXML.xml", ".customBaseXmlSV.json",
        """
        file text=TestXML.xml
         tag text=bookstore specialty='novel'
          tag text=book style='autobiography'
           tag text=author
           tag text=price
           tag text=title
          tag text=book style='textbook'
           tag text=price
           tag text=title
          tag text=book style='novel' id='myfave'
           tag text=price intl='canada' exchange='0.7'
           tag text=title
          tag text=magazine style='glossy' frequency='monthly'
        """.trimIndent())

    override fun getTestDataPath(): String {
        return "src/test/testData/Xml/"
    }
}