package com.rri.lsvplugin.languageElements.elementUtils

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.ui.Queryable
import javax.swing.Icon

class PresentableView(private var presentableText: String?, private var icon: Icon?): ItemPresentation {

    override fun getPresentableText(): String? = presentableText

    override fun getIcon(unused: Boolean): Icon? = icon

}