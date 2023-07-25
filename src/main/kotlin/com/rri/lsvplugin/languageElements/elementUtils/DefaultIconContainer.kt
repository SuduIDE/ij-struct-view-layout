package com.rri.lsvplugin.languageElements.elementUtils

import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import javax.swing.Icon

class DefaultIconContainer {
    companion object {
        private val iconManager = IconManager.getInstance()
        fun getIcon(iconName: String?): Icon? {
            return when (iconName) {
                "interface" -> iconManager.getPlatformIcon(PlatformIcons.Interface)
                "class" -> iconManager.getPlatformIcon(PlatformIcons.Class)
                "abstractClass" -> iconManager.getPlatformIcon(PlatformIcons.AbstractClass)
                "aClass" -> iconManager.getPlatformIcon(PlatformIcons.AnonymousClass)
                "method" -> iconManager.getPlatformIcon(PlatformIcons.Method)
                "abstractMethod" -> iconManager.getPlatformIcon(PlatformIcons.AbstractMethod)
                "field" -> iconManager.getPlatformIcon(PlatformIcons.Field)
                "property" -> iconManager.getPlatformIcon(PlatformIcons.Property)
                "lambda" -> iconManager.getPlatformIcon(PlatformIcons.Lambda)
                "private" -> iconManager.getPlatformIcon(PlatformIcons.Private)
                "public" -> iconManager.getPlatformIcon(PlatformIcons.Public)
                "protected" -> iconManager.getPlatformIcon(PlatformIcons.Protected)
                "local" -> iconManager.getPlatformIcon(PlatformIcons.Local)
                "tag" -> iconManager.getPlatformIcon(PlatformIcons.Tag)
                "final" -> iconManager.getPlatformIcon(PlatformIcons.FinalMark)
                "static" -> iconManager.getPlatformIcon(PlatformIcons.StaticMark)
                else -> null
            }
        }
    }
}