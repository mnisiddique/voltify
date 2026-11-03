package mni.siddique.battery_state_android.servicelocator

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

/*
 * ServiceLocator is a process-wide singleton with a private constructor, so registrations leak
 * between tests. Every test therefore registers its own private type and never reuses one.
 */
internal class ServiceLocatorTest {
    private class FactoryDep
    private class LazyDep
    private class UnregisteredDep
    private class LazyCountedDep
    private class ReplacedDep(val tag: String)
    private class BothDep(val tag: String)
    private interface Contract
    private class ContractImpl : Contract

    private val locator = ServiceLocator.get()

    @Test
    fun get_returnsSameLocatorInstance() {
        assertSame(ServiceLocator.get(), ServiceLocator.get())
    }

    @Test
    fun registerInstanceAsFactory() {
        locator.registerAsFactory { FactoryDep() }

        assertTrue(locator.factory.containsKey(FactoryDep::class))
        assertTrue(!locator.lazy.containsKey(FactoryDep::class))
    }

    @Test
    fun registerInstanceAsLazySingleton() {
        locator.registerAsLazySingleton { LazyDep() }

        assertTrue(locator.lazy.containsKey(LazyDep::class))
        assertTrue(!locator.factory.containsKey(LazyDep::class))
    }

    @Test
    fun registerAsLazySingleton_doesNotCreateInstanceUntilFirstGet() {
        var created = 0
        locator.registerAsLazySingleton { created++; LazyCountedDep() }

        assertTrue(created == 0)
        locator.get<LazyCountedDep>()
        assertTrue(created == 1)
    }

    @Test
    fun getFactoryInstance() {
        var created = 0
        locator.registerAsFactory { created++; FactoryDep() }

        val first = locator.get<FactoryDep>()
        val second = locator.get<FactoryDep>()

        assertNotSame(first, second)
        assertTrue(created == 2)
    }

    @Test
    fun getLazySingletonInstance() {
        var created = 0
        locator.registerAsLazySingleton { created++; LazyDep() }

        val first = locator.get<LazyDep>()
        val second = locator.get<LazyDep>()
        val third = locator.get<LazyDep>()

        assertSame(first, second)
        assertSame(first, third)
        assertTrue(created == 1, "lazy singleton was built $created times")
    }

    @Test
    fun throwsIllegalArgumentExceptionOnGetIfNotRegistered() {
        val error = assertFailsWith<IllegalArgumentException> {
            locator.get<UnregisteredDep>()
        }
        assertTrue(error.message!!.contains("UnregisteredDep"))
    }

    @Test
    fun registerAsFactory_reRegistrationReplacesPreviousFactory() {
        locator.registerAsFactory { ReplacedDep("old") }
        locator.registerAsFactory { ReplacedDep("new") }

        assertTrue(locator.get<ReplacedDep>().tag == "new")
    }

    @Test
    fun get_factoryTakesPrecedenceWhenTypeIsRegisteredBothWays() {
        locator.registerAsLazySingleton { BothDep("lazy") }
        locator.registerAsFactory { BothDep("factory") }

        assertTrue(locator.get<BothDep>().tag == "factory")
    }

    @Test
    fun get_resolvesByRegisteredTypeNotByImplementation() {
        locator.registerAsFactory<Contract> { ContractImpl() }

        assertTrue(locator.get<Contract>() is ContractImpl)
        assertFailsWith<IllegalArgumentException> { locator.get<ContractImpl>() }
    }
}
