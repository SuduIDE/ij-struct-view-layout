package com.rri.lsvplugin.languageElements.elements

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.jetbrains.rd.framework.base.deepClonePolymorphic
import com.rri.lsvplugin.languageElements.elementUtils.ElementDescriptorIconProvider
import com.rri.lsvplugin.utils.JsonStructureSV
import javax.swing.Icon

open class BaseElement(val langElement: PsiElement) {

    data class ElementStructure(
        val setAttributes: MutableMap<String, MutableList<*>?> = mutableMapOf(),
        val uniqueAttributes: MutableMap<String, Any?> = mutableMapOf()
    )

    data class IconStructure(
        val defaultIcon: JsonStructureSV.IconInfo?,
        val attributeKey: String?,
        val attributeValue: List<String>?,
        val alternativeIcon: JsonStructureSV.IconInfo?
    )

    data class KeywordStructure(
        val id : String,
        val sortValue: Int?,
        val icon: JsonStructureSV.IconInfo?
    ) {
        override fun toString(): String = id
    }

    inner class PresentableViewText(
        private var presentableTextList: List<String> = listOf(),
        private var presentableDescriptionList: List<String> = listOf()
    ) {

        fun getText(): String {
            return getPresentableText { obj: BaseElement -> obj.presentableText.presentableTextList }

        }

        fun getDescription(): String {
            return getPresentableText { obj: BaseElement -> obj.presentableText.presentableDescriptionList }

        }

        private fun getPresentableText(fpresentableElementList: (BaseElement) -> List<String>): String {
            val presentableText = StringBuilder()
            for (attr in fpresentableElementList(this@BaseElement)) {
                if (attr == "\$i") {
                    var thisPos = 0
                    for (parentChild in this@BaseElement.parent?.children!!) {
                        if (this@BaseElement === parentChild)
                            break
                        else if (this@BaseElement.elementType == parentChild.elementType)
                            thisPos++
                    }
                    presentableText.append(thisPos.toString())
                } else if (getUniqueAttributes()[attr] is List<*>) {
                    val uniqueAttributeList = getUniqueAttributes()[attr] as List<*>
                    presentableText.append(addPresentableListText(uniqueAttributeList))
                } else if (getSetAttributes().containsKey(attr)) {
                    if (getSetAttributes()[attr] != null) {
                        val setAttributeList = getSetAttributes()[attr] as List<*>
                        presentableText.append(addPresentableListText(setAttributeList))
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

        private fun addPartToPresentableText(
            presentableText: StringBuilder,
            description: Any?
        ): Boolean {
            if (description == null)
                return false

            presentableText.append(description.toString())
            return true
        }

        private fun addPresentableListText(attributeList: List<*>): String {
            val presentableListText = StringBuilder()
            for (elAttr in attributeList) {
                presentableListText.append(' ')
                addPartToPresentableText(presentableListText, elAttr)
            }
            if (presentableListText.isNotEmpty()) {
                presentableListText.deleteCharAt(0)
            }

            return presentableListText.toString()
        }
    }

    var displayLevel: Int = 0
    var elementType: String? = null
    var structure = ElementStructure()
    var baseIcon: IconStructure? = null
    var presentableText = PresentableViewText()
    var children: MutableList<BaseElement> = mutableListOf()
    var parent: BaseElement? = null

    fun getPresentableView(): ItemPresentation {
        if (elementType == "file")
            return PresentationData("", null, null, null)
        return PresentationData(
            presentableText.getText(),
            presentableText.getDescription(),
            getIcon(),
            null
        )
    }

    fun getUniqueAttributes(): MutableMap<String, Any?> = structure.uniqueAttributes
    fun getSetAttributes(): MutableMap<String, MutableList<*>?> = structure.setAttributes

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

    fun isFull(): Boolean {
        for (attr in structure.uniqueAttributes.values)
            if (attr == null)
                return false

        return true
    }

    private fun getIcon(): Icon? = ElementDescriptorIconProvider.getIcon(this)


    override fun equals(other: Any?): Boolean {
        if (other !is BaseElement)
            return false
        return elementType.equals(other.elementType) && other.structure == structure && other.children == children
    }

    override fun toString(): String = presentableText.getText()


    fun clear() {
        parent = null
        children.forEach { it.clear() }
        children.clear()
    }


}