import { Component } from '@angular/core';
import {NansukePuzzle, NansukeService} from "../../services/nansuke.service";

@Component({
  selector: 'app-nansuke',
  templateUrl: './nansuke.component.html',
  styleUrls: ['./nansuke.component.scss']
})
export class NansukeComponent {
  fileToUpload: File | null = null
  puzzle: NansukePuzzle | null = null
  solvedPuzzle: NansukePuzzle | null = null
  numVar: number = 0
  numClauses: number = 0
  time: string = ""
  indexes: number[] = []
  numbersBySize: number[][] = []

  constructor(private nansukeService: NansukeService) {
  }

  handleFileInput(event: any) {
    this.fileToUpload = event.target.files[0]
  }

  extractPuzzle() {
    if (this.fileToUpload == null) {
      window.alert("Please input file to upload")
      return
    }
    this.puzzle = null
    this.solvedPuzzle = null
    this.nansukeService.extractData(this.fileToUpload).subscribe(
      data => {
        this.puzzle = data
        this.indexes = Array(this.puzzle.boardSize).fill(1).map((x, i)=>i);
        this.numbersBySize = this.nansukeService.particitionNumberByDigit(this.puzzle.numbers)
        console.log(this.numbersBySize)
      }
    )
  }

  solvePuzzle() {
    if (this.puzzle == null) {
      return
    }
    this.nansukeService.solve(this.puzzle).subscribe(
      data => {
        this.solvedPuzzle = data.result
        this.time = data.time
        this.numVar = data.numVar
        this.numClauses = data.numClause
      }
    )
  }


  protected readonly Math = Math;
}
