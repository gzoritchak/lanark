package org.lanark.application

import org.lanark.diagnostics.*
import org.lanark.drawing.*
import org.lanark.geometry.*
import org.lanark.io.*
import org.lanark.media.*

expect class Engine(configure: EngineConfiguration.() -> Unit) {
    fun quit()
    
    val logger: Logger
    
    fun sleep(millis: UInt)
    fun setScreenSaver(enabled: Boolean)
    
    var activeCursor: Cursor?

    fun createCursor(canvas: Canvas, hotX: Int, hotY: Int): Cursor
    fun createCursor(systemCursor: SystemCursor): Cursor

    fun createCanvas(size: Size, bitsPerPixel: Int): Canvas 
    fun loadCanvas(path: String, fileSystem: FileSystem): Canvas

    fun loadMusic(path: String, fileSystem: FileSystem): Music
    fun loadSound(path: String, fileSystem: FileSystem): Sound
    fun loadVideo(path: String, fileSystem: FileSystem): Video
    
    fun createFrame(title: String,
        width: Int,
        height: Int,
        x: Int = Frame.UndefinedPosition,
        y: Int = Frame.UndefinedPosition,
        windowFlags: UInt = Frame.CreateShown
    ): Frame
}
