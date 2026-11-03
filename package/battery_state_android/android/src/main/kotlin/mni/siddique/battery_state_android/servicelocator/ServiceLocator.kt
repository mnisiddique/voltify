package mni.siddique.battery_state_android.servicelocator

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class ServiceLocator private constructor() {
    @PublishedApi
    internal val factory = ConcurrentHashMap<KClass<*>, () -> Any>()

    @PublishedApi
    internal val lazySingletons = ConcurrentHashMap<KClass<*>, Any>()

    @PublishedApi
    internal val lazy = ConcurrentHashMap<KClass<*>, () -> Any>()

    @PublishedApi
    internal val typeStack = mutableListOf<KClass<*>>()

    inline fun <reified T> get(): T {
        val kClass = T::class

        return when {
            factory.containsKey(kClass) -> {
                factory[kClass]!!() as T
            }

            lazySingletons.containsKey(kClass) -> {
                lazySingletons[kClass] as T
            }

            lazy.containsKey(kClass) -> {
                lazySingletons[kClass] = lazy[kClass]!!()
                lazySingletons[kClass] as T
            }

            else -> {
                throw IllegalArgumentException("No registration found for ${kClass.simpleName}")
            }
        }

    }

    inline fun <reified T : Any> registerAsFactory(noinline reg: () -> T) {
        factory[T::class] = reg
    }

    inline fun <reified T : Any> registerAsLazySingleton(noinline reg: () -> T) {
        lazy[T::class] = reg
    }

    companion object {

        @Volatile
        private var instance: ServiceLocator? = null

        fun get(): ServiceLocator {
            return synchronized(this) {
                instance ?: ServiceLocator().also { instance = it }
            }
        }
    }
}

/*
* Additional Tests
* 1.
*
*
*
* */