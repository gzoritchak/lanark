package ksdl.resources

import ksdl.system.*

class KTextureCache(val renderer: KRenderer) {
    private val textures = mutableMapOf<KSurface, KTexture>()

    fun getTexture(surface: KSurface): KTexture {
        return textures.getOrPut(surface) { surface.toTexture(renderer) }
    }

    fun release() {
        textures.forEach { it.value.release() }
    }
}

fun KSurface.toTexture(cache: KTextureCache) = cache.getTexture(this)