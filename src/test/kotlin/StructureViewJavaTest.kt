import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.ide.util.treeView.smartTree.SmartTreeStructure
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.Queryable
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.*
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewModel
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl
import junit.framework.TestCase
import org.junit.Test
import kotlin.io.path.Path

class StructureViewJavaTest : StructureViewBaseTest() {

    @Test
    fun `baseJavaTest`(): Unit = doTest("TestJava.java", ".customBaseJavaSV.json",
        """
        file text=TestJava.java
         class text=FinalTestJava
          method text=print(): void
         interface text=InterfaceTest
         class text=TestJava
          field text=testFinalStringField: String
          field text=testIntField: int
          method text=testPrivateMethod(String): String
          field text=testProtectedIntField: int
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
        """.trimIndent())

    @Test
    fun `filterNonPublicJavaTest`() : Unit = doTest("TestJava.java", ".customWithNonPublicFiltersJavaSV.json",
        """
        file text=TestJava.java
         class text=FinalTestJava
          method text=print(): void
         interface text=InterfaceTest
         class text=TestJava
          method text=testPublicMethod(): void
          field text=testPublicProtectedIntField: int
        """.trimIndent())

    @Test
    fun `filterFieldLambdaJavaTest`(): Unit = doTest("TestJava.java", ".customWithElementFiltersJavaSV.json",
        """
        file text=TestJava.java
         class text=FinalTestJava
          method text=print(): void
         interface text=InterfaceTest
         class text=TestJava
          field text=testFinalStringField: String
          field text=testIntField: int
          method text=testPrivateMethod(String): String
          field text=testProtectedIntField: int
          method text=testPublicMethod(): void
           aClass text=${'$'}0
            method text=print(): void
            field text=sas2: int
           aClass text=${'$'}1
            method text=print(): void
            field text=sas: int
          field text=testStringField: String
        """.trimIndent())

    @Test
    fun `propertyRegexpJavaTest`(): Unit = doTest("TestJava.java", ".customWithRegexpPropertyJavaSV.json",
        """
        file text=TestJava.java
         aClass text=${'$'}0
         aClass text=${'$'}1
         interface text=InterfaceTest
        """.trimIndent())

    @Test
    fun `displayLevelJavaTest`(): Unit = doTest("TestJava.java", ".customDisplayLevelJavaSV.json",
        """
        file text=TestJava.java
         class text=FinalTestJava
          method text=print(): void
         interface text=InterfaceTest
         field text=testFinalStringField: String
         field text=testIntField: int
         class text=TestJava
          method text=testPrivateMethod(String): String
          method text=testPublicMethod(): void
           field text=sas2: int
           field text=sas: int
         field text=testProtectedIntField: int
         field text=testPublicProtectedIntField: int
         field text=testStringField: String
        """.trimIndent())
    override fun getTestDataPath(): String {
        return "./src/test/testData/Java/"
    }
}