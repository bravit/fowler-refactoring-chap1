data class Play(val id: String, val name: String, val type: String)
data class Performance(val playID: String, val audience: Int)
data class Invoice(val customer: String, val performances: List<Performance>)

val PLAYS = listOf(
    Play("hamlet", "Hamlet", "tragedy"),
    Play("as-like", "As You Like it", "comedy"),
    Play("othello", "Othello", "tragedy")
)

val INVOICE = Invoice(
    "BigCo", listOf(
        Performance("hamlet", 55),
        Performance("as-like", 35),
        Performance("othello", 40)
    )
)