package com.example.nansuke.utils

import com.example.nansuke.model.Solution

interface SATSolver {
    fun atLeastOne(vars: List<Int>): List<Int>
    fun atMostOne(vars: List<Int>): Solution
}