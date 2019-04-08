import kotlinx.coroutines.*

fun main(args: Array<String>) {
    exampleBlocking()
}


fun printlnDelayed(message: String) {

    Thread.sleep(1000)
    println(message)
}

fun exampleBlocking() {
    println("one")
    printlnDelayed("two")
    println("three")
}