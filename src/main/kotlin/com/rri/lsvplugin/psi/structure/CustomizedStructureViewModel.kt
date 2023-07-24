package com.rri.lsvplugin.psi.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.elements.FileElement
import com.rri.lsvplugin.languageElements.factory.CustomizedElementCreator
import com.rri.lsvplugin.psi.ViewCreator
import com.rri.lsvplugin.psi.structure.filters.FieldElementsFilter
import com.rri.lsvplugin.psi.structure.filters.PublicElementsFilter
import com.rri.lsvplugin.psi.visitors.GeneralizedElementVisitor

class CustomizedStructureViewModel(
    private val psiFile: PsiFile,
    private val editor: Editor?,
    private val creator: ViewCreator = ViewCreator(GeneralizedElementVisitor(), CustomizedElementCreator())
) : StructureViewModelBase(psiFile, editor, CustomizedStructureViewElement(FileElement(psiFile), creator)),
    StructureViewModel.ElementInfoProvider {
    init {
        withSorters(Sorter.ALPHA_SORTER)
    }
    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean {
        return true
    }
    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean = false

    override fun getFilters() = FILTERS

    override fun getRoot(): StructureViewTreeElement {
        return CustomizedStructureViewElement(FileElement(psiFile), creator)
    }

    companion object {
        private val FILTERS = arrayOf(PublicElementsFilter, FieldElementsFilter)
    }
}