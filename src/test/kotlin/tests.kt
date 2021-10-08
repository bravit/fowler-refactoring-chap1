import kotlin.test.Test
import kotlin.test.assertEquals

class TestStatement {

    @Test
    fun `statement on empty invoice`() {
        val testInvoice = Invoice("testCo", listOf())
        val plays: List<Play> = listOf()
        val stmt = """
            Statement for testCo
            Amount owed is $0.00
            You earned 0 credits
            """.trimIndent()
        assertEquals(stmt, statement(testInvoice, plays).trimIndent())
    }

    @Test
    fun `statement on prod data`() {
        val plays = listOf(
                Play("hamlet", "Hamlet", PlayType.TRAGEDY),
                Play("as-like", "As You Like it", PlayType.COMEDY),
                Play("othello", "Othello", PlayType.TRAGEDY)
        )
        val invoice = Invoice("BigCo", listOf(
                Performance("hamlet", 55),
                Performance("as-like", 35),
                Performance("othello", 40)
        ))
        val stmt = """
            Statement for BigCo
             Hamlet: $650.00 (55 seats)
             As You Like it: $580.00 (35 seats)
             Othello: $500.00 (40 seats)
            Amount owed is $1,730.00
            You earned 47 credits
            """.trimIndent()
        assertEquals(stmt, statement(invoice, plays).trimIndent())
    }
}