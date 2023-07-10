package com.rri.lsvplugin.languageElements.factory

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.ClassBaseElement
import com.rri.lsvplugin.languageElements.elements.FieldElement
import com.rri.lsvplugin.languageElements.elements.FunctionBaseElement
import com.rri.lsvplugin.languageElements.elements.LambdaElement

interface IElementFactory {

    fun createClass(langElement: PsiElement) : ClassBaseElement

    fun createAClass(langElement: PsiElement) : ClassBaseElement

    fun createFunction(langElement: PsiElement) : FunctionBaseElement

    fun createMethod(langElement: PsiElement) : FunctionBaseElement

    fun createLambda(langElement: PsiElement) : LambdaElement

    fun createField(langElement: PsiElement) : FieldElement


}