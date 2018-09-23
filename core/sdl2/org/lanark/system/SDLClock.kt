package org.lanark.system

import sdl2.*

actual class Clock {
    private val frequency: ULong = SDL_GetPerformanceFrequency()
    
    actual var start: ULong = SDL_GetPerformanceCounter()
        private set

    actual fun reset() {
        start = SDL_GetPerformanceCounter()
    }

    actual fun elapsedTicks(): ULong = SDL_GetPerformanceCounter() - start

    actual fun elapsedMillis(): ULong {
        val elapsed = elapsedTicks() * 1000u
        return (elapsed / frequency)
    }

    actual fun elapsedMicros(): ULong {
        val elapsed = elapsedTicks() * 1000000u
        return (elapsed / frequency)
    }

    actual fun elapsedSeconds(): ULong {
        return (elapsedTicks() / frequency)
    }
}