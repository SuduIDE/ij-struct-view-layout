package com.rri.lsvplugin.services

import com.intellij.openapi.components.Service
import java.io.File

@Service(Service.Level.PROJECT)
class JsonSvContainerImpl : JsonSvContainer {
    private val jsonSV = JsonInfo()
    override fun setJsonSV(mapSV: MapTypeSV?) {
        if (mapSV != null)
            jsonSV.setMapSV(mapSV)
    }

    override fun reset() = jsonSV.reset()

    override fun getJsonSV(): String = jsonSV.getJsonSV()
    override fun getMapSV(): MapTypeSV = jsonSV.getMapSV()

    override fun getFilename(): File = jsonSV.filenameJsonSV
}