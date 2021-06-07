import * as dayjs from 'dayjs';
import { IMileageRequest } from 'app/entities/mileage-request/mileage-request.model';

export interface IMileageResponse {
  id?: number;
  responseDatetime?: dayjs.Dayjs | null;
  tankNumber?: string | null;
  mileageCurrent?: string | null;
  mileageDatetime?: string | null;
  mileageRemain?: string | null;
  mileageUpdateDatetime?: string | null;
  mileageRequest?: IMileageRequest | null;
}

export class MileageResponse implements IMileageResponse {
  constructor(
    public id?: number,
    public responseDatetime?: dayjs.Dayjs | null,
    public tankNumber?: string | null,
    public mileageCurrent?: string | null,
    public mileageDatetime?: string | null,
    public mileageRemain?: string | null,
    public mileageUpdateDatetime?: string | null,
    public mileageRequest?: IMileageRequest | null
  ) {}
}

export function getMileageResponseIdentifier(mileageResponse: IMileageResponse): number | undefined {
  return mileageResponse.id;
}
