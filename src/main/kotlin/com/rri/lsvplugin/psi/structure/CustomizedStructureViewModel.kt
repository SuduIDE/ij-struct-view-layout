package com.rri.lsvplugin.psi.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.rri.lsvplugin.languageElements.builders.ElementBuilder
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.elements.FileElement
import com.rri.lsvplugin.languageElements.factory.ElementFactory
import com.rri.lsvplugin.languageElements.factory.IPresentableViewFactory
import com.rri.lsvplugin.languageElements.factory.PresentableViewFactoryDefaultImpl
import com.rri.lsvplugin.psi.ViewCreator
import com.rri.lsvplugin.psi.visitors.JavaElementLangVisitor

class CustomizedStructureViewModel(
    private val psiFile: PsiFile,
    private val editor: Editor?,
    private val creator: ViewCreator = ViewCreator(ElementFactory(), PresentableViewFactoryDefaultImpl(),  JavaElementLangVisitor(), ElementBuilder())
) : StructureViewModelBase(psiFile, editor, CustomizedStructureViewElement(FileElement(psiFile), creator)),
    StructureViewModel.ElementInfoProvider {
    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean {
        val value = element?.value as BaseElement
        return value.getChildren().isNotEmpty()
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean = false

    override fun getFilters() = FILTERS

    override fun getRoot(): StructureViewTreeElement {
        return CustomizedStructureViewElement(FileElement(psiFile), creator)
    }

    companion object {
        private val FILTERS = arrayOf<Filter>()
    }
}