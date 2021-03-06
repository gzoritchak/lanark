package org.lanark.ui

import org.lanark.application.*
import org.lanark.drawing.*
import org.lanark.geometry.*
import org.lanark.resources.*

class Button(val position: Point, private val resources: ResourceContext) : Control() {
    private val tiles = resources.loadTiles("elements")
    private val button = tiles["button"]
    private val buttonHover = tiles["button-hover"]
    private val buttonPressed = tiles["button-pressed"]
    private val buttonDisabled = tiles["button-disabled"]

    override fun render(frame: Frame, area: Rect) {
        frame.draw(button, position.relativeTo(area.origin))
    }
}