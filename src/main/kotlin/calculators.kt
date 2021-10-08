import kotlin.math.floor
import kotlin.math.max

val additionalVolumeCreditCalculator = mapOf(
    PlayType.COMEDY to ::calculateComedyAddVolumeCredit
)

fun volumeCreditsFor(perf: Performance, play: Play): Int {
    return max(perf.audience - 30, 0) +
            (additionalVolumeCreditCalculator[play.type]?.invoke(perf) ?: 0)
}

private fun calculateComedyAddVolumeCredit(perf: Performance) = floor(perf.audience / 5.0).toInt()

val amountCalculator = mapOf(
    PlayType.TRAGEDY to ::calculateTragedyAmount,
    PlayType.COMEDY to ::calculateComedyAmount
)

fun amountFor(perf: Performance, play: Play): Int {
    return amountCalculator[play.type]?.invoke(perf) ?:
    throw Exception("Undefined amount calculator")
}

private fun calculateComedyAmount(perf: Performance): Int {
    var result = 30000
    if (perf.audience > 20) {
        result += 10000 + 500 * (perf.audience - 20)
    }
    result += 300 * perf.audience
    return result
}

private fun calculateTragedyAmount(perf: Performance): Int {
    var result = 40000
    if (perf.audience > 30) {
        result += 1000 * (perf.audience - 30)
    }
    return result
}