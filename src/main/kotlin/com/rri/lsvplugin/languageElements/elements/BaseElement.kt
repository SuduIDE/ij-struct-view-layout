package com.rri.lsvplugin.languageElements.elements

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.rd.framework.base.deepClonePolymorphic
import com.rri.lsvplugin.languageElements.elementUtils.ElementDescriptorIconProvider
import com.rri.lsvplugin.utils.JsonStructureSV
import javax.swing.Icon

open class BaseElement(val langElement: PsiElement) : Attributes(){
    data class IconStructure(
        val defaultIcon: JsonStructureSV.IconInfo?,
        val attributeKey: String?,
        val attributeValue: List<String>?,
        val alternativeIcon: JsonStructureSV.IconInfo?
    ) {
        fun getDefaultIcon() = defaultIcon?.loadedIcon
        fun getAlternativeIcon() = alternativeIcon?.loadedIcon
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
                } else if (getAttribute(attr) is List<*>) {
                    val attributeList = getAttribute(attr) as List<*>
                    presentableText.append(addPresentableListText(attributeList))
                } else if (!addPartToPresentableText( presentableText, getAttribute(attr)) && !isAttribute(attr))
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

            presentableText.append(description.toString().replace(Regex("^\"|\"$"), ""))
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

    var displayLevel: Int = 1
    var displayOnlyLevel: Int = 0
    var currentLevel: Int = 0
    var elementType: String? = null
    var baseIcon: IconStructure? = null
    var presentableText = PresentableViewText()
    var children: MutableList<BaseElement> = mutableListOf()
    var parent: BaseElement? = null


    init {
        if (langElement is PsiFile) {
            elementType = "file"
            uniqueAttributes["name"] = langElement.name
            uniqueAttributes["filepath"] = langElement.containingDirectory.virtualFile.path
            presentableText = PresentableViewText(listOf("name", " ", "filepath"), listOf())
        }
    }

    fun getPresentableView(): ItemPresentation {
        return PresentationData(
            presentableText.getText(),
            presentableText.getDescription(),
            getIcon(),
            null
        )
    }

    override fun getDefaultAttributes() : List<KeywordStructure>? {
        val parentDefaultAttributes =  defaultAttributes?.parent?.get(parent!!.elementType) ?: defaultAttributes?.parent?.get("else")
        val childrenDefaultAttributes : List<KeywordStructure>?
        val childrenContainedInDefaultedAttributes = children.filter { defaultAttributes?.children?.containsKey(it.elementType) ?: false}
        if (children.isEmpty())
            childrenDefaultAttributes  = defaultAttributes?.children?.get("leaf")
        else if (childrenContainedInDefaultedAttributes.isEmpty())
            childrenDefaultAttributes = defaultAttributes?.children?.get("else")
        else {
            childrenDefaultAttributes = mutableListOf()
            for (child in childrenContainedInDefaultedAttributes) {
                childrenDefaultAttributes.addAll(defaultAttributes!!.children!!.get(child.elementType)!!)
            }
        }
        return if (parentDefaultAttributes == null)
            childrenDefaultAttributes
        else if (childrenDefaultAttributes == null)
            parentDefaultAttributes
        else
            parentDefaultAttributes.plus(childrenDefaultAttributes)
    }

    fun clone(): BaseElement {
        val cloneElement = BaseElement(langElement.deepClonePolymorphic())
        cloneElement.displayLevel = displayLevel
        cloneElement.elementType = elementType
        cloneElement.baseIcon = baseIcon
        super.clone(cloneElement)
        for (child in children)
            cloneElement.children.add(child.clone())
        return cloneElement
    }

    fun isFull(): Boolean {
        for (attr in uniqueAttributes.values)
            if (attr == null)
                return false

        for (attr in exclusiveAttributes.values)
            if (attr != null)
                return false

        return true
    }

    private fun getIcon(): Icon? = ElementDescriptorIconProvider.getIcon(this)


    override fun equals(other: Any?): Boolean {
        if (other !is BaseElement)
            return false
        return elementType.equals(other.elementType) && other.children == children && super.equals(other)
    }

    override fun toString(): String = presentableText.getText()


    fun clear() {
        parent = null
        children.forEach { it.clear() }
        children.clear()
    }


}