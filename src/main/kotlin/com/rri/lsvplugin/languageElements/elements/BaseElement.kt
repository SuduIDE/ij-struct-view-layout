package com.rri.lsvplugin.languageElements.elements

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elementUtils.ElementStructure
import com.rri.lsvplugin.psi.visitors.IElementVisitor

abstract class BaseElement(private val langElement: PsiElement) {
    open lateinit var presentableView: ItemPresentation
    open var elementStructure: ElementStructure = ElementStructure()

    open fun setStructure(value: ElementStructure) {
        elementStructure = value
    }

    open fun addChild(childElement: BaseElement) {
        elementStructure.children.add(childElement)
    }

    open fun getChildren(): List<BaseElement> = elementStructure.children

    open fun isPublic(): Boolean {
        return elementStructure.getModifiers()?.contains("public") ?: false
    }

    abstract fun accept(visitor: IElementVisitor)

    abstract fun createPresentableView()

    open fun getLangElement(): PsiElement {
        return langElement
    }

}