import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {EmployeeService} from '../../../services/employee/employee.service';
import {ProjectService} from '../../../services/project/project.service';
import {DataSet, Network} from 'vis-network';
import {EmployeeModel} from '../../../models/employee.model';
import {ProjectModel} from '../../../models/project.model';
import {WorkingOnService} from '../../../services/workingOn/workingOn.service';
import {WorkingOnProjectModel} from '../../../models/workingOnProject.model';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {MessageModalComponent} from '../../../modals/message-modal/message-modal.component';
import {ActivatedRoute, Router} from '@angular/router';
import {NewWorkingOnModalComponent} from "../../../modals/new-working-on-modal/new-working-on-modal.component";

export let networkOptions = {
  autoResize: true,
  interaction: {
    keyboard: false,
    hover: true
  },
  manipulation: {
    enabled: false,
    editEdge: false,
    addNode: false,
    deleteNode: undefined,
    addEdge: undefined,
    deleteEdge: undefined,
  },
  edges: {
    title: 'Hover',
    length: 200,
    width: 0.75
  },
  nodes: {
    physics: false,
    borderWidth: 0,
  },
  physics: {
    maxVelocity: 10,
    repulsion: {
      nodeDistance: 120,
    }
  },
  groups: {
    employeeNode: {
      shape: 'circle',
      widthConstraint: {
        maximum: 60,
        minimum: 60
      },
      heightConstraint: {
        minimum: 60,
        maximum: 60
      }
    },
    projectNode: {
      shape: 'circle',
      margin: 10,
      widthConstraint: {
        maximum: 90,
        minimum: 90
      },
      heightConstraint: {
        minimum: 90,
        maximum: 90
      }
    },
    homeNode: {
      shape: 'box',
      margin: 20
    }
  }
};

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  public employees: EmployeeModel[];
  public projects: ProjectModel[];

  private dateThreshold: Date;
  modalRef: BsModalRef;
  @ViewChild('createWorkingOnModal', {static: false}) createWorkingOnModal: TemplateRef<any>;
  private editMode: boolean;
  public network: Network;
  private container: HTMLElement;
  private layoutSeed: number;
  public isPhysicsEnabled: boolean;
  private networkOptions = networkOptions;
  private showBar: boolean;

  constructor(public employeeService: EmployeeService,
              private projectService: ProjectService,
              private workingOnService: WorkingOnService,
              private modalService: BsModalService,
              private router: Router,
              public activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.isPhysicsEnabled = false;
    this.editMode = false;
    this.layoutSeed = JSON.parse(localStorage.getItem('layoutSeed'));
    this.container = document.getElementById('mynetwork');
    this.network = new Network(this.container, {}, {});
    this.editMode = false;
    this.dateThreshold = new Date();
    this.dateThreshold.setMonth(this.dateThreshold.getMonth() + 2);
    this.employees = [];
    this.projects = [];

    if(this.activatedRoute.snapshot.url[0].toString() === 'map'){
      this.showBar = true;
     this.getMapData();
    }else if (this.activatedRoute.snapshot.url[0].toString() === 'employee'){
      this.showBar = false;
      this.employeeService.getEmployee(+this.activatedRoute.snapshot.url[1].toString()).subscribe(employee => {
        this.employees.push(employee);
        this.createMap();
      });
    }else if(this.activatedRoute.snapshot.url[0].toString() === 'project'){
      this.showBar = false;
      this.projectService.getProject(+this.activatedRoute.snapshot.url[1].toString()).subscribe(project => {
        this.projects.push(project);
        this.createMap();
      });
    }
  }

  getMapData(){
    this.employeeService.getEmployees().subscribe(() => {
      this.employees = this.employeeService.employees;
      this.projectService.getProjects().subscribe(() => {
        this.projects =  this.projectService.projects;
        this.createMap();
      });
    });
  }

  createMap() {
    // Nodes
    let nodeList: any[]= [];
    let edgeList: any[]= [];

    let res = this.getNodeData(nodeList, edgeList);
    nodeList = res.nodeList;
    edgeList = res.edgeList;

    // create a network
    const nodes = new DataSet(nodeList);
    const edges = new DataSet(edgeList);
    document.getElementById('mynetwork').style.height = Math.round(window.innerHeight * 0.94) + 'px';
    // provide the data in the vis format
    const data = {
      nodes,
      edges
    };

    this.networkOptions.manipulation.deleteNode = (nodeData, callback) => this.deleteNode(nodeData, callback);
    this.networkOptions.manipulation.addEdge = (edgeData, callback) => this.addEdge(edgeData, callback);
    this.networkOptions.manipulation.deleteEdge = (edgeData, callback) => this.deleteEdge(edgeData, callback);
    this.networkOptions.nodes.physics = this.isPhysicsEnabled;

    // initialize your network!
    this.network = new Network(this.container, data, this.networkOptions);

    this.initializeCurserOptions(nodes,edges);

    this.getLayout();
  }

  getNodeData(nodeList, edgeList){

    if(!this.employees) this.employees = [];
    if(!this.projects) this.projects = [];

    if(this.employees.length != 0 && this.projects.length != 0 ){
      this.employees.forEach( employee => {
        nodeList.push(
          { id: employee.employeeId,
            label: employee.firstName + ' ' + employee.lastName,
            title: 'Id: ' + employee.employeeId +
              '<br>Email: ' + employee.email +
              '<br> Phone: ' + employee.phone,
            color: employee.isExternal ? '#c9c9c9' : '#65a4f7',
            url: 'http://localhost:4200/employee/' + employee.employeeId,
            group: 'employeeNode'
          });
      });

      this.projects.forEach( project => {
        nodeList.push(
          { id: project.projectId,
            label: project.title,
            title: 'Id: ' + project.projectId +
              '<br>Since: ' + project.since.toDateString() +
              '<br> Until: ' + project.until.toDateString(),
            color: project.isExternal ? '#c9c9c9' : '#ffebad',
            url: 'http://localhost:4200/map/' + project.projectId,
            group: 'projectNode'
          });
      });


      this.employees.forEach( employee => {
        employee.projects.forEach( project => {
          edgeList.push(
            { id: project.workingOnId,
              from: employee.employeeId,
              to: project.project.projectId,
              title: 'Id: ' + project.workingOnId +
                '<br>Since: ' + project.since.toDateString() +
                '<br> Until: ' + project.until.toDateString() +
                '<br> Workload: ' + project.workload + '%',
              color: project.until < this.dateThreshold ? '#bb0300' : '#000000',
              dashes: employee.isExternal
            });
        });
      });
    } else if(this.projects.length === 0 ){
      const employee = this.employees[0];
      if(employee){
        nodeList.push({
          id: employee.employeeId,
          label:employee.firstName,
          color: employee.isExternal ? '#c9c9c9' : '#ffebad',
          group: 'employeeNode'
        });
        employee.projects.forEach( project => {
          nodeList.push(
            { id: project.project.projectId,
              label: project.project.title,
              title: 'Id: ' + project.project.projectId +
                '<br>Since: ' + project.since.toDateString() +
                '<br> Until: ' + project.until.toDateString(),
              color: project.project.isExternal ? '#c9c9c9' : '#65a4f7',
              url: 'http://localhost:4200/employee/' + project.project.projectId,
              group: 'projectNode'

            });
          edgeList.push(
            { id: project.workingOnId,
              from: employee.employeeId,
              to: project.project.projectId,
              title: 'Id: ' + project.workingOnId +
                '<br>Since: ' + project.since.toDateString() +
                '<br> Until: ' + project.until.toDateString() +
                '<br> Workload: ' + project.workload + '%',
              color: project.until < this.dateThreshold ? '#ff0002' : '#000000',
              dashes: project.project.isExternal
            });
        });
      }
    } else if(this.employees.length === 0 ){
      const project = this.projects[0];
      if(project){
        nodeList.push(
          { id: project.projectId,
            label: project.title,
            color: project.isExternal ? '#c9c9c9' : '#ffebad',
            group: 'projectNode'
          });

        project.employees.forEach( employee => {
          nodeList.push(
            { id: employee.employee.employeeId,
              label: employee.employee.firstName + ' ' + employee.employee.lastName,
              title: 'Email: ' + employee.employee.email + '<br> Phone: ' + employee.employee.phone,
              color: employee.employee.isExternal ? '#c9c9c9' : '#65a4f7',
              url: 'http://localhost:4200/employee/' + employee.employee.employeeId,
              group: 'employeeNode'
            });
          edgeList.push(
            { id: employee.workingOnId,
              from: employee.employee.employeeId,
              to: project.projectId,
              title: 'Id: ' + employee.workingOnId +
                '<br>Since: ' + employee.since.toDateString() +
                '<br> Until: ' + employee.until.toDateString() +
                '<br> Workload: ' + employee.workload + '%',
              color: employee.until < this.dateThreshold ? '#ff0002' : '#000000',
              dashes: employee.employee.isExternal
            });
        });
      }
    }

    return {
      nodeList,
      edgeList
    }
  }

  deleteNode(nodeData, callback) {
    const validNodes = [];
    const validEdges = [];
    nodeData.nodes.forEach( node => {
      if (this.isEmployee(node)) {
        validNodes.push(node);
        this.employeeService.deleteEmployee(node).subscribe(() => this.refresh());
      } else if (this.isProject(node)) {
        validNodes.push(node);
        this.projectService.deleteProject(node).subscribe(() => this.refresh());
      }
    });
    nodeData.edges.forEach( edge => {
      if (this.isWorkingOn(edge)) {
        validEdges.push(edge);
        this.workingOnService.deleteWorkingOn(edge).subscribe(() => this.refresh());
      }
    });
    nodeData.nodes = validNodes;
    nodeData.edges = validEdges;
    if (nodeData.nodes.length === 0 && nodeData.edges.length === 0) {
      window.alert('Given nodes can\'t be deleted');
    }
    if (nodeData) {
      callback(nodeData);
    } else {
      callback();
    }
  }

  addEdge(edgeData, callback) {

    if (!this.isEmployee(edgeData.from) && !this.isProject(edgeData.to)) {
      const temp = edgeData.from;
      edgeData.from = edgeData.to;
      edgeData.to = temp;
    }

    if (this.isEmployee(edgeData.from ) && this.isProject(edgeData.to)) {
      let edgeAlreadyExists = false;
      this.network.getConnectedNodes(edgeData.from).forEach(elem => {
        if (edgeData.to === elem) {
         edgeAlreadyExists = true;
        }
      });

      if (!edgeAlreadyExists) {
        const config = {
          backdrop: true,
          ignoreBackdropClick: true,
          initialState: {
            edgeData,
            isNew: true,
          }
        };
        this.modalRef = this.modalService.show(NewWorkingOnModalComponent, config);
        this.modalRef.content.onClose.subscribe(wasSuccessfully => {
          if (wasSuccessfully) {
            let employee: EmployeeModel;
            this.employeeService.getEmployee(edgeData.from).subscribe(res => {
              employee = new EmployeeModel(
                res.employeeId,
                res.firstName,
                res.lastName,
                res.birthday,
                res.email,
                res.phone,
                res.isExternal,
                res.image,
                res.projects);
              let workingOn: WorkingOnProjectModel;
              let i: number;
              for (i = 0; i < employee.projects.length; i++) {
                if (employee.projects[i].project.projectId === edgeData.to) {
                  workingOn = employee.projects[i];
                  break;
                }
              }
              edgeData = {
                id: workingOn.workingOnId,
                from: edgeData.from,
                to: edgeData.to,
                title: 'Id: ' + workingOn.workingOnId +
                  '<br>Since: ' + workingOn.since.toDateString() +
                  '<br> Until: ' + workingOn.until.toDateString() +
                  '<br> Workload: ' + workingOn.workload + '%',
                color: workingOn.until < this.dateThreshold ? '#bb0300' : '#000000',
                dashes: employee.isExternal
              };
              if (edgeData) {
                callback(edgeData);
              } else {
                callback();
              }
            });
          } else {
            const config2 = {
              backdrop: true,
              ignoreBackdropClick: false,
              initialState: {
                message: 'An error occured! Could not create the edge.'
              }
            };
            this.modalService.show(MessageModalComponent, config2);
          }
        });
        } else {
          const config2 = {
            backdrop: true,
            ignoreBackdropClick: false,
            initialState: {
              message: 'The relationship already exists!'
            }
          };
          this.modalService.show(MessageModalComponent, config2);
          return;
        }
      } else {
      const config2 = {
        backdrop: true,
        ignoreBackdropClick: false,
        initialState: {
          message: 'Only edges between employees and projects are supported!'
        }
      };
      this.modalService.show(MessageModalComponent, config2);
    }
  }

  deleteEdge(edgeData, callback) {
    const validEdges = [];
    edgeData.edges.forEach( edge => {
      if (this.isWorkingOn(edge)) {
        validEdges.push(edge);
        this.workingOnService.deleteWorkingOn(edge).subscribe(() => this.refresh());
      }
    });
    edgeData.edges = validEdges;
    if (edgeData.edges.length === 0) {
      window.alert('Given edges can\'t be deleted');
    }
    callback(edgeData);
  }

  isEmployee(id: number): boolean {
    let i;
    if(this.employees){
      for (i = 0; i < this.employees.length; i++) {
        if (this.employees[i].employeeId === id) {
          return true;
        }
      }
    }
    return false;
  }

  isProject(id: number): boolean {
    let i;
    if(this.projects){
      for (i = 0; i < this.projects.length; i++) {
        if (this.projects[i].projectId === id) {
          return true;
        }
      }
    }
    return false;
  }

  isWorkingOn(id: number): boolean {
    let i;
    let j;
    for (i = 0; i < this.projects.length; i++) {
      for (j = 0; j < this.projects[i].employees.length; j++) {
        if (this.projects[i].employees[j].workingOnId === id) {
          return true;
        }
      }
    }
    return false;
  }

  initializeCurserOptions(nodes, edges){
    // Courser Options
    const networkCanvas = document
      .getElementById('mynetwork')
      .getElementsByTagName('canvas')[0];

    function changeCursor(newCursorStyle) {
      networkCanvas.style.cursor = newCursorStyle;
    }
    this.network.on('hoverNode', () => {
      changeCursor('grab');
    });
    this.network.on('blurNode', () => {
      changeCursor('default');
    });
    this.network.on('hoverEdge', () => {
      changeCursor('grab');
    });
    this.network.on('blurEdge', () => {
      changeCursor('default');
    });
    this.network.on('dragStart', () => {
      changeCursor('grabbing');
    });
    this.network.on('dragging', () => {
      changeCursor('grabbing');
    });
    this.network.on('dragEnd', () => {
      changeCursor('grab');
    });
    this.network.on('doubleClick', params => {
      if (params.nodes.length === 1) {
        let node: any;
        node = nodes.get(params.nodes[0]);
        if (node.url != null && node.url !== '' && node.group === 'projectNode') {
          this.router.navigate(['project/' + node.id]);
        } else if (node.group === 'employeeNode') {
          this.router.navigate(['employee/' + node.id]);
        }
      }
    });
    this.network.on('doubleClick', () => {
      this.projects.forEach( project => {
        if (project.isExternal) {
          const node = nodes.get(project.projectId);
          nodes.update({id: node.id, hidden: !node.hidden});
        }
      });
      this.employees.forEach(employee => {
        if (employee.isExternal) {
          const node = nodes.get(employee.employeeId);
          nodes.update({id: node.id, hidden: !node.hidden});
        }
      });
    });
  }

  refresh() {
    this.employees = [];
    this.projects = [];
    this.employeeService.getEmployees().subscribe(() => {
      this.employees = this.employeeService.employees;
      this.projectService.getProjects().subscribe(() => {
        this.projects =  this.projectService.projects;
      });
    });
    this.layoutSeed = JSON.parse(localStorage.getItem('layoutSeed'));
    if (this.layoutSeed) {
      for (const [key, value] of Object.entries(this.layoutSeed)) {
        this.network.moveNode(key, value.x, value.y);
      }
    }
  }

  saveLayout() {
    localStorage.setItem('layoutSeed', JSON.stringify(this.network.getPositions()));
    const config = {
      backdrop: true,
      ignoreBackdropClick: false,
      initialState: {
        message: 'Layout has been saved!'
      }
    };
    this.modalService.show(MessageModalComponent, config);
  }

  getLayout() {
    this.layoutSeed = JSON.parse(localStorage.getItem('layoutSeed'));
    if (this.layoutSeed) {
      for (const [key, value] of Object.entries(this.layoutSeed)) {
        this.network.moveNode(key, value.x, value.y);
      }
    }
  }

  togglePhysics() {
    this.isPhysicsEnabled = !this.isPhysicsEnabled;
    this.network.setOptions({
      nodes: {physics: this.isPhysicsEnabled}
    });
  }

  enableEditMode() {
    this.editMode = true;
    this.network.enableEditMode();
  }

  disableEditMode() {
    this.editMode = false;
    this.network.disableEditMode();
  }

  enableAddEgdeMode() {
  this.network.addEdgeMode();
  }

  onDeleteSelected() {
    this.network.deleteSelected();
  }
}
