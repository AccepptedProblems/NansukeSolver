import {Component, ElementRef} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'nansuke-client';

  constructor(private e1: ElementRef) {
    e1.nativeElement.style.display = 'block'
    e1.nativeElement.style.height = "100%"
  }
}
