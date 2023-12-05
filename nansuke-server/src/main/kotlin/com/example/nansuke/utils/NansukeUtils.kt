package com.example.nansuke.utils

import com.example.nansuke.model.Cell
import com.example.nansuke.model.NansukePuzzle
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class NansukeUtils {

    fun convert(file: MultipartFile): NansukePuzzle {
        val convFile: File = convertMultipartToFile(file)
        var matrix: MutableList<MutableList<Int>> = mutableListOf()
        var numbers: List<Long> = listOf()
        val reader = convFile.bufferedReader()
        val firstMatrixLine: MutableList<Int>
        var size = 0

        try {
            numbers = reader.readLine().split(" ").map { it.toLong() }

            firstMatrixLine = reader.readLine().split(" ").map { it.toInt() }.toMutableList()
            size = firstMatrixLine.size
            matrix.add(firstMatrixLine)
            for (i in 1 until size) {
                val line: MutableList<Int> = reader.readLine().split(" ").map { it.toInt() }.toMutableList()
                matrix.add(line)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            reader.close()
            convFile.delete()
        }

        return NansukePuzzle(size, numbers, matrix)
    }

    private fun convertMultipartToFile(multipartFile: MultipartFile): File {
        val convFile = File(multipartFile.originalFilename!!)
        val fos = FileOutputStream(convFile)
        fos.write(multipartFile.bytes)
        fos.close()
        return convFile
    }

    fun getNumberDigitInArray(arr: List<Long>): List<Int> {
        val hashMap = HashMap<Int, Boolean>()
        var result: List<Int> = mutableListOf()
        arr.forEach { item ->
            var value = item
            while (value > 0) {
                val digit = (value % 10).toInt()
                value /= 10

                if (hashMap[digit] == null) {
                    result = result.plus(digit)
                }

                hashMap[digit] = true
            }
        }
        result.sorted()
        return result
    }

    fun getNumberSizes(numbers: List<Long>): HashMap<Int, List<List<Int>>> {
        val result: HashMap<Int, List<List<Int>>> = HashMap()

        numbers.forEach { number ->
            val digits = extractNumberDigits(number)
            val size = digits.size
            if (result[size] != null) {
                var sizeTypes = result[size]!!
                sizeTypes = sizeTypes.plus(listOf(digits))
                result[size] = sizeTypes
            } else {
                result[size] = listOf(digits)
            }
        }

        return result
    }

    private fun extractNumberDigits(number: Long): List<Int> {
        var value = number
        var digits: List<Int> = mutableListOf()
        while (value > 0) {
            digits = digits.plus((value % 10).toInt())
            value /= 10
        }
        digits = digits.reversed()
        return digits
    }

    fun getCellMap(category: List<Int>, nansukePuzzle: NansukePuzzle): HashMap<Int, List<List<Cell>>> {
        val map: HashMap<Int, List<List<Cell>>> = HashMap()
        for (size in category) {
            map[size] = getCellsBySize(size, nansukePuzzle)
        }
        return map
    }

    private fun getCellsBySize(size: Int, nansukePuzzle: NansukePuzzle): List<List<Cell>> {
        var result: List<List<Cell>> = mutableListOf()
        val matrix = nansukePuzzle.matrix

        for (row in matrix.indices) {
            var cnt = 0
            val numCol = matrix[row].size

            for (col in 0 until numCol) {
                if ((editableCell(matrix, row, col) && col == numCol - 1 && cnt == size - 1) ||
                    (unEditableCell(matrix, row, col) && cnt == size)
                ) {
                    var cell: List<Cell> = mutableListOf()
                    var k = col - (if ((unEditableCell(matrix, row, col) && cnt == size)) 1 else 0)
                    for (i in 1..size) {
                        cell = cell.plus(Cell(row, k))
                        k--
                    }
                    cnt = 0
                    cell = cell.reversed()
                    result = result.plus(listOf(cell))
                } else {
                    if (editableCell(matrix, row, col)) {
                        cnt++
                    } else {
                        cnt = 0
                    }
                }
            }


        }

        for (col in matrix.indices) {
            var cnt = 0
            val numRow = matrix[col].size

            for (row in 0 until numRow) {
                if ((editableCell(matrix, row, col) && row == numRow - 1 && cnt == size - 1) ||
                    (unEditableCell(matrix, row, col) && cnt == size)
                ) {
                    var cells: List<Cell> = mutableListOf()
                    var k = row - (if ((unEditableCell(matrix, row, col) && cnt == size)) 1 else 0)
                    for (i in 1..size) {
                        cells = cells.plus(Cell(k, col))
                        k--
                    }
                    cnt = 0
                    cells = cells.reversed()
                    result = result.plus(listOf(cells))
                } else {
                    if (editableCell(matrix, row, col)) {
                        cnt++
                    } else {
                        cnt = 0
                    }
                }
            }


        }

        return result
    }

    private fun editableCell(matrix: List<List<Int>>, row: Int, col: Int): Boolean {
        return matrix[row][col] == 1
    }

    private fun unEditableCell(matrix: List<List<Int>>, row: Int, col: Int): Boolean {
        return matrix[row][col] == 0
    }
}