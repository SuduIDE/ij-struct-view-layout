package com.rri.lsvplugin.psi.structure

import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.rri.lsvplugin.utils.JsonContainerUtil

class CustomizedStructureViewFactory : PsiStructureViewFactory {

    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder {
        val jsonUtil = JsonContainerUtil()
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return CustomizedStructureViewModel(psiFile, editor, jsonUtil)
            }

            override fun isRootNodeShown(): Boolean = jsonUtil.isShowFile(psiFile)
        }
    }
}