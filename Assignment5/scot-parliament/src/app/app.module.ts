import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

import {AllMembersComponent} from './all-members/all-members.component';
import {MemberDetailComponent} from './member-detail/member-detail.component';
import {CacheService} from "./services/cache.service";

@NgModule({
    declarations: [
        AppComponent,
        AllMembersComponent,
        MemberDetailComponent,
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule,
        RouterModule
    ],
    providers: [
        CacheService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

