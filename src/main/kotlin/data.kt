enum class PlayType {
    TRAGEDY,
    COMEDY
}

data class Play(val id: String, val name: String, val type: PlayType)
data class Performance(val playID: String, val audience: Int)
data class Invoice(val customer: String, val performances: List<Performance>)

val PLAYS = listOf(
    Play("hamlet", "Hamlet", PlayType.TRAGEDY),
    Play("as-like", "As You Like it", PlayType.COMEDY),
    Play("othello", "Othello", PlayType.TRAGEDY)
)

val INVOICE = Invoice(
    "BigCo", listOf(
        Performance("hamlet", 55),
        Performance("as-like", 35),
        Performance("othello", 40)
    )
)