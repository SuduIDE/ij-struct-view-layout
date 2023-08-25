import org.junit.Test

class StructureViewXmlTest : StructureViewBaseTest() {

    @Test
    fun `baseXmlTest`(): Unit = doTest(
        "TestXML.xml", ".customBaseXmlSV.json",
        """
        file text=TestXML.xml /src
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
        """.trimIndent()
    )

    @Test
    fun `multiPropertyTest`(): Unit = doTest(
        "TestXML.xml", ".customMultiPropertyXmlSV.json",
        """
        file text=TestXML.xml /src
         bookTag text=Something book:bookstore specialty='novel'
          tag text=magazine style='glossy' frequency='monthly'
          bookTag text=Something book:book style='autobiography'
           tag text=author
           tag text=price
           tag text=title
          bookTag text=Something book:book style='textbook'
           tag text=price
           tag text=title
          bookTag text=Something book:book style='novel' id='myfave'
           tag text=price intl='canada' exchange='0.7'
           tag text=title
        """.trimIndent()
    )

    @Test
    fun `multiPropertyWithNotMatchedDisplayLevelTest`(): Unit = doTest(
        "TestXML.xml", ".customMultiPropertyWithNotMatchedValueXmlSV.json",
        """
        file text=TestXML.xml /src
         priceTag text=12
         priceTag text=55
         priceTag text=6.50
         titleTag text=History of Trenton
         titleTag text=Seven Years in Trenton
         titleTag text=Trenton Today, Trenton Tomorrow
        """.trimIndent()
    )

    override fun getTestDataPath(): String {
        return "src/test/testData/Xml/"
    }
}