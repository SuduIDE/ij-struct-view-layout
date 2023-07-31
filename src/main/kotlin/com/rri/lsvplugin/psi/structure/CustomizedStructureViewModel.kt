package com.rri.lsvplugin.psi.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.languageElements.factory.elementCreator.CustomizedElementCreator
import com.rri.lsvplugin.psi.ViewCreator
import com.rri.lsvplugin.psi.visitors.GeneralizedElementVisitor

class CustomizedStructureViewModel(
    private val psiFile: PsiFile,
    private val editor: Editor?,
    private val creator: ViewCreator = ViewCreator(GeneralizedElementVisitor(), CustomizedElementCreator())
) : StructureViewModelBase(psiFile, editor, CustomizedStructureViewElement(BaseElement(psiFile), creator)),
    StructureViewModel.ElementInfoProvider {
    init {
        withSorters(Sorter.ALPHA_SORTER)
    }
    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean {
        return (element!!.value as BaseElement).children.isNotEmpty()
    }
    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean = false

    override fun isSuitable(element: PsiElement?): Boolean {
        return super.isSuitable(element)
    }

    override fun getFilters(): Array<out Filter> {
        val filterList = creator.createFilters(psiFile)
        return Array(filterList.size) {
            filterList[it]
        }
    }

    override fun getRoot(): StructureViewTreeElement {
        val fileElement = BaseElement(psiFile)
        fileElement.elementType = "file"
        return CustomizedStructureViewElement(fileElement, creator)
    }
}