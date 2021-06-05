import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocationResponse, getLocationResponseIdentifier } from '../location-response.model';

export type EntityResponseType = HttpResponse<ILocationResponse>;
export type EntityArrayResponseType = HttpResponse<ILocationResponse[]>;

@Injectable({ providedIn: 'root' })
export class LocationResponseService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/location-responses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(locationResponse: ILocationResponse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locationResponse);
    return this.http
      .post<ILocationResponse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(locationResponse: ILocationResponse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locationResponse);
    return this.http
      .put<ILocationResponse>(`${this.resourceUrl}/${getLocationResponseIdentifier(locationResponse) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(locationResponse: ILocationResponse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(locationResponse);
    return this.http
      .patch<ILocationResponse>(`${this.resourceUrl}/${getLocationResponseIdentifier(locationResponse) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILocationResponse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILocationResponse[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocationResponseToCollectionIfMissing(
    locationResponseCollection: ILocationResponse[],
    ...locationResponsesToCheck: (ILocationResponse | null | undefined)[]
  ): ILocationResponse[] {
    const locationResponses: ILocationResponse[] = locationResponsesToCheck.filter(isPresent);
    if (locationResponses.length > 0) {
      const locationResponseCollectionIdentifiers = locationResponseCollection.map(
        locationResponseItem => getLocationResponseIdentifier(locationResponseItem)!
      );
      const locationResponsesToAdd = locationResponses.filter(locationResponseItem => {
        const locationResponseIdentifier = getLocationResponseIdentifier(locationResponseItem);
        if (locationResponseIdentifier == null || locationResponseCollectionIdentifiers.includes(locationResponseIdentifier)) {
          return false;
        }
        locationResponseCollectionIdentifiers.push(locationResponseIdentifier);
        return true;
      });
      return [...locationResponsesToAdd, ...locationResponseCollection];
    }
    return locationResponseCollection;
  }

  protected convertDateFromClient(locationResponse: ILocationResponse): ILocationResponse {
    return Object.assign({}, locationResponse, {
      responseDatetime: locationResponse.responseDatetime?.isValid() ? locationResponse.responseDatetime.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.responseDatetime = res.body.responseDatetime ? dayjs(res.body.responseDatetime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((locationResponse: ILocationResponse) => {
        locationResponse.responseDatetime = locationResponse.responseDatetime ? dayjs(locationResponse.responseDatetime) : undefined;
      });
    }
    return res;
  }
}
