import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITank, getTankIdentifier } from '../tank.model';

export type EntityResponseType = HttpResponse<ITank>;
export type EntityArrayResponseType = HttpResponse<ITank[]>;

@Injectable({ providedIn: 'root' })
export class TankService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tanks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tank: ITank): Observable<EntityResponseType> {
    return this.http.post<ITank>(this.resourceUrl, tank, { observe: 'response' });
  }

  update(tank: ITank): Observable<EntityResponseType> {
    return this.http.put<ITank>(`${this.resourceUrl}/${getTankIdentifier(tank) as number}`, tank, { observe: 'response' });
  }

  partialUpdate(tank: ITank): Observable<EntityResponseType> {
    return this.http.patch<ITank>(`${this.resourceUrl}/${getTankIdentifier(tank) as number}`, tank, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITank>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITank[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTankToCollectionIfMissing(tankCollection: ITank[], ...tanksToCheck: (ITank | null | undefined)[]): ITank[] {
    const tanks: ITank[] = tanksToCheck.filter(isPresent);
    if (tanks.length > 0) {
      const tankCollectionIdentifiers = tankCollection.map(tankItem => getTankIdentifier(tankItem)!);
      const tanksToAdd = tanks.filter(tankItem => {
        const tankIdentifier = getTankIdentifier(tankItem);
        if (tankIdentifier == null || tankCollectionIdentifiers.includes(tankIdentifier)) {
          return false;
        }
        tankCollectionIdentifiers.push(tankIdentifier);
        return true;
      });
      return [...tanksToAdd, ...tankCollection];
    }
    return tankCollection;
  }
}
