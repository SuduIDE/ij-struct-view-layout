package com.rri.lsvplugin.psi.structure.filters

import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.rri.lsvplugin.languageElements.elements.BaseElement
import com.rri.lsvplugin.psi.structure.CustomizedStructureViewElement


object PublicElementsFilter : Filter {
    override fun isVisible(treeNode: TreeElement): Boolean {
        return (treeNode as? CustomizedStructureViewElement)?.value?.isPublic() ?: true
    }

    override fun getPresentation(): ActionPresentation {
        return ActionPresentationData(
            "Non-public",
            null,
            IconManager.getInstance().getPlatformIcon(PlatformIcons.Private)
        )
    }

    override fun getName() = ID

    override fun isReverted() = true

    const val ID = "LSV_SHOW_NON_PUBLIC"
}