package com.rri.lsvplugin.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl
import java.io.File


class ActionCreateJsonSV : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val containerSV = e.project?.service<JsonSvContainerServiceImpl>()
        val fullPathToJsonSV = containerSV?.let { File(e.project?.basePath).resolve(it.getFilename()) }

        containerSV?.reset()
        fullPathToJsonSV?.createNewFile()
        fullPathToJsonSV?.writeText(containerSV.getJsonSV())
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}