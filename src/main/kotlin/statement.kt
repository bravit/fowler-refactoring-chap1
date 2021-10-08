import java.text.NumberFormat
import java.util.*
import kotlin.math.*

data class StatementLine(val playName: String, val amount: Int,
                         val volumeCredits: Int, val audience: Int) {
    override fun toString(): String {
        return " $playName: ${asUSD(amount)} (${audience} seats)"
    }
}

fun statement(invoice: Invoice, plays: List<Play>): String {
    var totalAmount = 0
    var volumeCredits = 0
    var result = "Statement for ${invoice.customer}\n"

    for(perf in invoice.performances) {
        val play = plays.find { it.id == perf.playID } ?:
            throw Exception("Unknown play: ${perf.playID}")
        val statementLine = StatementLine(play.name, amountFor(perf, play),
                                          volumeCreditsFor(perf, play), perf.audience)
        volumeCredits += statementLine.volumeCredits
        result += "$statementLine\n"
        totalAmount += statementLine.amount
    }
    result += "Amount owed is ${asUSD(totalAmount)}\n"
    result += "You earned $volumeCredits credits\n"
    return result
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