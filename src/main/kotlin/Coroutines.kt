import kotlinx.coroutines.*

fun main(args: Array<String>) {
    exampleWithContext()
}


suspend fun printlnDelayed(message: String) {
    delay(1000)
    println(message)
}


// runBlocking runs new coroutine and blocks
// current thread interruptibly until its completion
fun exampleBlocking() = runBlocking {
    println("one")
    printlnDelayed("two")
    println("three")
}


// Running on another thread but still blocking the main thread
fun exampleBlockingDispatcher(){

    // Dispatchers will switch between different threads.
    // Dispatchers.Default runs the coroutine in another thread.
    runBlocking(Dispatchers.Default) {
        println("one - from thread ${Thread.currentThread().name}")
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }

    // Outside of runBlocking to show that it's running in the blocked main thread
    println("three - from thread ${Thread.currentThread().name}")

    // It still runs only after the runBlocking is fully executed.
}


fun exampleLaunchGlobal() = runBlocking {

    println("one - from thread ${Thread.currentThread().name}")

    // GlobalScope.launch doesn't block the main thread
    // The main thread won't await for this thread
    GlobalScope.launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }

    println("three - from thread ${Thread.currentThread().name}")
    delay(3000)
}


fun exampleLaunchGlobalWaiting() = runBlocking {

    println("one - from thread ${Thread.currentThread().name}")

    // GlobalScope.launch return a 'Job' object
    // that can be used to await for the coroutine to end,
    // or to stop the coroutine
    val job = GlobalScope.launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }

    println("three - from thread ${Thread.currentThread().name}")

    // Delay is not a good practice!!!
    // It's better to use job.join() :)
    job.join()
}


fun exampleLaunchCoroutineScope() = runBlocking {

    println("one - from thread ${Thread.currentThread().name}")

    // We're running 'launch' in a Local Scope,
    // not in the Glocal Scope
    // Type of dispatchers:
    //  - Default (another thread)
    //  - IO (shared pool thread with the main thread)
    //  - Main (android!! use)
    //  - Unconfined
    launch(Dispatchers.Default) {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }

    println("three - from thread ${Thread.currentThread().name}")

    // Job.join() is no longer needed, because the coroutine
    // knows that there is another task that needs to be completed
}


suspend fun calculateHardThing(startNum: Int) : Int {
    delay(1000)
    return startNum * 10
}


fun exampleAsyncAwait() = runBlocking {

    val startTime = System.currentTimeMillis()

    val deferred1 = this.async { calculateHardThing(10) }
    val deferred2 = this.async { calculateHardThing(20) }
    val deferred3 = this.async { calculateHardThing(30) }

    val sum = deferred1.await() + deferred2.await() + deferred3.await()
    val endTime = System.currentTimeMillis()

    println("async/await result: $sum")
    println("Total elapsed time: ${endTime - startTime} ")
}


fun exampleWithContext() = runBlocking {

    val startTime = System.currentTimeMillis()

    // withContext won't run the tasks concurrently,
    // unlike async, in which case must await for the task to be completed
    val result1 = withContext(Dispatchers.Default) { calculateHardThing(10) }
    val result2 = withContext(Dispatchers.Default){ calculateHardThing(20) }
    val result3 = withContext(Dispatchers.Default) { calculateHardThing(30) }

    val sum = result1 + result2 + result3
    val endTime = System.currentTimeMillis()

    println("async/await result: $sum")
    println("Total elapsed time: ${endTime - startTime} ")
}