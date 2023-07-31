package com.rri.lsvplugin.services

import java.io.File
import com.rri.lsvplugin.utils.MapTypeSV

interface JsonSvContainerService {

    fun setJsonSV(mapSV: MapTypeSV?)
    fun reset()
    fun getJsonSV(): String

    fun getMapSV(): MapTypeSV?

    fun getFilename(): File
}