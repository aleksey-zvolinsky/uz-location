import * as dayjs from 'dayjs';
import { ILocationRequest } from 'app/entities/location-request/location-request.model';

export interface ILocationResponse {
  id?: number;
  responseDatetime?: dayjs.Dayjs | null;
  tankNumber?: string | null;
  tankType?: string | null;
  cargoId?: string | null;
  cargoName?: string | null;
  weight?: string | null;
  receiverId?: string | null;
  tankIndex?: string | null;
  locationStationId?: string | null;
  locationStationName?: string | null;
  locationDatetime?: string | null;
  locationOperation?: string | null;
  stateFromStationId?: string | null;
  stateFromStationName?: string | null;
  stateToStationId?: string | null;
  stateToStationName?: string | null;
  stateSendDatetime?: string | null;
  stateSenderId?: string | null;
  planedServiceDatetime?: string | null;
  tankOwner?: string | null;
  tankModel?: string | null;
  defectRegion?: string | null;
  defectStation?: string | null;
  defectDatetime?: string | null;
  defectDetails?: string | null;
  repairRegion?: string | null;
  repairStation?: string | null;
  repairDatetime?: string | null;
  updateDatetime?: string | null;
  locationRequest?: ILocationRequest | null;
}

export class LocationResponse implements ILocationResponse {
  constructor(
    public id?: number,
    public responseDatetime?: dayjs.Dayjs | null,
    public tankNumber?: string | null,
    public tankType?: string | null,
    public cargoId?: string | null,
    public cargoName?: string | null,
    public weight?: string | null,
    public receiverId?: string | null,
    public tankIndex?: string | null,
    public locationStationId?: string | null,
    public locationStationName?: string | null,
    public locationDatetime?: string | null,
    public locationOperation?: string | null,
    public stateFromStationId?: string | null,
    public stateFromStationName?: string | null,
    public stateToStationId?: string | null,
    public stateToStationName?: string | null,
    public stateSendDatetime?: string | null,
    public stateSenderId?: string | null,
    public planedServiceDatetime?: string | null,
    public tankOwner?: string | null,
    public tankModel?: string | null,
    public defectRegion?: string | null,
    public defectStation?: string | null,
    public defectDatetime?: string | null,
    public defectDetails?: string | null,
    public repairRegion?: string | null,
    public repairStation?: string | null,
    public repairDatetime?: string | null,
    public updateDatetime?: string | null,
    public locationRequest?: ILocationRequest | null
  ) {}
}

export function getLocationResponseIdentifier(locationResponse: ILocationResponse): number | undefined {
  return locationResponse.id;
}
