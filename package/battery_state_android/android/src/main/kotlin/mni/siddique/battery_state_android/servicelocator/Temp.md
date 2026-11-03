# ServiceLocator.kt review

## Decision

1. **Lazy singleton isn't thread-safe** (`ServiceLocator.kt:27-30`). Two threads can both pass `containsKey(kClass)` and both run `lazy[kClass]!!()`, giving two "singleton" instances.
   - **Decision:** guard lazy resolution with the single reentrant lock from item 3 (supersedes the earlier `computeIfAbsent` decision). Plain maps are enough under the lock.
   - The circular-dependency check is deferred (see Still undecided).

2. **Silent precedence and re-registration quirks.**
   - **Decision:** registering a type as both factory and lazy singleton must fail.
   - **Decision:** registering the same type twice must throw a duplicate-registration error. This also covers re-registering a lazy singleton after it has been resolved.

3. **Check-then-`!!` lookups** (`:19-20, 27-29`).
   - **Decision:** one reentrant lock (`synchronized(lock)` or `ReentrantLock`) around every `register*`, `get`, `unregister` and `reset`. `get` stays non-suspend. This closes the check-then-`!!` race and gives visibility across threads.
   - **Decision:** `unregister` removes the type from both maps (`lazyFactories` and the resolved-singleton map).
   - **Rejected:** `kotlinx.coroutines.sync.Mutex`. `lock()` is suspend, which would force every caller into a coroutine. It is also non-reentrant, so a factory calling `get<Other>()` would deadlock silently. Revisit only if factories become `suspend`; then resolve nested dependencies outside the lock.
   - Never call a suspend function inside the locked section.

4. **`get<T>()` has no `T : Any` bound.**
   - **Decision:** add the bound.

5. **Two different `get`s** (`ServiceLocator.get()` vs `locator.get<T>()`).
   - **Decision:** rename the instance-returning function to `getInstance()`.

6. **Singleton access is over-built** (`:49-56`).
   - **Decision:** simplify to `val instance by lazy { ServiceLocator() }`. Keep `getInstance()` from item 5 as a thin accessor if you still want the function form.

7. **The field name `lazy` shadows Kotlin's `lazy {}`.**
   - **Decision:** rename to `lazyFactories` and `factories`.

8. **Wrong exception type and weak message** (`:33`).
   - **Decision:** use `IllegalStateException` and `qualifiedName ?: toString()`.

9. **No way to reset state for tests.**
   - **Decision:** add an explicit `reset()` method.

10. **Public API surface.**
    - **Decision:** keep `inline reified` so instances are fetched by type, not class literal. There is no performance cost. The only trade-off is that the maps stay `@PublishedApi internal`.

11. **Stray prompt text in the file** (`ServiceLocator.kt:69-70`).
    - **Decision:** delete that comment block.

13. **Naming of registration lambdas.**
    - **Decision:** name every registration lambda parameter `factory`.

## Still undecided

1b. **Circular dependency check** (part of item 1).
    - With the reentrant lock, a cycle (A needs B needs A) recurses until `StackOverflowError` unless detected.
    - Option: keep a "currently resolving" set (guarded by the same lock) and throw "Circular dependency: A -> B -> A".
    - Deferred by you.
Decision: Add circular dependency catching. if circular dependency found do your recommendation of 'throw "Circular dependency: A -> B -> A"'

12. **Test plan** (`:62-66`). Deferred by you. Candidate additions to the five listed:
    - concurrent lazy resolution returns the same instance
    - lazy singleton returns the same instance across calls
    - factory returns a new instance each call
    - duplicate registration throws (item 2)
    - factory-and-lazy registration for the same type throws (item 2)
    - circular dependency throws (item 1b)
    - `reset()` clears all registrations (item 9)

