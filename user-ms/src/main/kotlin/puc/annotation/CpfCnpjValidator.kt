package puc.annotation

import CpfCnpj
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CpfCnpjValidator : ConstraintValidator<CpfCnpj, String> {
    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        return isValidCpfOrCnpj(value)
    }

    fun isValidCpfOrCnpj(stringNumber: String): Boolean {
        return when (stringNumber.length) {
            11 -> {
                isValidCpf(stringNumber)
            }

            14 -> {
                isValidCpnj(stringNumber)
            }

            else -> {
                false
            }
        }
    }

    private fun isValidCpf(cpf: String): Boolean {
        val firstActualVerifier = cpf[9].digitToInt()
        val secondActualVerifier = cpf[10].digitToInt()

        // first verification number calculate
        val firstNineNumbersOfCpf = cpf.subSequence(0, 9).toString()
        val firstCalculation = getCpfNumbersCalculation(8, firstNineNumbersOfCpf)
        val realFirstVerifier = getRealVerifierNumber(firstCalculation)

        if (realFirstVerifier != firstActualVerifier) {
            return false
        }

        // second verification number calculate
        val firstTenNumbersOfCpf = cpf.subSequence(0, 10).toString()
        val secodCalculation = getCpfNumbersCalculation(9, firstTenNumbersOfCpf)
        val realSecondVerifier = getRealVerifierNumber(secodCalculation)
        return (realSecondVerifier == secondActualVerifier)
    }

    private fun getRealVerifierNumber(calculationTotal: Int): Int {
        val modOfFirstDvision = calculationTotal % 11
        return if (modOfFirstDvision < 2) {
            0
        } else {
            11 - modOfFirstDvision
        }
    }

    private fun getCpfNumbersCalculation(rangeEnd: Int, numbers: String) =
        IntProgression.fromClosedRange(0, rangeEnd, 1)
            .reversed()
            .map { position ->
                val multiplier = (rangeEnd - position) + 2
                multiplier * numbers[position].digitToInt()
            }
            .reduce(Integer::sum)

    private fun isValidCpnj(cnpj: String): Boolean {
        val firstBaseCnpjNumbersMultipliers = arrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
        val secondBaseCnpjNumbersMultipliers = arrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

        val firstTwelveNumbersOfCnpj = cnpj.subSequence(0, 12).toString()
        val actualFirstVerifier = cnpj[12].digitToInt()
        val actualSecondVerifier = cnpj[13].digitToInt()

        val firstCalculation = calculateCnpjBaseNumbers(firstBaseCnpjNumbersMultipliers, firstTwelveNumbersOfCnpj)
        val realFirstVerifier = getRealVerifierNumber(firstCalculation)
        if (realFirstVerifier != actualFirstVerifier) {
            return false
        }

        val firstThirteenNumbersOfCnpj = cnpj.subSequence(0, 13).toString()
        val secondCalculation = calculateCnpjBaseNumbers(secondBaseCnpjNumbersMultipliers, firstThirteenNumbersOfCnpj)

        val realSecondVerifier = getRealVerifierNumber(secondCalculation)
        return (actualSecondVerifier == realSecondVerifier)
    }

    private fun calculateCnpjBaseNumbers(baseMultipliers: Array<Int>, numers: String) =
        baseMultipliers.mapIndexed { position, value -> numers[position].digitToInt() * value }
            .reduce(Integer::sum)
}