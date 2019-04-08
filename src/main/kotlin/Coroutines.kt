import kotlinx.coroutines.*

fun main(args: Array<String>) {
    exampleBlockingDispatcher()
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