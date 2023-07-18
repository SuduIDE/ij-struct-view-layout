package com.rri.lsvplugin.languageElements.factory

import com.intellij.psi.PsiElement
import com.rri.lsvplugin.languageElements.elements.*

interface IElementFactory {

    fun createClass(langElement: PsiElement): ClassBaseElement

    fun createInterface(langElement: PsiElement) : InterfaceElement

    fun createAClass(langElement: PsiElement): ClassBaseElement

    fun createFunction(langElement: PsiElement): FunctionBaseElement

    fun createMethod(langElement: PsiElement): FunctionBaseElement

    fun createLambda(langElement: PsiElement): LambdaElement

    fun createField(langElement: PsiElement): FieldElement


}