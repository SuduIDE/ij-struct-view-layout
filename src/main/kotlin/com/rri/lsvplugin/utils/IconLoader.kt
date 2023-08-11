package com.rri.lsvplugin.utils

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import java.nio.file.Path
import javax.swing.Icon
import kotlin.reflect.KClass
import kotlin.reflect.full.staticProperties

class IconLoader {
    companion object {
        private val iconMap : Map<String, Icon> = fillIconMap()

        fun getIcon(iconStr: String, project : Project) : Icon? {
            return PlatformIcons.values().firstOrNull { it.name == iconStr }?.let { IconManager.getInstance().getPlatformIcon(it) }
                ?: iconMap[iconStr]
                ?: IconLoader.findIcon(Path.of(project.basePath).resolve(iconStr).toUri().toURL(), true)
                ?: IconLoader.findIcon(Path.of(iconStr).toUri().toURL(), true)
        }


        private fun fillIconMap() : Map<String, Icon> {
            val iconMap = mutableMapOf<String, Icon>()
            findAllIcons(AllIcons::class, iconMap)
            return iconMap
        }


        private fun findAllIcons(currentGroup: KClass<*>, iconMap : MutableMap<String, Icon>) {
            for (icon in currentGroup.staticProperties) {
                iconMap[icon.name] = icon.get() as Icon
            }

            for (childGroup in currentGroup.nestedClasses) {
                findAllIcons(childGroup, iconMap)
            }
        }

    }
}