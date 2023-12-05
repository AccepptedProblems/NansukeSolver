package com.example.nansuke.service

import com.example.nansuke.model.NansukePuzzle
import com.example.nansuke.model.NansukePuzzleResp
import org.springframework.web.multipart.MultipartFile

interface NansukeService {
    fun convertFileToData(file: MultipartFile): NansukePuzzle
    fun solvePuzzle(nansukePuzzle: NansukePuzzle): NansukePuzzleResp
}