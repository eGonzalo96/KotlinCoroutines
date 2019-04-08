import kotlinx.coroutines.*

fun main(args: Array<String>) {
    exampleLaunchGlobalWaiting()
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