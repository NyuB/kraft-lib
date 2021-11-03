package syntax

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class KotlinSyntaxTests {
    class InRangeTest(number: Int) {
        var number = number
            set(value) {
                if (value in 0..42) {
                    field = value
                } else {
                    throw IllegalArgumentException("Number must be in 0..42 range")
                }
            }
    }

    class InRangeWithConstructorCheck(number: Int) {
        var number = validateNumber(number)
            set(value) {
                if (value in 0..42) {
                    field = value
                } else {
                    throw IllegalArgumentException("Number must be in 0..42 range")
                }
            }

        private fun validateNumber(n: Int): Int {
            if (n in 0..42) {
                return n
            } else {
                throw IllegalArgumentException("Number must be in 0..42 range")
            }
        }

    }

    @Test
    fun `Constructor assignment is not done via setter`() {
        val outOfRange = InRangeTest(43)
        assertEquals(43, outOfRange.number)
    }

    @Test
    fun `Must check field assignment`() {
        assertThrows(IllegalArgumentException::class.java) {
            val outOfRange = InRangeWithConstructorCheck(43)
            assertEquals(43, outOfRange.number)
        }

    }
}