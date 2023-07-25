package com.rri.lsvplugin.languageElements.factory.filterCreator

import com.intellij.ide.util.treeView.smartTree.Filter
import com.rri.lsvplugin.psi.structure.filters.VisibilityFilter
import com.rri.lsvplugin.services.JsonStructureSV

class VisibilityFilterCreator : IFilterCreator {
    override fun createFilter(filterName: String, filterMap: Map<String, Any>): Filter {
        val filterInfo = JsonStructureSV.VisibilityFilterInfo(
            filterMap["elementType"] as? List<String>,
            filterMap["notElementType"] as? List<String>,
            filterMap["attributeKey"] as? String,
            filterMap["attributeValue"] as? String,
            filterMap["icon"] as? String
        )

        return VisibilityFilter(filterName, filterInfo)
    }

}