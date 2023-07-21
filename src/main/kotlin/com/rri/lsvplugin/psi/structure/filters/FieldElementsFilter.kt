package com.rri.lsvplugin.psi.structure.filters

import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewElement

object FieldElementsFilter : Filter {
    override fun isVisible(treeNode: TreeElement): Boolean {
        return true
    }

    override fun getPresentation(): ActionPresentation {
        return ActionPresentationData(
            "FIELD",
            null,
            IconManager.getInstance().getPlatformIcon(PlatformIcons.Field)
        )
    }

    override fun getName() = ID

    override fun isReverted() = true

    const val ID = "LSV_SHOW_FIELD"
}