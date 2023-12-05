import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NansukeService {
  url = "http://localhost:8081/api/v1/nansuke"
  constructor(private http: HttpClient) { }

  extractData(file: File): Observable<NansukePuzzle> {
    let formData = new FormData()
    formData.append('file', file)

    return this.http.post<NansukePuzzle>(this.url+"/extract", formData)
  }

  solve(nansukePuzzle: NansukePuzzle): Observable<NansukePuzzleResult> {
    return this.http.post<NansukePuzzleResult>(this.url+'/solve', nansukePuzzle)
  }


}

export interface NansukePuzzle {
  boardSize: number,
  numbers: number[],
  matrix: number[][]
}

export interface NansukePuzzleResult{
  result: NansukePuzzle,
  time: string,
  numVar: number,
  numClause: number
}
