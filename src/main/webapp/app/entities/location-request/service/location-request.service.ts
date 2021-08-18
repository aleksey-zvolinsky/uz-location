import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocationRequest, getLocationRequestIdentifier } from '../location-request.model';

export type EntityResponseType = HttpResponse<ILocationRequest>;
export type EntityArrayResponseType = HttpResponse<ILocationRequest[]>;

@Injectable({ providedIn: 'root' })
export class LocationRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/location-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(locationRequest: ILocationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locationRequest);
    return this.http
      .post<ILocationRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(locationRequest: ILocationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locationRequest);
    return this.http
      .put<ILocationRequest>(`${this.resourceUrl}/${getLocationRequestIdentifier(locationRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(locationRequest: ILocationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locationRequest);
    return this.http
      .patch<ILocationRequest>(`${this.resourceUrl}/${getLocationRequestIdentifier(locationRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILocationRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILocationRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocationRequestToCollectionIfMissing(
    locationRequestCollection: ILocationRequest[],
    ...locationRequestsToCheck: (ILocationRequest | null | undefined)[]
  ): ILocationRequest[] {
    const locationRequests: ILocationRequest[] = locationRequestsToCheck.filter(isPresent);
    if (locationRequests.length > 0) {
      const locationRequestCollectionIdentifiers = locationRequestCollection.map(
        locationRequestItem => getLocationRequestIdentifier(locationRequestItem)!
      );
      const locationRequestsToAdd = locationRequests.filter(locationRequestItem => {
        const locationRequestIdentifier = getLocationRequestIdentifier(locationRequestItem);
        if (locationRequestIdentifier == null || locationRequestCollectionIdentifiers.includes(locationRequestIdentifier)) {
          return false;
        }
        locationRequestCollectionIdentifiers.push(locationRequestIdentifier);
        return true;
      });
      return [...locationRequestsToAdd, ...locationRequestCollection];
    }
    return locationRequestCollection;
  }

  protected convertDateFromClient(locationRequest: ILocationRequest): ILocationRequest {
    return Object.assign({}, locationRequest, {
      requestDatetime: locationRequest.requestDatetime?.isValid() ? locationRequest.requestDatetime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.requestDatetime = res.body.requestDatetime ? dayjs(res.body.requestDatetime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((locationRequest: ILocationRequest) => {
        locationRequest.requestDatetime = locationRequest.requestDatetime ? dayjs(locationRequest.requestDatetime) : undefined;
      });
    }
    return res;
  }
}
