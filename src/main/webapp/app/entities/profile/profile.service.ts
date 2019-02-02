import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProfile } from 'app/shared/model/profile.model';

type EntityResponseType = HttpResponse<IProfile>;
type EntityArrayResponseType = HttpResponse<IProfile[]>;

@Injectable({ providedIn: 'root' })
export class ProfileService {
    public resourceUrl = SERVER_API_URL + 'api/profiles';

    constructor(protected http: HttpClient) {}

    create(profile: IProfile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(profile);
        return this.http
            .post<IProfile>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(profile: IProfile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(profile);
        return this.http
            .put<IProfile>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IProfile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(profile: IProfile): IProfile {
        const copy: IProfile = Object.assign({}, profile, {
            creationDate: profile.creationDate != null && profile.creationDate.isValid() ? profile.creationDate.toJSON() : null,
            birthdate: profile.birthdate != null && profile.birthdate.isValid() ? profile.birthdate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
            res.body.birthdate = res.body.birthdate != null ? moment(res.body.birthdate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((profile: IProfile) => {
                profile.creationDate = profile.creationDate != null ? moment(profile.creationDate) : null;
                profile.birthdate = profile.birthdate != null ? moment(profile.birthdate) : null;
            });
        }
        return res;
    }
}
