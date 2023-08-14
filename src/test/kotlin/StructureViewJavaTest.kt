import org.junit.Test

class StructureViewJavaTest : StructureViewBaseTest() {

    @Test
    fun `baseJavaTest`(): Unit = doTest(
        "ElementTesting/BaseTestJava.java", "ElementTesting/.customBaseJavaSV.json",
        """
        file text=BaseTestJava.java
         interface text=BaseTestInterface
          method text=testMethod(int): void
         class text=BaseTestJava
          field text=test: int
        """
    )

    @Test
    fun `textJavaTest`(): Unit = doTest(
        "ElementTesting/BaseTestJava.java", "ElementTesting/.customTextJavaSV.json",
        """
        file text=BaseTestJava.java
         interface text=BaseTestInterface
          method text=testMethod(int testArg): void
         class text=BaseTestJava class- test description
          field text=public static final test: int
        """
    )

    @Test
    fun `displayLevelJavaTest`(): Unit = doTest(
        "ElementTesting/BaseTestJava.java", "ElementTesting/.customDisplayLevelJavaSV.json",
        """
        file text=BaseTestJava.java
         class text=BaseTestJava
         field text=test: int 
         method text=testMethod(int): void
        """
    )

    @Test
    fun `elementFileterJavaTest`(): Unit = doTest(
        "FilterTesting/TestJava.java", "FilterTesting/.customElementFilterSV.json",
        """
        file text=TestJava.java
         interface text=InterfaceTest
        """.trimIndent()
    )

    @Test
    fun `notElementFilterJavaTest`(): Unit = doTest(
        "FilterTesting/TestJava.java", "FilterTesting/.customNotElementFilterSV.json",
        """
        file text=TestJava.java
         interface text=InterfaceTest
        """.trimIndent()
    )

    @Test
    fun `attributeFilterJavaTest`(): Unit = doTest(
        "FilterTesting/TestJava.java", "FilterTesting/.customAttributeFilterSV.json",
        """
        file text=TestJava.java
         class text=FinalTestJava
          method text=print(): void
         class text=TestJava
          method text=testPublicMethod(): void
          field text=testPublicProtectedIntField: int 
          field text=testStringField: String
        """.trimIndent()
    )

    @Test
    fun `visibilityFilterJavaTest`(): Unit = doTest(
        "FilterTesting/TestJava.java", "FilterTesting/.customVisibilityFilterSV.json",
        """
        file text=TestJava.java
         class text=FinalTestJava
          method text=print(): void
         interface text=InterfaceTest
         class text=TestJava
          method text=testPrivateMethod(String): String
          method text=testPublicMethod(): void
          field text=testPublicProtectedIntField: int 
          field text=testStringField: String
        """.trimIndent()
    )

    @Test
    fun `sortingJavaTest`(): Unit = doTest(
        "FilterTesting/TestJava.java", "FilterTesting/.customSortingSV.json",
        """
        file text=TestJava.java
         class text=FinalTestJava
          method text=print(): void
         class text=TestJava
          method text=testPublicMethod(): void
           aClass text=${'$'}0
            method text=print(): void
            field text=sas2: int 
           aClass text=${'$'}1
            method text=print(): void
            field text=sas: int 
           lambda text=lambda${'$'}0 (<lambda expression>)
           lambda text=lambda${'$'}1 (<lambda expression>)
          field text=testPublicProtectedIntField: int 
          field text=testStringField: String 
          field text=testFinalStringField: String 
          field text=testIntField: int 
          method text=testPrivateMethod(String): String
          field text=testProtectedIntField: int 
         interface text=InterfaceTest
        """.trimIndent()
    )


    override fun getTestDataPath(): String {
        return "./src/test/testData/Java/"
    }
}