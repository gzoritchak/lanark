package ksdl.system

import kotlinx.cinterop.*
import ksdl.diagnostics.*
import ksdl.geometry.*
import ksdl.rendering.*
import sdl2.*

class KWindow(private val windowPtr: CPointer<SDL_Window>) : KManaged {
    val id: Int get() = SDL_GetWindowID(windowPtr)

    override fun release() {
        KPlatform.unregisterWindow(id, this)
        val captureId = id
        SDL_DestroyWindow(windowPtr)
        logger.system("Released window #$captureId ${windowPtr.rawValue}")
    }

    fun setBordered(enable: Boolean) {
        SDL_SetWindowBordered(windowPtr, enable.toSDLBoolean())
    }

    fun setResizable(enable: Boolean) {
        SDL_SetWindowResizable(windowPtr, enable.toSDLBoolean())
    }

    fun setWindowMode(mode: Mode) {
        SDL_SetWindowFullscreen(windowPtr, when (mode) {
            Mode.Windowed -> 0
            Mode.FullScreen -> SDL_WINDOW_FULLSCREEN
            Mode.FullScreenDesktop -> SDL_WINDOW_FULLSCREEN_DESKTOP
        })
    }

    fun setIcon(icon: KSurface) {
        SDL_SetWindowIcon(windowPtr, icon.surfacePtr)
    }

    val size: KSize
        get() = memScoped {
            val w = alloc<IntVar>()
            val h = alloc<IntVar>()
            SDL_GetWindowSize(windowPtr, w.ptr, h.ptr)
            KSize(w.value, h.value)
        }

    var minimumSize: KSize
        get() = memScoped {
            val w = alloc<IntVar>()
            val h = alloc<IntVar>()
            SDL_GetWindowMinimumSize(windowPtr, w.ptr, h.ptr)
            KSize(w.value, h.value)
        }
        set(value) {
            SDL_SetWindowMinimumSize(windowPtr, value.width, value.height)
        }

    var maximumSize: KSize
        get() = memScoped {
            val w = alloc<IntVar>()
            val h = alloc<IntVar>()
            SDL_GetWindowMaximumSize(windowPtr, w.ptr, h.ptr)
            KSize(w.value, h.value)
        }
        set(value) {
            SDL_SetWindowMaximumSize(windowPtr, value.width, value.height)
        }

    var brightness: Float
        get() = SDL_GetWindowBrightness(windowPtr)
        set(value) = SDL_SetWindowBrightness(windowPtr, value).checkSDLError("SDL_SetWindowBrightness")

    var title: String
        get() = SDL_GetWindowTitle(windowPtr).checkSDLError("SDL_GetWindowTitle").toKString()
        set(value) = SDL_SetWindowTitle(windowPtr, value)


    val borders: KMargins
        get() = memScoped {
            val top = alloc<IntVar>()
            val left = alloc<IntVar>()
            val bottom = alloc<IntVar>()
            val right = alloc<IntVar>()
            SDL_GetWindowBordersSize(windowPtr, top.ptr, left.ptr, bottom.ptr, right.ptr).checkSDLError("SDL_GetWindowBordersSize")
            KMargins(top.value, left.value, bottom.value, right.value)
        }


    fun createRenderer(rendererFlags: Int = SDL_RENDERER_ACCELERATED or SDL_RENDERER_PRESENTVSYNC): KRenderer {
        val renderer = SDL_CreateRenderer(windowPtr, -1, rendererFlags).checkSDLError("SDL_CreateRenderer")
        return KRenderer(this, renderer)
    }

    fun messageBox(title: String, message: String, icon: KPlatform.MessageBoxIcon) {
        KPlatform.messageBox(title, message, icon, windowPtr)
    }


    enum class Mode {
        Windowed,
        FullScreen,
        FullScreenDesktop,
    }

    override fun toString() = "Window #$id ${windowPtr.rawValue}"

    companion object {
        fun create(title: String, width: Int, height: Int, x: Int = SDL_WINDOWPOS_UNDEFINED, y: Int = SDL_WINDOWPOS_UNDEFINED, windowFlags: SDL_WindowFlags = SDL_WINDOW_SHOWN): KWindow {
            val window = SDL_CreateWindow(title, x, y, width, height, windowFlags).checkSDLError("SDL_CreateWindow")
            return KWindow(window).also {
                KPlatform.registerWindow(it.id, it)
                logger.system("Created $it")
            }
        }
    }
}
