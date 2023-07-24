package com.rri.lsvplugin.languageElements.elements

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.openhtmltopdf.css.parser.property.BorderPropertyBuilders.BorderLeft
import javax.swing.Icon

open class BaseElement(val langElement: PsiElement){
    open var typeElement : String? = null
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
            if (structure[attr] is BaseElement) {
                presentableText.append((structure[attr] as BaseElement).getPresentableText())
            } else if (structure.containsKey(attr))
                presentableText.append(structure[attr])
            else
                presentableText.append(attr)
        }

        return presentableText.toString()
    }

    fun getIcon() : Icon {
        val iconManager = IconManager.getInstance()
        return iconManager.getPlatformIcon(PlatformIcons.Class)
    }

    fun isFull() : Boolean {
        for (attr in structure.values)
            if (attr == null)
                return false
        return true
    }
    
}