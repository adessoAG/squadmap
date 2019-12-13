import {WorkingOnModel} from './workingOn.model';

export class ProjectModel {
  constructor(
    public projectId: number,
    public title: string,
    public description: string,
    public since: Date,
    public until: Date,
    public isExternal: boolean,
    public employees: WorkingOnModel[]
  ) {
  }
}