package com.example.nansuke.model


data class NansukePuzzle(
    val boardSize: Int,
    val numbers: List<Long>,
    val matrix: MutableList<MutableList<Int>>
) {
    fun setValue(row: Int, col: Int, value: Int) {
        matrix[row][col] = value
    }
}

data class Solution(
    var numVar: Int = 0,
    var solutions: MutableList<List<Int>> = mutableListOf()
){

    fun add(value: Int) {
        val clause: MutableList<Int> = ArrayList()
        clause.add(value)
        solutions.add(clause)
    }

    fun add(clause: List<Int>) {
        solutions.add(clause)
        //solutions = solutions.plus(listOf(clause))
    }

    fun add(clauses: Solution) {
        solutions.addAll(clauses.solutions)
//        solutions = solutions.plus(clauses.solutions)
    }

    fun intToString(number: Int): String {
        return number.toString()
    }

    fun toSolString(): String {
        val clauses = arrayOf("")
        clauses[0] = String.format("p cnf %d %d%n", numVar, solutions.size)
        this.solutions.forEach { clause ->
            val cls = arrayOf("")
            clause.forEach { number -> cls[0] += intToString(number) + " " }
            cls[0] += "0\n"
            clauses[0] += cls[0]
        }
        return clauses[0]
    }
}

data class Cell(
    val row: Int,
    val col: Int,
)

enum class Method {
    NAIVE, SEQUENTIAL
}

data class NansukePuzzleResp(
    val result: NansukePuzzle,
    val time: String,
    val numVar: Int,
    val numClause: Int,
)