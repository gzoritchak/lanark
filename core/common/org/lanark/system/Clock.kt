package org.lanark.system

expect class Clock() {
    var start: ULong
        private set

    fun reset()
    fun delay(millis: ULong)
    fun elapsedTicks(): ULong
    fun elapsedMillis(): ULong
    fun elapsedMicros(): ULong
    fun elapsedSeconds(): ULong
}