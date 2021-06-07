import * as dayjs from 'dayjs';
import { IMileageResponse } from 'app/entities/mileage-response/mileage-response.model';
import { ITank } from 'app/entities/tank/tank.model';

export interface IMileageRequest {
  id?: number;
  requestDatetime?: dayjs.Dayjs | null;
  tankNumbers?: string | null;
  mileageResponse?: IMileageResponse | null;
  tank?: ITank | null;
}

export class MileageRequest implements IMileageRequest {
  constructor(
    public id?: number,
    public requestDatetime?: dayjs.Dayjs | null,
    public tankNumbers?: string | null,
    public mileageResponse?: IMileageResponse | null,
    public tank?: ITank | null
  ) {}
}

export function getMileageRequestIdentifier(mileageRequest: IMileageRequest): number | undefined {
  return mileageRequest.id;
}
