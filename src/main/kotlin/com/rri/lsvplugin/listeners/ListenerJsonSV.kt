package com.rri.lsvplugin.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl


class ListenerJsonSV(private val project: Project) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        val containerSV = project.service<JsonSvContainerServiceImpl>()
        for (e in events) {
            if (e.file?.name == containerSV.getFilename().toString()) {
              containerSV.loadCurrentVersion()
            }

        }
        super.after(events)
    }

}