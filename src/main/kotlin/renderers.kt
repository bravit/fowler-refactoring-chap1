import java.text.NumberFormat
import java.util.*

fun renderToPlainText(statementData: StatementData): String {
    val result = StringBuilder("Statement for ${statementData.customer}\n")
    statementData.statementLines.forEach {
        result.appendLine(it)
    }
    result.append("Amount owed is ${asUSD(statementData.totalAmount)}\n")
    result.append("You earned ${statementData.volumeCredits} credits\n")
    return result.toString()
}

fun asUSD(thisAmount: Int): String? = NumberFormat.getCurrencyInstance(Locale.US).format(thisAmount / 100)