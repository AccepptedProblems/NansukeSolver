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

  particitionNumberByDigit(nums: number[]): number[][] {
    let result: number[][] = []
    let arr: number[] = []
    nums.sort((a, b) => a - b)
    for (let i = 0; i < nums.length; i++) {
      if(i == 0) {
        arr.push(nums[i])
        continue
      }
      if(this.countDigit(nums[i]) != this.countDigit(nums[i-1])) {
        result.push(arr)
        arr = []
      }
      arr.push(nums[i])
    }
    result.push(arr)
    return result
  }

  countDigit(number: number) {
    let cnt = 0
    while (number >= 1) {
      cnt++;
      number = number / 10
    }
    return cnt
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
