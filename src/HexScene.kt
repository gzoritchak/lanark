import ksdl.composition.*
import ksdl.events.*
import ksdl.grids.hex.*
import ksdl.resources.*
import ksdl.system.*

class HexScene(resources: KResourceScope) : KScene {
    var layout = HexLayout(HexLayout.orientationHorizontal, KSize(32, 32), KPoint(400, 400))
    val cornerIndices = listOf(1, 2, 3, 4, 5, 0)

    private var currentHex: HexCell = HexCell(0, 0)

    override fun activate(executor: KTaskExecutor) {

    }

    override fun deactivate(executor: KTaskExecutor) {
    }

    override fun render(renderer: KRenderer, cache: KTextureCache) {
        renderer.clear(Colors.BLACK)
        renderer.color(Colors.BLUE)
        for (q in -10..10)
            for (r in -10..10) {
                val hex = HexCell(q, r)
                renderer.renderHex(hex)
            }

        renderer.color(Colors.LIGHT_BLUE)
        renderer.renderHex(HexCell(0, 0))

        renderer.color(Colors.RED)
        renderer.renderHex(currentHex)

        renderer.present()

        if (rotate) {
            layout = layout.copy(orientation = layout.orientation + 0.01)
        }
    }

    fun KRenderer.renderHex(hex: HexCell) {
        val center = layout.cellCenter(hex)
        var previous = center + layout.cornerVectors[0]
        for (index in 0..5) {
            val current = center + layout.cornerVectors[cornerIndices[index]]
            drawLine(previous, current)
            previous = current
        }

    }

    override fun keyboard(event: KEventKey, executor: KTaskExecutor) {
    }

    private var rotate: Boolean = false

    override fun mouse(event: KEventMouse, executor: KTaskExecutor) {
        when (event) {
            is KEventMouseMotion -> {
                currentHex = layout.cellAt(event.position)
            }
            is KEventMouseDown -> {
                rotate = true
            }
            is KEventMouseUp -> {
                rotate = false
            }
        }
    }
}