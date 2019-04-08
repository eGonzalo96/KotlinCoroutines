import kotlinx.coroutines.*

fun main(args: Array<String>) {
    exampleBlocking()
}


suspend fun printlnDelayed(message: String) {
    delay(5000)
    println(message)
}

fun exampleBlocking() {
    println("one")
    runBlocking {
        // Runs new coroutine and blocks
        // current thread interruptibly until its completion
        printlnDelayed("two")
    }

    println("three")
}