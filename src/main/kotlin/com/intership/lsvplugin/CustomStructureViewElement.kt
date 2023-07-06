package com.intership.lsvplugin

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ArrayUtil

class CustomStructureViewElement(private val element: NavigatablePsiElement): StructureViewTreeElement {
    override fun getPresentation(): ItemPresentation {
        val presentation = element.presentation
        return presentation ?: PresentationData()
    }

    override fun getChildren(): Array<out StructureViewTreeElement> {
        val childrenElements = ArrayList<StructureViewTreeElement>()
        return ArrayUtil.toObjectArray(childrenElements, StructureViewTreeElement::class.java)
    }

    override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)

    override fun canNavigate(): Boolean = element.canNavigate()

    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

    override fun getValue(): Any = element
}