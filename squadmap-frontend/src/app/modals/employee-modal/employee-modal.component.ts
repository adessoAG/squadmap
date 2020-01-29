import {Component, OnInit} from '@angular/core';
import {BsModalRef} from 'ngx-bootstrap';
import {NgForm} from '@angular/forms';
import {CreateEmployeeModel} from '../../models/createEmployee.model';
import {EmployeeService} from '../../services/employee/employee.service';
import {EmployeeModel} from '../../models/employee.model';

@Component({
  selector: 'app-employee-modal',
  templateUrl: './employee-modal.component.html',
  styleUrls: ['./employee-modal.component.css']
})
export class EmployeeModalComponent implements OnInit {
  actionName: string;
  employee: EmployeeModel;
  isNew: boolean;
  private errorMessage: string;
  private errorOccurred: boolean;


  constructor(private modalRef: BsModalRef,
              private employeeService: EmployeeService) { }

  ngOnInit() {
    this.errorMessage = '';
    if (!this.employee) {
      this.employee = new EmployeeModel(0, '', '', new Date(), '',  '', false, '', []);
      this.isNew = true;
    } else {
      this.isNew = false;
    }
  }

  onCreateEmployee(employeeForm: NgForm) {
    const employee = new CreateEmployeeModel(
      employeeForm.value.firstName,
      employeeForm.value.lastName,
      employeeForm.value.birthday,
      employeeForm.value.email,
      employeeForm.value.phone,
      employeeForm.value.isExternal,
      employeeForm.value.image
    );
    if (this.isNew) {
      this.employeeService.addEmployee(employee).subscribe(() => {
        this.closeModal();
      }, error => {
        this.handleError(error.error.message);
      });
    } else {
      this.employeeService.updateEmployee(employee, this.employee.employeeId).subscribe(() => {
        this.closeModal();
      }, error => {
        this.handleError(error.error.message);
      });
    }
  }

  private closeModal() {
      this.modalRef.hide();
      location.reload();
  }

  private handleError(message: string) {
    this.errorOccurred = true;
    this.errorMessage = message;
    setTimeout(() => {
      this.errorOccurred = false;
    }, 10000);
  }
}