import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NansukeComponent } from './nansuke.component';

describe('NansukeComponent', () => {
  let component: NansukeComponent;
  let fixture: ComponentFixture<NansukeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NansukeComponent]
    });
    fixture = TestBed.createComponent(NansukeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
