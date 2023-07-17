package com.rri.lsvplugin.psi.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.pom.Navigatable
import com.intellij.util.ArrayUtil
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.ViewCreator

class CustomizedStructureViewElement(
    private val element: BaseElement,
    private val creator: ViewCreator,
) : StructureViewTreeElement {
    override fun getPresentation(): ItemPresentation {
        element.createPresentableView()
        return element.presentableView
    }

    override fun getChildren(): Array<StructureViewTreeElement> {
        val childrenElements = ArrayList<StructureViewTreeElement>()
        for (childElement in element.getLangElement().children) {
            val newElement = creator.createElement(childElement)
            if (newElement != null) {
                element.addChild(newElement)
                creator.visitElement(newElement)
                childrenElements.add(CustomizedStructureViewElement(newElement, creator))
            }
        }

//        val childrenElements = element.getChildren().map { CustomizedStructureViewElement(it, creator) }.toList()
        return ArrayUtil.toObjectArray(childrenElements, StructureViewTreeElement::class.java)
    }

    override fun navigate(requestFocus: Boolean) = (element.getLangElement() as Navigatable).navigate(requestFocus)

    override fun canNavigate(): Boolean = (element.getLangElement() as Navigatable).canNavigate()

    override fun canNavigateToSource(): Boolean = (element.getLangElement() as Navigatable).canNavigateToSource()

    override fun getValue(): Any = element

    fun isPublic(): Boolean = element.isPublic()
}