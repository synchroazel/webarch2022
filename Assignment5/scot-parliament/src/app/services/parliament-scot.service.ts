import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
    providedIn: 'root'
})

export class ParliamentScotService {

    membersUrl = 'https://data.parliament.scot/api/members';
    memberPartiesUrl = 'https://data.parliament.scot/api/memberparties';
    partiesUrl = 'https://data.parliament.scot/api/parties';
    websitesUrl = 'https://data.parliament.scot/api/websites';

    constructor(private http: HttpClient) {
    }

    getMembers(): Observable<any[]> {
        return this.http.get<any[]>(this.membersUrl);
    }

    getMember(id: string): Observable<any> {
        return this.http.get<any>(`${this.membersUrl}/${id}`);
    }

    getMemberParties(id: string): Observable<any[]> {
        return this.http.get<any[]>(this.memberPartiesUrl);
    }

    getParties(): Observable<any[]> {
        return this.http.get<any[]>(this.partiesUrl);
    }

    getWebsites(): Observable<any> {
        return this.http.get<any>(this.websitesUrl);
    }
}
