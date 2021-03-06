package org.lanark.drawing

import org.lanark.geometry.*
import org.lanark.system.*
import org.lwjgl.glfw.*

actual class Canvas(val image: GLFWImage) : Managed {
    override fun release() {
        image.close()
    }

    actual val size: Size
        get() = Size(image.width(), image.height())

    actual var blendMode: BlendMode = BlendMode.None

    actual fun blit(source: Canvas) {

    }

    actual fun blit(source: Canvas, sourceRect: Rect, destination: Point) {}
    actual fun blitScaled(source: Canvas) {}
    actual fun blitScaled(source: Canvas, sourceRect: Rect, destinationRect: Rect) {}
    actual fun fill(color: Color) {}
    actual fun fill(color: Color, rectangle: Rect) {}

    override fun toString() = "Canvas ${image}"
}