package com.example.nansuke.utils

import com.example.nansuke.model.Method
import com.example.nansuke.model.Solution


open class BaseSolver : SATSolver {
    open var method: Method = Method.NAIVE
    open var maxVar: Int = 0

    private fun sequentialNewVar(index: Int): Int {
        return maxVar + index + 1
    }

    override fun atLeastOne(vars: List<Int>): List<Int> {
        return vars
    }

    override fun atMostOne(vars: List<Int>): Solution {
        val clauses = Solution()
        when (method) {
            Method.NAIVE -> {
                var i = 0
                while (i < vars.size - 1) {
                    var j = i + 1
                    while (j < vars.size) {
                        val clause: MutableList<Int> = ArrayList()
                        clause.add(-vars[i])
                        clause.add(-vars[j])
                        clauses.add(clause)
                        j++
                    }
                    i++
                }
            }

            Method.SEQUENTIAL -> {
                val newVarNumber: Int = vars.size - 1
                var i = 0
                while (i < vars.size) {
                    when (i) {
                        0 -> {
                            val clause: MutableList<Int> = ArrayList()
                            clause.add(-vars[i])
                            clause.add(sequentialNewVar(i))
                            clauses.add(clause)
                        }
                        vars.size - 1 -> {
                            val clause: MutableList<Int> = ArrayList()
                            clause.add(-sequentialNewVar(i - 1))
                            clause.add(-vars[i])
                            clauses.add(clause)
                        }
                        else -> {
                            var clause: MutableList<Int> = ArrayList()
                            clause.add(-vars[i])
                            clause.add(sequentialNewVar(i))
                            clauses.add(clause)

                            clause = ArrayList()
                            clause.add(-sequentialNewVar(i - 1))
                            clause.add(sequentialNewVar(i))
                            clauses.add(clause)

                            clause = ArrayList()
                            clause.add(-sequentialNewVar(i - 1))
                            clause.add(-vars[i])
                            clauses.add(clause)
                        }
                    }
                    i++
                }
                maxVar += newVarNumber
            }

        }
        return clauses
    }
}