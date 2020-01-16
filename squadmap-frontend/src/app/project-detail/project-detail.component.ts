import {Component, OnInit} from '@angular/core';
import {ProjectModel} from '../models/project.model';
import {ActivatedRoute, Router} from '@angular/router';
import {ProjectService} from '../service/project.service';
import {EmployeeService} from '../service/employee.service';
import {EmployeeModel} from '../models/employee.model';
import {WorkingOnService} from '../service/workingOn.service';
import {WorkingOnEmployeeModel} from '../models/workingOnEmployee.model';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {WorkingOnModalComponent} from '../working-on-modal/working-on-modal.component';
import {AddEmployeeModalComponent} from '../add-employee-modal/add-employee-modal.component';

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.css']
})
export class ProjectDetailComponent implements OnInit {

  private project: ProjectModel;
  private filteredEmployees: EmployeeModel[];
  searchText: string;
  isSearching: boolean;
  modalRef: BsModalRef;

  constructor(private route: ActivatedRoute,
              private projectService: ProjectService,
              private employeeService: EmployeeService,
              private workingOnService: WorkingOnService,
              private modalService: BsModalService,
              private router: Router) { }

  ngOnInit() {
    this.isSearching = false;
    this.project = new ProjectModel(0,  '', '', new Date(), new Date(), false, []); // vermeidet exceptions beim Aufbau der view
    this.refreshProject();
    this.searchText = '';
  }

  onOpenAddEmployeeModal() {
    this.filteredEmployees = this.employeeService.getCurrentEmployees();
    const initialState = {
      allEmployees: this.filteredEmployees,
      existingEmployees: this.project.employees,
      projectId: this.project.projectId
    };
    this.modalRef = this.modalService.show(AddEmployeeModalComponent, {initialState});
  }

  onDelete(workingOn: WorkingOnEmployeeModel) {
    this.workingOnService.deleteWorkingOn(workingOn.workingOnId).subscribe(() => {
        this.refreshProject();
    });
  }

  refreshProject() {
    this.projectService.getProject( this.route.snapshot.params.id).subscribe( res => {
      for (const employee of res.employees) {
        employee.since = new Date(employee.since);
        employee.until = new Date(employee.until);
      }
      this.project = new ProjectModel(
        res.projectId, res.title, res.description, res.since, res.until, res.isExternal, res.employees
      );
    });
  }

  onEdit(workingOnEmployee: WorkingOnEmployeeModel) {
    const initialState = {
      workingOnEmployee,
      projectId: this.project.projectId
    };
    this.modalRef = this.modalService.show(WorkingOnModalComponent, {initialState});
  }

  onOpenEmployeeDetail(employeeId: number) {
    this.router.navigate(['/employee/' + employeeId]);
  }
}
