package com.intership.lsvplugin

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class CustomStructureViewModel(psiFile : PsiFile, editor : Editor?) :  StructureViewModelBase(psiFile, editor, CustomStructureViewElement(psiFile)), StructureViewModel.ElementInfoProvider{
    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean {
        return true
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        return element.value is PsiElement
    }

    override fun getSuitableClasses(): Array<Class<Any>> {
        return super.getSuitableClasses()
    }

}