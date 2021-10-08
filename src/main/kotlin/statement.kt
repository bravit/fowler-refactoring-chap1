data class StatementLine(val playName: String, val amount: Int,
                         val volumeCredits: Int, val audience: Int) {
    override fun toString(): String {
        return " $playName: ${asUSD(amount)} (${audience} seats)"
    }
}

data class StatementData(val customer: String,
                         val statementLines: List<StatementLine>) {
    val volumeCredits = statementLines.sumOf { it.volumeCredits }
    val totalAmount = statementLines.sumOf { it.amount }
}

fun statement(invoice: Invoice, plays: List<Play>): String {
    val statementData = statementData(invoice, plays)
    return renderToPlainText(statementData)
}

private fun statementData(invoice: Invoice, plays: List<Play>): StatementData {
    fun findPlay(perf: Performance): Play {
        return plays.find { it.id == perf.playID } ?: throw Exception("Unknown play: ${perf.playID}")
    }

    fun statementLine(perf: Performance): StatementLine {
        val play = findPlay(perf)
        return StatementLine(
            play.name, amountFor(perf, play),
            volumeCreditsFor(perf, play), perf.audience
        )
    }

    return StatementData(
        invoice.customer,
        invoice.performances.map(::statementLine)
    )
}