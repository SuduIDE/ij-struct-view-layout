package com.rri.lsvplugin.psi.structure.filters

import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.rri.lsvplugin.languageElements.elements.Attributes
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewElement
import com.rri.lsvplugin.utils.ErrorNotification
import com.rri.lsvplugin.utils.JsonStructureSV


// Global class for all types a visibility filters
class VisibilityFilter(
    private val filterName: String,
    private val filterInfo: JsonStructureSV.VisibilityFilterInfo,
    private val iconInfo: JsonStructureSV.IconInfo?
) : Filter {
    override fun isVisible(treeNode: TreeElement?): Boolean {
        val element = (treeNode as CustomizedStructureViewElement).value
        //exclude elements for which the filter does not work
        if (filterInfo.notElementType != null && filterInfo.notElementType.contains(element.elementType))
            return true
        //exclude elements for which the filter does not work
        if (filterInfo.elementType != null && !filterInfo.elementType.contains(element.elementType))
            return true

        if (filterInfo.attributeKey != null) {
            if (
                (filterInfo.attributeValue != null && excludeAttributes(element))
                ||
                (filterInfo.notAttributeValue != null && includeAttributes(element))
            ) {
                return true
            }

            if (filterInfo.notAttributeValue == null && filterInfo.attributeValue == null)
                return  !element.uniqueAttributes.containsKey(filterInfo.attributeKey)
                        &&
                        !element.setAttributes.containsKey(filterInfo.attributeKey)
        }

        return false
    }

    override fun getName(): String = filterName

    override fun getPresentation(): ActionPresentation {
        return ActionPresentationData(
            filterName,
            null,
           iconInfo?.loadedIcon
        )
    }

    override fun isReverted(): Boolean = true

    private fun includeAttributes(element: BaseElement): Boolean {
        return if (element.uniqueAttributes[filterInfo.attributeKey] == null)
            false
        else if (includeUniqueAttribute(element, filterInfo.notAttributeValue!!))
            true
        else if (element.setAttributes[filterInfo.attributeKey] == null) {
            if (element.getDefaultAttributes() == null)
                return false
            includeDefaultAttribute(element, filterInfo.notAttributeValue)
        }
        else
            includeSetAttribute(element, filterInfo.notAttributeValue)
    }

    private fun excludeAttributes(element: BaseElement): Boolean {
        return if (element.uniqueAttributes[filterInfo.attributeKey] == null)
            false
        else if (includeUniqueAttribute(element, filterInfo.attributeValue!!))
            false
        else if (element.setAttributes[filterInfo.attributeKey] == null) {
            if (element.getDefaultAttributes() == null)
                return true

            !includeDefaultAttribute(element, filterInfo.attributeValue)
        }
        else
            !includeSetAttribute(element, filterInfo.attributeValue)
    }

    private fun includeUniqueAttribute(element: BaseElement, attributeValue: List<String>): Boolean {
        try {
            return if (element.uniqueAttributes[filterInfo.attributeKey] is List<*>)
                (element.uniqueAttributes[filterInfo.attributeKey] as List<*>).any {
                    findAttribute(it, attributeValue)
                }
            else
                findAttribute(element.uniqueAttributes[filterInfo.attributeKey], attributeValue)
        } catch (error: Exception) {
            ErrorNotification.notifyError(element.langElement.project, "Regular expression is incorrectly specified, regexp search disabled ")
            return true
        }
    }

    private fun findAttribute(attribute: Any?, attributeValue: List<String>) : Boolean {
        if (attribute !is Attributes.PropertyStructure)
            return  attributeValue.contains(attribute.toString())

        return attributeValue.firstOrNull {
            Regex(it, RegexOption.IGNORE_CASE).find(attribute.toString()) != null
        } != null
    }

    private fun includeSetAttribute(element: BaseElement, attributeValue: List<String>): Boolean {
        return element.setAttributes[filterInfo.attributeKey]!!.any {
           attributeValue.contains(it.toString())
        }
    }

    private fun includeDefaultAttribute(element: BaseElement, attributeValue: List<String>) : Boolean {
        return element.getDefaultAttributes()!!.any {
            attributeValue.contains(it.toString())
        }
    }

}