package com.rri.lsvplugin.listeners

import com.intellij.ide.ActivityTracker
import com.intellij.ide.SaveAndSyncHandler
import com.intellij.ide.impl.StructureViewWrapperImpl
import com.intellij.ide.structureView.StructureViewFactoryEx
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.rri.lsvplugin.services.JsonSvContainerServiceImpl


class ListenerJsonSV(private val project: Project) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        val containerSV = project.service<JsonSvContainerServiceImpl>()
        for (e in events) {
            if (e.file?.name == containerSV.getFilename().toString()) {
                containerSV.loadCurrentVersion()
                val wrapper = StructureViewFactoryEx.getInstanceEx(project).structureViewWrapper as StructureViewWrapperImpl
                wrapper.rebuildNow("update structure view")
            }
        }
        super.after(events)
    }

}