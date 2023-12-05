package com.example.nansuke.service

import com.example.nansuke.model.NansukePuzzle
import com.example.nansuke.model.NansukePuzzleResp
import com.example.nansuke.utils.NansukeSolver
import com.example.nansuke.utils.NansukeUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class NansukeServiceImpl : NansukeService {
    override fun convertFileToData(file: MultipartFile): NansukePuzzle {
        val nansukeUtils = NansukeUtils()
        return nansukeUtils.convert(file)
    }

    override fun solvePuzzle(nansukePuzzle: NansukePuzzle): NansukePuzzleResp {
        val solver = NansukeSolver()
        solver.setUpForPuzzle(nansukePuzzle)
        return solver.solve()
    }
}