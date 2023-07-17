package com.rri.lsvplugin.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.rri.lsvplugin.services.JsonSvContainerImpl
import com.rri.lsvplugin.services.MapTypeSV
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File


class ListenerJsonSV(private val project: Project) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        val containerSV = project.service<JsonSvContainerImpl>()
        val fullPathToJsonSV = project.basePath?.let { File(it).resolve(containerSV.getFilename()) }
        for (e in events) {
            if (e.file?.name == containerSV.getFilename().toString()) {
                val updatedJsonSV = fullPathToJsonSV?.readText()?.let { Json.decodeFromString<MapTypeSV>(it) }
                containerSV.setJsonSV(updatedJsonSV)
            }
        }
        super.after(events)
    }

}