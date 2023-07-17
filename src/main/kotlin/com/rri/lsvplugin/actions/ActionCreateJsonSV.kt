package com.rri.lsvplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.rri.lsvplugin.services.JsonSvContainerImpl
import java.io.File

class ActionCreateJsonSV : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val containerSV = e.project?.service<JsonSvContainerImpl>()
        val fullPathToJsonSV = containerSV?.let { File(e.project?.basePath).resolve(it.getFilename()) }
        fullPathToJsonSV?.createNewFile()
        fullPathToJsonSV?.writeText(containerSV.getJsonSV())
    }
}