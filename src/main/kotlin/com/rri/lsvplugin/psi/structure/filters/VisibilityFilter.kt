package com.rri.lsvplugin.psi.structure.filters

import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewElement
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
                return !element.getUniqueAttributes()
                    .containsKey(filterInfo.attributeKey) && !element.getSetAttributes()
                    .containsKey(filterInfo.attributeKey)
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
        return if (element.getUniqueAttributes()[filterInfo.attributeKey] == null)
            false
        else if (includeUniqueAttribute(element, filterInfo.notAttributeValue!!))
            true
        else if (element.getSetAttributes()[filterInfo.attributeKey] == null) {
            if (element.getDefaultAttributes() == null)
                return false
            includeDefaultAttribute(element, filterInfo.notAttributeValue)
        }
        else
            includeSetAttribute(element, filterInfo.notAttributeValue)
    }

    private fun excludeAttributes(element: BaseElement): Boolean {
        return if (element.getUniqueAttributes()[filterInfo.attributeKey] == null)
            false
        else if (includeUniqueAttribute(element, filterInfo.attributeValue!!))
            false
        else if (element.getSetAttributes()[filterInfo.attributeKey] == null) {
            if (element.getDefaultAttributes() == null)
                return true

            !includeDefaultAttribute(element, filterInfo.attributeValue)
        }
        else
            !includeSetAttribute(element, filterInfo.attributeValue)
    }

    private fun includeUniqueAttribute(element: BaseElement, attributeValue: List<String>): Boolean {
        return if (element.getUniqueAttributes()[filterInfo.attributeKey] is List<*>)
            (element.getUniqueAttributes()[filterInfo.attributeKey] as List<*>).any {
                attributeValue.contains(it.toString())
            }
        else
            attributeValue.contains(element.getUniqueAttributes()[filterInfo.attributeKey].toString())
    }


    private fun includeSetAttribute(element: BaseElement, attributeValue: List<String>): Boolean {
        return element.getSetAttributes()[filterInfo.attributeKey]!!.any {
           attributeValue.contains(it.toString())
        }
    }

    private fun includeDefaultAttribute(element: BaseElement, attributeValue: List<String>) : Boolean {
        return element.getDefaultAttributes()!!.any {
            attributeValue.contains(it.toString())
        }
    }

}