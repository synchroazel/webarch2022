import {Injectable} from '@angular/core';

@Injectable()
export class CacheService {
    private static PREFIX = 'scot-parliament-';

    static get(key: string) {
        const value = localStorage.getItem(this.PREFIX + key);
        if (value) {
            return JSON.parse(value);
        }
    }

    static set(id: string, value: any) {
        localStorage.setItem(this.PREFIX + id, JSON.stringify(value));
    }
}
