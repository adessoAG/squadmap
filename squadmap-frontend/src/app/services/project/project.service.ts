import {Injectable} from '@angular/core';
import {ProjectModel} from '../../models/project.model';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {CreateProjectModel} from '../../models/createProject.model';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  public projects: ProjectModel[] = [];

  constructor(public http: HttpClient) {}

  getProjects() {
    this.projects = [];
    return this.http.get<ProjectModel[]>('http://localhost:8080/project/all').pipe(map(res => {
      Object.values(res).map(receivedData => {
        this.projects.push(new ProjectModel(
          receivedData.projectId,
          receivedData.title,
          receivedData.description,
          new Date(receivedData.since),
          new Date(receivedData.until),
          receivedData.isExternal,
          receivedData.sites,
          receivedData.employees
        ));
      });
    }));
  }

  getProject(id: number) {
    return this.http.get<ProjectModel>('http://localhost:8080/project/' + id).pipe(map(res => {
      res.since = new Date(res.since);
      res.until = new Date(res.until);
      for (const workingOn of res.employees) {
        workingOn.since = new Date(workingOn.since);
        workingOn.until = new Date(workingOn.until);
      }
      return res;
    }));
  }

  deleteProject(projectId: number) {
    return this.http.delete('http://localhost:8080/project/delete/' + projectId);
  }

  updateProject(newProject: CreateProjectModel, projectId: number) {
    return this.http.put('http://localhost:8080/project/update/' + projectId, {
      title: newProject.title,
      description: newProject.description,
      since: newProject.since,
      until: newProject.until,
      isExternal: newProject.isExternal,
      sites: newProject.sites,
    });
  }

  addProject(dummyProject: CreateProjectModel) {
    return this.http.post('http://localhost:8080/project/create', {
      title: dummyProject.title,
      description: dummyProject.description,
      since: dummyProject.since,
      until: dummyProject.until,
      isExternal: dummyProject.isExternal,
      sites: dummyProject.sites,
    });
  }
}