import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {NansukeComponent} from "./components/nansuke/nansuke.component";

const routes: Routes = [
  {path: '', component:NansukeComponent },
  {path: 'nansuke', component: NansukeComponent},
  {path: "**", redirectTo: ''},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
