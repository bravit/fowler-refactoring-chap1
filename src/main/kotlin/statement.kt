import java.text.NumberFormat
import java.util.*
import kotlin.math.*

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

private fun renderToPlainText(statementData: StatementData): String {
    val result = StringBuilder("Statement for ${statementData.customer}\n")
    statementData.statementLines.forEach {
        result.appendLine(it)
    }
    result.append("Amount owed is ${asUSD(statementData.totalAmount)}\n")
    result.append("You earned ${statementData.volumeCredits} credits\n")
    return result.toString()
}

private fun asUSD(thisAmount: Int): String? = NumberFormat.getCurrencyInstance(Locale.US).format(thisAmount / 100)

private fun volumeCreditsFor(perf: Performance, play: Play): Int {
    var result = max(perf.audience - 30, 0)
    // add extra credit for every five comedy attendees
    if (play.type == PlayType.COMEDY)
        result += floor(perf.audience / 5.0).toInt()
    return result
}

private fun amountFor(perf: Performance, play: Play): Int {
    var result: Int
    when (play.type) {
        PlayType.TRAGEDY -> {
            result = 40000
            if (perf.audience > 30) {
                result += 1000 * (perf.audience - 30)
            }
        }
        PlayType.COMEDY -> {
            result = 30000
            if (perf.audience > 20) {
                result += 10000 + 500 * (perf.audience - 20)
            }
            result += 300 * perf.audience
        }
    }
    return result
}