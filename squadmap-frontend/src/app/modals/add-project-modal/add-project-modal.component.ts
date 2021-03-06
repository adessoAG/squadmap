import {Component, OnInit} from '@angular/core';
import {BsModalRef} from 'ngx-bootstrap';
import {WorkingOnService} from '../../services/workingOn/workingOn.service';
import {ProjectModel} from '../../models/project.model';
import {CreateWorkingOnModel} from '../../models/createWorkingOn.model';

@Component({
  selector: 'app-add-project-modal',
  templateUrl: './add-project-modal.component.html',
})
export class AddProjectModalComponent implements OnInit {
  private allProjects: ProjectModel[];
  private employeeId: number;
  private searchText: string;
  private errorMessage: string;
  private errorOccurred: boolean;

  constructor(private modalRef: BsModalRef,
              private workingOnService: WorkingOnService) { }

  ngOnInit() {
    this.errorMessage = '';
  }

  onAddProject(project: ProjectModel, since: Date, until: Date, workload: number) {
    this.workingOnService.createWorkingOn(
      new CreateWorkingOnModel(this.employeeId, project.projectId, since, until, workload)).subscribe(() => {
      this.modalRef.hide();
      location.reload();
    }, error => {
      this.handleError(error.error.message);
    });
  }

  private handleError(message: string) {
    this.errorOccurred = true;
    this.errorMessage = message;
    setTimeout(() => {
      this.errorOccurred = false;
    }, 10000);
  }
}
