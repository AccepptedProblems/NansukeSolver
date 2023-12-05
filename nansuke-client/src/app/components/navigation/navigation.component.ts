import {Component, OnInit} from '@angular/core';
import {SideBarItem, SidebarService} from "../../services/sidebar.service";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit{
  menus: SideBarItem[] = []
  sub!: Subscription

  constructor(private sidebarService: SidebarService,
              private router: Router) {
  }

  ngOnInit() {
    this.sub = this.sidebarService.getSideBarMenu().subscribe(
      data => this.menus = data
    )
  }

  onClickItem(menuItem: SideBarItem) {
    this.sidebarService.selectMenuItem(menuItem)
    menuItem.isActive = true
    this.router.navigate([menuItem.path])
  }
}
