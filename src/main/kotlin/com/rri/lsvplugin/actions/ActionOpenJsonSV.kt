package com.rri.lsvplugin.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.vfs.findFile
import com.rri.lsvplugin.services.JsonSvContainerImpl
import java.io.File

class ActionOpenJsonSV : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val containerSV = e.project?.service<JsonSvContainerImpl>()
        val fullPathToJsonSV =
            containerSV?.let { e.project?.basePath?.let { it1 -> File(it1).resolve(it.getFilename()) } }
        val virtualJsonFile = e.getData(CommonDataKeys.PSI_FILE)?.virtualFile?.findFile(fullPathToJsonSV.toString())
        virtualJsonFile?.let { e.project?.let { it1 -> OpenFileDescriptor(it1, it).navigate(true) } }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}