package com.rri.lsvplugin.psi.structure.filters

import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.rri.lsvplugin.languageElements.elements.Attributes
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewElement
import com.rri.lsvplugin.utils.JsonStructureSV


class SortingFilter(private val filterName : String,
                    private val filterInfo: JsonStructureSV.SortingFilterInfo,
                    private val iconInfo: JsonStructureSV.IconInfo?) : Sorter {
    override fun getPresentation(): ActionPresentation {
        return ActionPresentationData(
            filterName,
            null,
            iconInfo?.loadedIcon
        )
    }

    override fun getComparator() = Comparator<Any> { a1, a2 -> a1.accessLevel() - a2.accessLevel() }

    private fun Any.accessLevel() : Int {
        val element = (this as? CustomizedStructureViewElement)?.value ?: return filterInfo.defaultValue ?: return 0
        val sortByList = filterInfo.sortBy
        for (attribute in element.uniqueAttributes.values) {

            if (attribute is List<*>) {
                val sortableKeyword =
                    attribute.firstOrNull { attr -> sortByList.any { sortableAttr -> sortableAttr == attr.toString() && attr is Attributes.KeywordStructure && attr.sortValue != null } }
                if (sortableKeyword != null)
                    return (sortableKeyword as Attributes.KeywordStructure).sortValue!!
            } else if (attribute is Attributes.KeywordStructure && attribute.sortValue != null  && sortByList.contains(attribute.toString())) {
                    return attribute.sortValue
            }
        }
        if (element.getDefaultAttributes() != null) {
            for (attribute in element.getDefaultAttributes()!!) {
                if (attribute.sortValue != null && sortByList.contains(attribute.toString()))
                    return attribute.sortValue
            }
        }

        return filterInfo.defaultValue ?: return 0
    }

    override fun isVisible(): Boolean = true
    override fun getName(): String = filterName
}