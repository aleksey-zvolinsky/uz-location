import { ILocationRequest } from 'app/entities/location-request/location-request.model';
import { IMileageRequest } from 'app/entities/mileage-request/mileage-request.model';

export interface ITank {
  id?: number;
  tankNumber?: string;
  ownerName?: string;
  clientName?: string;
  tankNumbers?: ILocationRequest[] | null;
  tankNumbers?: IMileageRequest[] | null;
}

export class Tank implements ITank {
  constructor(
    public id?: number,
    public tankNumber?: string,
    public ownerName?: string,
    public clientName?: string,
    public tankNumbers?: ILocationRequest[] | null,
    public tankNumbers?: IMileageRequest[] | null
  ) {}
}

export function getTankIdentifier(tank: ITank): number | undefined {
  return tank.id;
}
