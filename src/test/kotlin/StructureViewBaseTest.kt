import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.ide.util.treeView.smartTree.SmartTreeStructure
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.Queryable
import com.intellij.psi.PsiFile
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewModel
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl
import junit.framework.TestCase
import kotlin.io.path.Path

abstract class StructureViewBaseTest : LightPlatformCodeInsightFixture4TestCase() {
    protected fun doTest(fileName: String, jsonName: String, expected: String) {
        val testVF = myFixture.configureByFile(fileName)
        project.service<JsonSvContainerServiceImpl>().loadCurrentVersion(Path(testDataPath).resolve(jsonName))

        val tree = createTestStructureView(testVF)
        val normExpected = expected.trimIndent()
        assertTreeEquals(tree, normExpected)
    }

    private fun createTestStructureView(testVF: PsiFile): SmartTreeStructure {
        val structureViewBuilder = StructureViewBuilder.PROVIDER.getStructureViewBuilder(
            testVF.fileType,
            testVF.virtualFile, myFixture.project
        )
        val structureViewModel = (structureViewBuilder as TreeBasedStructureViewBuilder).createStructureViewModel(null)
        return SmartTreeStructure(project, structureViewModel as CustomizedStructureViewModel)
    }

    private fun assertTreeEquals(abstractTreeStructure: SmartTreeStructure, expected: String) {
        val printInfo = Queryable.PrintInfo(
            arrayOf("element"),
            arrayOf("text")
        )
        val stringPresentationTree = PlatformTestUtil.print(
            abstractTreeStructure,
            abstractTreeStructure.rootElement,
            0,
            null,
            -1,
            ' ',
            printInfo
        )

        TestCase.assertEquals(expected.trim(), stringPresentationTree.trim())
    }

}