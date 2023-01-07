import {Component, OnInit} from '@angular/core';
import {ParliamentScotService} from '../services/parliament-scot.service';
import {CacheService} from "../services/cache.service";

@Component({
    selector: 'app-members',
    templateUrl: './all-members.component.html',
    styleUrls: ['./all-members.component.css']
})
export class AllMembersComponent implements OnInit {

    allMembers: Array<any> = [];

    cache = CacheService;

    constructor(private memberService: ParliamentScotService) {
    }

    ngOnInit() {

        this.resolveMembers();

    }

    resolveMembers() {

        if (this.cache.get('allMembers')) {

            console.log('[INFO] Full members list found in cache, using cached data.')

            this.allMembers = this.cache.get('allMembers');

        } else {

            console.log('[INFO] Full members list not found in cache, fetching data.');

            this.memberService.getMembers()
                .subscribe(data => {

                    for (let i = 0; i < data.length; i++) {

                        this.allMembers.push({
                            "Id": data[i].PersonID,
                            "Fullname": data[i].ParliamentaryName,
                            "Birthdate": data[i].BirthDate,
                            "PhotoUrl": data[i].PhotoURL,
                        });

                    }

                    this.cache.set('allMembers', this.allMembers);

                });

        }

    }

}
