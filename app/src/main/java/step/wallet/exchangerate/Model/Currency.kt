package step.wallet.exchangerate.Model

class Currency(
    val code: String,
    val alphacode: String,
    val numericCode: String,
    val name: String,
    val rate: Double,
    val date: String,
    val inverseRate: Double
)