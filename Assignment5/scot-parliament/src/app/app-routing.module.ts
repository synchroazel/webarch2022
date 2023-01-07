import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {AllMembersComponent} from './all-members/all-members.component';
import {MemberDetailComponent} from './member-detail/member-detail.component';

const routes: Routes = [
  {path: 'members', component: AllMembersComponent},
  {path: 'member/:id', component: MemberDetailComponent},
  {path: '', redirectTo: '/members', pathMatch: 'full'}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {useHash: true})
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
