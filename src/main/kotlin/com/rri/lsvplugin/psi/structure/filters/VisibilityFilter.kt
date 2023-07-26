package com.rri.lsvplugin.psi.structure.filters

import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.languageElements.elementUtils.DefaultIconContainer
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewElement
import com.rri.lsvplugin.services.JsonStructureSV

class VisibilityFilter(private val filterName : String, private val filterInfo: JsonStructureSV.VisibilityFilterInfo) : Filter {
    override fun isVisible(treeNode: TreeElement?): Boolean {
        val element = (treeNode as CustomizedStructureViewElement).value
        if (filterInfo.notElementType != null && filterInfo.notElementType.contains(element.elementType))
            return true

        if ( filterInfo.elementType != null && !filterInfo.elementType.contains(element.elementType))
            return true

        if (filterInfo.attributeKey != null) {
            if (element.structure[filterInfo.attributeKey] is List<*>) {
                return (element.structure[filterInfo.attributeKey] as List<*>).contains(filterInfo.attributeValue)
            } else if (element.structure[filterInfo.attributeKey] != null && element.structure[filterInfo.attributeKey] != filterInfo.attributeValue)
                return true
        }

        return false
    }

    override fun getName(): String = filterName

    override fun getPresentation(): ActionPresentation {
        return ActionPresentationData(
            filterName,
            null,
            DefaultIconContainer.getIcon(filterInfo.icon)
        )
    }

    override fun isReverted(): Boolean = true
}