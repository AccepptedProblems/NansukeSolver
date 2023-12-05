import { Injectable } from '@angular/core';
import {Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SidebarService {

  constructor() { }

  getSideBarMenu(): Observable<SideBarItem[]> {
    let menus: SideBarItem[] = [
      {
        id: "1",
        name: "Nansuke solver",
        icon: "",
        path: "/nansuke",
        isActive: false
      }
    ]
    let selectedItemId = this.getSelectedItemId()
    menus.forEach(menu => {
      if(menu.id === selectedItemId) menu.isActive = true
    })
    return of(menus)
  }

  selectMenuItem(menuItem: SideBarItem) {
    window.sessionStorage.setItem("menuItemId", menuItem.id)
  }

  getSelectedItemId(): string|null {
    return window.sessionStorage.getItem("menuItemId")
  }
}

export interface SideBarItem {
  id: string,
  name: string,
  icon: string,
  path: string,
  isActive: boolean
}
