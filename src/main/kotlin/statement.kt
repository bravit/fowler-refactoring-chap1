import java.text.NumberFormat
import java.util.*
import kotlin.math.*

fun statement(invoice: Invoice, plays: List<Play>): String {
    var totalAmount = 0
    var volumeCredits = 0
    var result = "Statement for ${invoice.customer}\n"
    val format = NumberFormat.getCurrencyInstance(Locale.US)

    for(perf in invoice.performances) {
        val play = plays.find { it.id == perf.playID } ?:
            throw Exception("Unknown play: ${perf.playID}")
        var thisAmount: Int
        when (play.type) {
            PlayType.TRAGEDY -> {
                thisAmount = 40000
                if (perf.audience > 30) {
                    thisAmount += 1000 * (perf.audience - 30)
                }
            }
            PlayType.COMEDY -> {
                thisAmount = 30000
                if (perf.audience > 20) {
                    thisAmount += 10000 + 500 * (perf.audience - 20)
                }
                thisAmount += 300 * perf.audience
            }
        }
        // add volume credits
        volumeCredits += max(perf.audience - 30, 0)
        // add extra credit for every ten comedy attendees
        if (play.type == PlayType.COMEDY)
            volumeCredits += floor(perf.audience / 5.0).toInt()
        // print line for this order
        result += " ${play.name}: ${format.format(thisAmount / 100)} (${perf.audience} seats)\n"
        totalAmount += thisAmount
    }
    result += "Amount owed is ${format.format(totalAmount / 100)}\n"
    result += "You earned $volumeCredits credits\n"
    return result
}