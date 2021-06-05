import * as dayjs from 'dayjs';
import { ILocationResponse } from 'app/entities/location-response/location-response.model';
import { ITank } from 'app/entities/tank/tank.model';

export interface ILocationRequest {
  id?: number;
  requestDatetime?: dayjs.Dayjs | null;
  tankNumbers?: string | null;
  tankNumber?: ILocationResponse | null;
  tank?: ITank | null;
}

export class LocationRequest implements ILocationRequest {
  constructor(
    public id?: number,
    public requestDatetime?: dayjs.Dayjs | null,
    public tankNumbers?: string | null,
    public tankNumber?: ILocationResponse | null,
    public tank?: ITank | null
  ) {}
}

export function getLocationRequestIdentifier(locationRequest: ILocationRequest): number | undefined {
  return locationRequest.id;
}
