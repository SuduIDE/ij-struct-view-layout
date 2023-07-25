package com.rri.lsvplugin.languageElements.factory.filterCreator
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.psi.PsiElement
import com.rri.lsvplugin.psi.JsonContainerUtil
import net.minidev.json.JSONUtil

interface IFilterCreator {
    fun createFilter(filterName : String, filterMap: Map<String, Any>) : Filter
}