import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {DatePipe} from '@angular/common';

import {ParliamentScotService} from '../services/parliament-scot.service';
import {CacheService} from "../services/cache.service";

@Component({
    selector: 'app-member-detail',
    templateUrl: './member-detail.component.html',
    styleUrls: ['./member-detail.component.css'],
    providers: [DatePipe]
})

export class MemberDetailComponent implements OnInit {

    // info about the member to display detail of
    MemberFullname!: string;
    MemberBirthDate!: string | null;
    MemberPhotoURL!: string;

    MemberParties!: any[];
    MemberFromDates!: string[];
    MemberToDates!: string[];

    MemberWebsiteGov!: string;
    MemberWebsitePersonal!: string;

    // used to check if all data is loaded and template can be displayed
    memberLoaded = false;
    partiesLoaded = false;
    websitesLoaded = false;

    cache = CacheService;

    constructor(
        private route: ActivatedRoute,
        private ParliamentScot: ParliamentScotService,
        public datePipe: DatePipe
    ) {
    }

    ngOnInit(): void {

        const id = this.route.snapshot.paramMap.get('id');

        if (id != null) {

            // resolve (:fetch from cache or APIs) all data needed to display

            this.resolveMember(id).then(data => {

                // get the member's name, birthdate and photo using getMember()

                this.MemberFullname = data.ParliamentaryName;
                this.MemberBirthDate = this.datePipe.transform(data.BirthDate, "MMMM dd, yyyy")
                this.MemberPhotoURL = data.PhotoURL;

                this.memberLoaded = true;  // all basic member info has been handled

            });

            this.resolveMemberParties(id).then(data => {

                // get the list of Parties the member has been in using getMemberParties() and getParties

                let inCharge: boolean;  // to check if we need to change the last toDate with "present"

                let memberPartiesData = data.filter((memberParties: { PersonID: string; }) => memberParties.PersonID == id);

                let memberPartiesIds = new Array<number>;
                let fromDates = new Array<Date>;
                let toDates = new Array<Date>;

                for (let memberPartyData of memberPartiesData) {

                    memberPartiesIds.push(memberPartyData.PartyID);

                    fromDates.push(memberPartyData.ValidFromDate);

                    if (memberPartyData.ValidUntilDate != null) {
                        toDates.push(memberPartyData.ValidUntilDate);
                    } else {
                        // if still in the party, set the toDate to current date instead of null
                        inCharge = true;
                        toDates.push(new Date());
                    }

                }

                // fromDates and toDates may come unsorted! Better do the following

                fromDates = fromDates.sort();
                toDates = toDates.sort();

                // if some parties listed are "contiguous", merge them into a single item

                let memberPartiesIdsRed = new Array<any>;
                let fromDatesRed = new Array<any>;
                let toDatesRed = new Array<any>;

                for (let i = 0; i < memberPartiesIds.length; i++) {

                    if (memberPartiesIds[i] != memberPartiesIds[i - 1]) {
                        fromDatesRed.push(fromDates[i]);
                        memberPartiesIdsRed.push(memberPartiesIds[i]);
                    }

                    if (memberPartiesIds[i] != memberPartiesIds[i + 1]) {
                        toDatesRed.push(toDates[i]);
                    }

                }

                // dates are converted to string and party ids to party names

                let memberPartiesNames = new Array<string>;
                let fromDatesStr = new Array<string>;
                let toDatesStr = new Array<string>;

                this.resolveParties().then(data => {

                    memberPartiesIdsRed.forEach((memberPartyId, i) => {
                        let party = data.find((party: { ID: number; }) => party.ID == memberPartyId);

                        memberPartiesNames.push(party.ActualName);
                        fromDatesStr.push(<string>this.datePipe.transform(fromDatesRed[i], "MM/yyyy"));
                        toDatesStr.push(<string>this.datePipe.transform(toDatesRed[i], "MM/yyyy"));

                    });

                    // if the member is currently in the party, set the toDate to "present"
                    if (inCharge) {
                        toDatesStr[toDatesStr.length - 1] = "present";
                    }

                    this.partiesLoaded = true;  // all the parties data has been handled

                });

                // finally assign all the data to the variables to be used in the template

                this.MemberParties = memberPartiesNames;
                this.MemberFromDates = fromDatesStr;
                this.MemberToDates = toDatesStr;

            });

            this.resolveWebsites().then(data => {

                let websitesData = data.filter((websites: { PersonID: string; }) => websites.PersonID == id);

                let website1URL = websitesData.filter((websitesData: { WebSiteTypeID: number; }) => websitesData.WebSiteTypeID == 1);
                let website2URL = websitesData.filter((websitesData: { WebSiteTypeID: number; }) => websitesData.WebSiteTypeID == 2);
                let website3URL = websitesData.filter((websitesData: { WebSiteTypeID: number; }) => websitesData.WebSiteTypeID == 3);

                // the first 2 urls, if both available, redirect to the same gov. website

                if (website2URL.length > 0) {
                    this.MemberWebsiteGov = website2URL[0].WebURL;
                }
                if (website1URL.length > 0) {
                    this.MemberWebsiteGov = website1URL[0].WebURL;
                }
                if (website3URL.length > 0) {
                    this.MemberWebsitePersonal = website3URL[0].WebURL;
                }

                this.websitesLoaded = true;  // all websites data has been handled

            });

        }

    }

    resolveMember(id: string) {

        /* Wrap getMember() and make use of caching */

        if (this.cache.get('memberData-' + id)) {

            console.log('[INFO] Member data found in cache, using cached data.');

            return Promise.resolve(this.cache.get('memberData-' + id));

        } else {

            console.log('[INFO] Member data not found in cache, fetching data.');

            return new Promise(resolve => {
                this.ParliamentScot.getMember(id)
                    .subscribe(data => {

                        this.cache.set('memberData-' + id, data);
                        resolve(data)

                    });
            });

        }
    }

    resolveMemberParties(id: string) {

        /* Wrap getMemberParties() and make use of caching */

        if (this.cache.get('memberPartiesData-' + id)) {

            console.log('[INFO] Member parties list found in cache, using cached data.');

            return Promise.resolve(this.cache.get('memberPartiesData-' + id));

        } else {

            console.log('[INFO] Member parties list not found in cache, fetching data.');

            return new Promise(resolve => {
                this.ParliamentScot.getMemberParties(id)
                    .subscribe(data => {

                        this.cache.set('memberPartiesData-' + id, data);
                        resolve(data)

                    });
            });

        }
    }

    resolveParties() {

        /* Wrap getParties() and make use of caching */

        if (this.cache.get('partiesData')) {

            console.log('[INFO] Parties list found in cache, using cached data.');

            return Promise.resolve(this.cache.get('partiesData'));

        } else {

            console.log('[INFO] Parties list not found in cache, fetching data.');

            return new Promise(resolve => {
                this.ParliamentScot.getParties()
                    .subscribe(data => {

                        this.cache.set('partiesData', data);
                        resolve(data)

                    });
            });

        }
    }

    resolveWebsites() {

        /* Wrap getWebsites() and make use of caching */

        if (this.cache.get('websitesData')) {

            console.log('[INFO] Websites list found in cache, using cached data.');

            return Promise.resolve(this.cache.get('websitesData'));

        } else {

            console.log('[INFO] Websites list not found in cache, fetching data.');

            return new Promise(resolve => {
                this.ParliamentScot.getWebsites()
                    .subscribe(data => {

                        this.cache.set('websitesData', data);
                        resolve(data)

                    });
            });

        }
    }

}






