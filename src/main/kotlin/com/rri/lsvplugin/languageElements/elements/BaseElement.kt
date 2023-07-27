package com.rri.lsvplugin.languageElements.elements

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.jetbrains.rd.framework.base.deepClonePolymorphic
import com.rri.lsvplugin.languageElements.elementUtils.ElementDescriptorIconProvider
import javax.swing.Icon

open class BaseElement(val langElement: PsiElement) {

    data class ElementStructure(
        val setAttributes: MutableMap<String, MutableList<*>?> = mutableMapOf(),
        val uniqueAttributes: MutableMap<String, Any?> = mutableMapOf()
    )

    data class ElementPresentableText(
        var presentableTextList: List<String> = listOf(),
        var presentableDescriptionList: List<String> = listOf()
    )

    var displayLevel: Int = 0
    open var elementType: String? = null
    var structure = ElementStructure()
    open var baseIcon: String? = "default"
    var presentableText = ElementPresentableText()
    open var children: MutableList<BaseElement> = mutableListOf()

    fun getPresentableView(): ItemPresentation {
        if (elementType == "file")
            return PresentationData("", null, null, null)
        return PresentationData(
            getText(),
            getDescription(),
            getIcon(),
            null
        )
    }
    fun getUniqueAttributes() : MutableMap<String, Any?> = structure.uniqueAttributes
    fun getSetAttributes() : MutableMap<String, MutableList<*>?> = structure.setAttributes

    fun clone(): BaseElement {
        val cloneElement = BaseElement(langElement.deepClonePolymorphic())
        cloneElement.displayLevel = displayLevel
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

    private fun getPresentableText(fpresentableElementList: (BaseElement) -> List<String>): String {
        val presentableText = StringBuilder()
        for (attr in fpresentableElementList(this)) {
            if (getUniqueAttributes()[attr] is List<*>) {
                for (elAttr in getUniqueAttributes()[attr] as List<*>) {
                    addPartToPresentableText(presentableText, elAttr)
                }
            } else if (getSetAttributes().containsKey(attr)) {
                if (getSetAttributes()[attr] != null) {
                    for (elAttr in getSetAttributes()[attr] as List<*>) {
                        addPartToPresentableText(presentableText, elAttr)
                    }
                }
            } else if (!addPartToPresentableText(
                    presentableText,
                    getUniqueAttributes()[attr]
                ) && !getUniqueAttributes().containsKey(attr)
            )
                presentableText.append(attr)

        }

        return presentableText.toString()
    }

    private fun getText() : String {
        return getPresentableText { obj: BaseElement -> obj.presentableText.presentableTextList }
    }

    private fun getDescription() : String {
        return getPresentableText { obj: BaseElement -> obj.presentableText.presentableDescriptionList }
    }

    private fun getIcon(): Icon? {
        return ElementDescriptorIconProvider.getIcon(this)
    }

    fun isFull(): Boolean {
        for (attr in structure.uniqueAttributes.values)
            if (attr == null)
                return false

        return true
    }

    private fun addPartToPresentableText(
        presentableText: StringBuilder,
        description: Any?
    ): Boolean {
        if (description == null)
            return false

        if (description is BaseElement) {
            presentableText.append(description.getText())
        } else presentableText.append(description)

        return true
    }

}