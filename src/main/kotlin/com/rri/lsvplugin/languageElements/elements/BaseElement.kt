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
    open var baseIcon : String? = "default"
    open var children : MutableList<BaseElement> = mutableListOf()

    fun getPresentableView() : ItemPresentation {
        return PresentationData(getPresentableText(), null, getIcon(),  null)
    }

    private fun getPresentableText() : String {
        val presentableText = StringBuilder()
        for (attr in presentableTextList) {
            if (structure[attr] is List<*>) {
                for (elAttr in structure[attr] as List<*>) {
                    addDescriptionToPresentableText(presentableText, elAttr)
                }
            } else if (!addDescriptionToPresentableText(presentableText, structure[attr]) && !structure.containsKey(attr))
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

    private fun addDescriptionToPresentableText(presentableText: StringBuilder, description : Any?) : Boolean {
        if (description == null)
            return false

        if (description is BaseElement) {
            presentableText.append(description.getPresentableText())
        } else presentableText.append(description)

        return true
    }
    
}