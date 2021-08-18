import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMileageRequest, getMileageRequestIdentifier } from '../mileage-request.model';

export type EntityResponseType = HttpResponse<IMileageRequest>;
export type EntityArrayResponseType = HttpResponse<IMileageRequest[]>;

@Injectable({ providedIn: 'root' })
export class MileageRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mileage-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mileageRequest: IMileageRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mileageRequest);
    return this.http
      .post<IMileageRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mileageRequest: IMileageRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mileageRequest);
    return this.http
      .put<IMileageRequest>(`${this.resourceUrl}/${getMileageRequestIdentifier(mileageRequest) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(mileageRequest: IMileageRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mileageRequest);
    return this.http
      .patch<IMileageRequest>(`${this.resourceUrl}/${getMileageRequestIdentifier(mileageRequest) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMileageRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMileageRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMileageRequestToCollectionIfMissing(
    mileageRequestCollection: IMileageRequest[],
    ...mileageRequestsToCheck: (IMileageRequest | null | undefined)[]
  ): IMileageRequest[] {
    const mileageRequests: IMileageRequest[] = mileageRequestsToCheck.filter(isPresent);
    if (mileageRequests.length > 0) {
      const mileageRequestCollectionIdentifiers = mileageRequestCollection.map(
        mileageRequestItem => getMileageRequestIdentifier(mileageRequestItem)!
      );
      const mileageRequestsToAdd = mileageRequests.filter(mileageRequestItem => {
        const mileageRequestIdentifier = getMileageRequestIdentifier(mileageRequestItem);
        if (mileageRequestIdentifier == null || mileageRequestCollectionIdentifiers.includes(mileageRequestIdentifier)) {
          return false;
        }
        mileageRequestCollectionIdentifiers.push(mileageRequestIdentifier);
        return true;
      });
      return [...mileageRequestsToAdd, ...mileageRequestCollection];
    }
    return mileageRequestCollection;
  }

  protected convertDateFromClient(mileageRequest: IMileageRequest): IMileageRequest {
    return Object.assign({}, mileageRequest, {
      requestDatetime: mileageRequest.requestDatetime?.isValid() ? mileageRequest.requestDatetime.toJSON() : undefined,
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
      res.body.forEach((mileageRequest: IMileageRequest) => {
        mileageRequest.requestDatetime = mileageRequest.requestDatetime ? dayjs(mileageRequest.requestDatetime) : undefined;
      });
    }
    return res;
  }
}
