import java.text.NumberFormat
import java.util.*
import kotlin.math.*

fun statement(invoice: Invoice, plays: List<Play>): String {
    var totalAmount = 0
    var volumeCredits = 0
    var result = "Statement for ${invoice.customer}\n"

    for(perf in invoice.performances) {
        val play = plays.find { it.id == perf.playID } ?:
            throw Exception("Unknown play: ${perf.playID}")
        val thisAmount = amountFor(perf, play)
        // add volume credits
        volumeCredits += volumeCreditsFor(perf, play)
        // print line for this order
        result += " ${play.name}: ${asUSD(thisAmount)} (${perf.audience} seats)\n"
        totalAmount += thisAmount
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