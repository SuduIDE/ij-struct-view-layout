package com.rri.lsvplugin.languageElements.factory

import com.intellij.navigation.ItemPresentation
import com.rri.lsvplugin.languageElements.elementUtils.ElementDescriptorIconProvider
import com.rri.lsvplugin.languageElements.elementUtils.ElementDescriptorTextProvider
import com.rri.lsvplugin.languageElements.elementUtils.PresentableView
import com.rri.lsvplugin.languageElements.elements.*
import javax.swing.Icon

class PresentableViewFactoryDefaultImpl : IPresentableViewFactory {

    override fun createPresentableView(baseElement: BaseElement) : PresentableView {
        return PresentableView(ElementDescriptorTextProvider.getText(baseElement), ElementDescriptorIconProvider.getIcon(baseElement))
    }
}