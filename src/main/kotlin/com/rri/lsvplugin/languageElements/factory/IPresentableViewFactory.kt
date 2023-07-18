package com.rri.lsvplugin.languageElements.factory

import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.languageElements.elements.*

interface IPresentableViewFactory {

    fun createPresentableView(baseElement: BaseElement) : PresentableView

}