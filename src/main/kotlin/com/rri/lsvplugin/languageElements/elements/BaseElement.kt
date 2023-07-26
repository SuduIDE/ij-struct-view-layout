package com.rri.lsvplugin.languageElements.elements

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elementUtils.ElementDescriptorIconProvider
import javax.swing.Icon

open class BaseElement(val langElement: PsiElement){
    open var elementType : String? = null
    open var structure : MutableMap<String, Any?> = mutableMapOf()
    open var presentableTextList: List<String> = listOf()
    open var presentableDescriptionList: List<String> = listOf()
    open var baseIcon : String? = "default"
    open var children : MutableList<BaseElement> = mutableListOf()

    fun getPresentableView() : ItemPresentation {
        return PresentationData(getPresentableText { obj: BaseElement -> obj.presentableTextList }, getPresentableText {obj : BaseElement -> obj.presentableDescriptionList}, getIcon(),  null)
    }

    fun clone() : BaseElement {
        val cloneElement = BaseElement(langElement)
        cloneElement.elementType = elementType
        cloneElement.structure = structure
        cloneElement.baseIcon = baseIcon
        for (child in children)
            cloneElement.children.add(child.clone())
        return cloneElement
    }

    override fun equals(other: Any?): Boolean {
        if (other !is BaseElement)
            return false
        return elementType.equals(other.elementType) && other.structure == structure && other.children == children
    }
    private fun getPresentableText(fpresentableElementList: (BaseElement) -> List<String>) : String {
        val presentableText = StringBuilder()
        for (attr in fpresentableElementList(this)) {
            if (structure[attr] is List<*>) {
                for (elAttr in structure[attr] as List<*>) {
                    addDescriptionToPresentableText(fpresentableElementList, presentableText, elAttr)
                }
            } else if (!addDescriptionToPresentableText(fpresentableElementList, presentableText, structure[attr]) && !structure.containsKey(attr))
                    presentableText.append(attr)

        }

        return presentableText.toString()
    }

    private fun getIcon() : Icon? {
        return ElementDescriptorIconProvider.getIcon(this)
    }

    fun isFull() : Boolean {
        for (attr in structure.values)
            if (attr == null)
                return false
        return true
    }

    private fun addDescriptionToPresentableText(fpresentableElementList: (BaseElement) -> List<String>, presentableText: StringBuilder, description : Any?) : Boolean {
        if (description == null)
            return false

        if (description is BaseElement) {
            presentableText.append(description.getPresentableText(fpresentableElementList))
        } else presentableText.append(description)

        return true
    }

}