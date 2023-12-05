package com.example.nansuke.utils

import com.example.nansuke.model.*
import org.sat4j.core.VecInt
import org.sat4j.minisat.SolverFactory
import org.sat4j.specs.ContradictionException
import org.sat4j.specs.TimeoutException
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException


class NansukeSolver : BaseSolver() {
    override var method: Method = Method.NAIVE
    override var maxVar: Int = 0
    private var numSizes: List<Int> = mutableListOf()
    private var numbersWithSize: HashMap<Int, List<List<Int>>> = HashMap()
    private var cellsWithSize: HashMap<Int, List<List<Cell>>> = HashMap()
    private var nansukePuzzle = NansukePuzzle(0, listOf(), mutableListOf())
    private val nansukeUtils = NansukeUtils()
    private var digits = listOf<Int>()
    private var solution: Solution = Solution()
    private val answers: MutableList<Int> = mutableListOf()
    var time: String = ""

    fun setUpForPuzzle(nansukePuzzle: NansukePuzzle) {
        this.nansukePuzzle = nansukePuzzle
        this.numbersWithSize = nansukeUtils.getNumberSizes(this.nansukePuzzle.numbers)
        for ((key, _) in this.numbersWithSize) {
            this.numSizes = this.numSizes.plus(key)
        }
        this.numSizes = this.numSizes.sorted()
        this.digits = nansukeUtils.getNumberDigitInArray(nansukePuzzle.numbers)
        this.cellsWithSize = nansukeUtils.getCellMap(numSizes, nansukePuzzle)
        this.maxVar = nansukePuzzle.boardSize * nansukePuzzle.boardSize * 10
    }

    fun solve(method: Method = Method.NAIVE): NansukePuzzleResp {
        val start1 = System.nanoTime()
        this.method = method
        this.solution.numVar = maxVar

        getCnfSolution()

        val solutions: List<List<Int>> = solution.solutions
        val nbClauses = solutions.size
        val solver = SolverFactory.newDefault()

        solver.newVar(maxVar)
        solver.setExpectedNumberOfClauses(nbClauses)
        solver.timeout = 3600

        for (clause in solutions) {
            val cls = IntArray(clause.size)
            for (i in clause.indices) cls[i] = clause[i]
            try {
                solver.addClause(VecInt(cls))
            } catch (e: ContradictionException) {
                e.printStackTrace()
            }
        }

        // Get answer
        try {
            if (solver.isSatisfiable) {
                val model = solver.model()
                for (i in model) {
                    if (i > 0) {
                        println(i)
                        answers.add((i - 1) % 10)
                    }
                }
                val start2 = System.nanoTime()
                this.time = ((start2 - start1).toDouble() / 1000000000.0).toString()
                println(time)
            } else throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Cannot solve this puzzle"
            )
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }

        // Apply answer to puzzle
        var count = 0
        for (i in 0 until nansukePuzzle.boardSize)
            for (j in 0 until nansukePuzzle.boardSize) {
                nansukePuzzle.setValue(i, j, answers[count++])
            }

        return NansukePuzzleResp(result = nansukePuzzle, time = time, numVar = maxVar, numClause = solution.solutions.size)
    }

    private fun getCnfSolution() {
        val matrix = nansukePuzzle.matrix

        // Editable cell must not have value 0, uneditable cell must have value 0
        // Rule 1:

        val rule1Solution = Solution()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 1) {
                    rule1Solution.add(-variable(Cell(i, j), 0))
                } else {
                    rule1Solution.add(variable(Cell(i, j), 0))
                }
            }
        }

        for (i in matrix.indices) {
            for (j in matrix.indices) {
                // Each value has only 1 value
                var vars: MutableList<Int> = mutableListOf()
                for (digit in 0..9) {
                    vars.add(variable(Cell(i, j), digit))
                }
                rule1Solution.add(atLeastOne(vars))
                rule1Solution.add(atMostOne(vars))

                // Limit value cell can receive
                for (digit in 1..9) {
                    if (!digits.contains(digit)) {
                        rule1Solution.add(-variable(Cell(i, j), digit))
                    }
                }
            }
        }

        solution.add(rule1Solution)
        for (numSize in numSizes) {
            val numbersList = numbersWithSize[numSize]!!
            val cellsList = cellsWithSize[numSize]!!

            //Rule 3: Each number cannot write more than 1 time
            val rule3Solution = Solution()

            for (numbers in numbersList) {

                if (cellsList.size == 1) {
                    for(index in cellsList[0].indices) {
                        rule3Solution.add(variable(cellsList[0][index], numbers[index]))
                    }
                    break
                }

                for(i in cellsList.indices) {
                    for(j in i+1 until cellsList.size) {
                        val vars: MutableList<Int> = mutableListOf()
                        val cell1 = cellsList[i]
                        val cell2 = cellsList[j]

                        for (index in cell1.indices) {
                            vars.add(-variable(cell1[index], numbers[index]))
                        }
                        for (index in cell2.indices) {
                            vars.add(-variable(cell2[index], numbers[index]))
                        }

                        rule3Solution.add(vars)
                    }
                }

            }

            solution.add(rule3Solution)

            //Rule 2: Luat keo theo
            val overlapNums: MutableList<List<Int>> = mutableListOf()
            val notOverlapNums: MutableList<List<Int>> = mutableListOf()

            for (i in numbersList.indices) {
                val numbers = numbersList[i]
                if (overlapNums.contains(numbers)) continue
                var flag = false
                for (j in i + 1 until numbersList.size) {
                    if (numbers[0] == numbersList[j][0]) {
                        if (!overlapNums.contains(numbers)) {
                            overlapNums.add(numbers)
                        }
                        overlapNums.add(numbersList[j])
                        flag = true
                    }
                }
                if (!flag) {
                    notOverlapNums.add(numbers)
                }
            }

            // for not overlap
            cellsList.forEach { cells ->
                notOverlapNums.forEach { notOverlapNum ->
                    for (i in 1 until notOverlapNum.size) {
                        val vars: MutableList<Int> = mutableListOf()
                        vars.add(-variable(cells[0], notOverlapNum[0]))
                        vars.add(variable(cells[i], notOverlapNum[i]))
                        solution.add(vars)
                    }
                }

                for (i in cells.indices) {
                    val vars: MutableList<Int> = mutableListOf()
                    for (numbers in numbersList) {
                        vars.add(variable(cells[i], numbers[i]))
                    }
                    solution.add(vars)
                }


            }

            //for overlap
            val possibleStartValues: MutableList<Int> = overlapNums.map { it[0] }.distinct().toMutableList()
            for (index in cellsList.indices) {
                val cells = cellsList[index]
                for (startValue in possibleStartValues) {
                    for (i in 1 until cells.size) {
                        val clause: MutableList<Int> = mutableListOf()
                        val possibleStartValues2: MutableList<Int> = mutableListOf()
                        clause.add(-variable(cells[0], startValue))

                        overlapNums.forEach { overlapNum ->
                            val valueStart = overlapNum[0]
                            val valueAtIndex = overlapNum[i]

                            if (!possibleStartValues2.contains(valueAtIndex) && valueStart == startValue) {
                                possibleStartValues2.add(valueAtIndex)
                            }
                        }

                        for (val2 in possibleStartValues2) {
                            clause.add(variable(cells[i], val2))
                        }

                        solution.add(clause)
                    }
                }
            }

        }
    }

    private fun variable(cell: Cell, value: Int): Int {
        val size = nansukePuzzle.boardSize
        return ((cell.row * size + cell.col) * 10 + value + 1)
    }

}

