fun main(args: Array<String>) {

    // Original
    val num = 4
    println("The number is ${(if(isEven(num)) "Even" else "Odd")}")
    num = 9
    println("The number is ${(if(isEven(num)) "Even" else "Odd")}")
    
    
    // Rx
    val subject: Subject<Int> = PublishSubject.create()

    subject.map { isEven(it) }
        .subscribe {
            println("The number is ${(if (it) "Even" else "Odd")}")
        }

    subject.onNext(4)
    subject.onNext(9)
}

fun isEven(n: Int): Boolean = (n % 2 == 0)