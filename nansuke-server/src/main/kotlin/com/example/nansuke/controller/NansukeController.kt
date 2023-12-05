package com.example.nansuke.controller

import com.example.nansuke.model.NansukePuzzle
import com.example.nansuke.model.NansukePuzzleResp
import com.example.nansuke.service.NansukeServiceImpl
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("api/v1/nansuke")
class NansukeController(private val nansukeService: NansukeServiceImpl) {

    @PostMapping("/extract", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun extractNansukePuzzle(@ModelAttribute file: MultipartFile): NansukePuzzle {
        return nansukeService.convertFileToData(file)
    }

    @PostMapping("/solve")
    fun solveNansukePuzzle(@RequestBody nansukePuzzle: NansukePuzzle): NansukePuzzleResp {
        return nansukeService.solvePuzzle(nansukePuzzle)
    }

}