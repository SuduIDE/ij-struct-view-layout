package com.rri.lsvplugin.services

import com.rri.lsvplugin.utils.MapTypeSV
import java.io.File

interface JsonSvContainerService {

    fun setJsonSV(languagesSV: MapTypeSV?)
    fun reset()
    fun getJsonSV(): String

    fun getMapSV(): MapTypeSV?

    fun getFilename(): File
}