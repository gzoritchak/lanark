package ksdl.composition

import ksdl.events.*
import ksdl.resources.*
import ksdl.system.*

class KComposer(val executor: KTaskExecutor, val renderer: KRenderer) {
    private val events = KEvents()

    var scene: KScene? = null

    private var activeScene: KScene? = null
    private var activeTextureCache: KTextureCache? = null

    private fun deactivate(activeScene: KScene?) {
        activeScene?.also { scene ->
            scene.deactivate(executor)
            activeTextureCache?.release()
            activeTextureCache = null
            this.activeScene = null
            logger.composer("Deactivated $scene")
        }
    }

    private fun activate(activeScene: KScene?) {
        activeScene?.also { scene ->
            logger.composer("Activating $scene")
            activeTextureCache = KTextureCache(renderer)
            scene.activate(executor)
        }
    }

    private val beforeHandler: (Unit) -> Unit = {
        if (scene != activeScene) {
            deactivate(activeScene)
            activeScene = scene
            activate(activeScene)
        }
        events.pollEvents()
    }

    private val afterHandler: (Unit) -> Unit = {
        scene?.render(renderer, activeTextureCache!!)
    }

    fun run() {
        executor.before.subscribe(beforeHandler)
        executor.after.subscribe(afterHandler)

        events.keyboard.subscribe { activeScene?.keyboard(it, executor) }
        events.mouse.subscribe { activeScene?.mouse(it, executor) }
        events.window.subscribe {
            // TODO: Decide on automatic resizing?
            if (it is KEventWindowResized) {
                renderer.size = KSize(it.width, it.height)
            }
        }

        events.application.subscribe {
            when (it) {
                is KEventAppQuit -> executor.stop()
            }
        }

        executor.run()
        deactivate(activeScene)
        activeScene = null

        executor.before.unsubscribe(beforeHandler)
        executor.after.unsubscribe(afterHandler)
    }

    companion object {
        val logCategory = KLogCategory("Composer", "\u001B[0;35m")
    }
}

fun KLogger.composer(message: String) = log(KComposer.logCategory, message)
