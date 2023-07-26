package com.rri.lsvplugin.psi.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.util.ArrayUtil
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.ViewCreator


class CustomizedStructureViewElement(
    private var element: BaseElement,
    private val creator: ViewCreator,
) : StructureViewTreeElement {
    override fun getPresentation(): ItemPresentation {
       return element.getPresentableView()
    }

    override fun getChildren(): Array<StructureViewTreeElement> {
        val childrenElements = ArrayList<StructureViewTreeElement>()
        val prevVersionElement = element.clone()
        prevVersionElement.children.clear()
        creator.visitElement(prevVersionElement)
        if (!prevVersionElement.equals(element))
            element = prevVersionElement

        element.children.forEach {childrenElements.add(CustomizedStructureViewElement(it, creator)) }
        return ArrayUtil.toObjectArray(childrenElements, StructureViewTreeElement::class.java)
    }

    override fun navigate(requestFocus: Boolean) = (element.langElement as Navigatable).navigate(requestFocus)

    override fun canNavigate(): Boolean = (element.langElement as Navigatable).canNavigate()

    override fun canNavigateToSource(): Boolean = (element.langElement as Navigatable).canNavigateToSource()

    override fun getValue(): BaseElement = element
}