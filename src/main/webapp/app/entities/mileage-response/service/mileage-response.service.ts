import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMileageResponse, getMileageResponseIdentifier } from '../mileage-response.model';

export type EntityResponseType = HttpResponse<IMileageResponse>;
export type EntityArrayResponseType = HttpResponse<IMileageResponse[]>;

@Injectable({ providedIn: 'root' })
export class MileageResponseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mileage-responses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mileageResponse: IMileageResponse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mileageResponse);
    return this.http
      .post<IMileageResponse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mileageResponse: IMileageResponse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mileageResponse);
    return this.http
      .put<IMileageResponse>(`${this.resourceUrl}/${getMileageResponseIdentifier(mileageResponse) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(mileageResponse: IMileageResponse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mileageResponse);
    return this.http
      .patch<IMileageResponse>(`${this.resourceUrl}/${getMileageResponseIdentifier(mileageResponse) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMileageResponse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMileageResponse[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMileageResponseToCollectionIfMissing(
    mileageResponseCollection: IMileageResponse[],
    ...mileageResponsesToCheck: (IMileageResponse | null | undefined)[]
  ): IMileageResponse[] {
    const mileageResponses: IMileageResponse[] = mileageResponsesToCheck.filter(isPresent);
    if (mileageResponses.length > 0) {
      const mileageResponseCollectionIdentifiers = mileageResponseCollection.map(
        mileageResponseItem => getMileageResponseIdentifier(mileageResponseItem)!
      );
      const mileageResponsesToAdd = mileageResponses.filter(mileageResponseItem => {
        const mileageResponseIdentifier = getMileageResponseIdentifier(mileageResponseItem);
        if (mileageResponseIdentifier == null || mileageResponseCollectionIdentifiers.includes(mileageResponseIdentifier)) {
          return false;
        }
        mileageResponseCollectionIdentifiers.push(mileageResponseIdentifier);
        return true;
      });
      return [...mileageResponsesToAdd, ...mileageResponseCollection];
    }
    return mileageResponseCollection;
  }

  protected convertDateFromClient(mileageResponse: IMileageResponse): IMileageResponse {
    return Object.assign({}, mileageResponse, {
      responseDatetime: mileageResponse.responseDatetime?.isValid() ? mileageResponse.responseDatetime.toJSON() : undefined,
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
      res.body.forEach((mileageResponse: IMileageResponse) => {
        mileageResponse.responseDatetime = mileageResponse.responseDatetime ? dayjs(mileageResponse.responseDatetime) : undefined;
      });
    }
    return res;
  }
}
