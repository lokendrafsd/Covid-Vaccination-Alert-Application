import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RegisterAlertService } from '../register-alert.service';
import { RegisterAlertRequest } from '../RegisterAlertRequest';

@Component({
  selector: 'app-register-alert',
  templateUrl: './register-alert.component.html',
  styleUrls: ['./register-alert.component.css']
})
export class RegisterAlertComponent implements OnInit {
  myForm!: FormGroup;
  submitted = false;
  isRegistrationActive: boolean = true;

  myDForm!: FormGroup;
  Deregistersubmitted = false;


  statesData: any;
  districtData: any;
  selectedDistrict: any;
  registerAlertsRequest: RegisterAlertRequest = new RegisterAlertRequest();

  constructor(private service: RegisterAlertService) { }

  ngOnInit(): void {
    this.submitted = false;
    this.isRegistrationActive = true;
    this.myForm = new FormGroup({
      name: new FormControl('', Validators.required),
      emailId: new FormControl('', [Validators.required, Validators.email]),
      filterAge: new FormControl('', Validators.required),
      districtId: new FormControl('', Validators.required),
      phone: new FormControl(''),
      state: new FormControl('', Validators.required)
    });

    this.statesData
    this.service.getStates()
      .subscribe(data => {
        this.statesData = data.states;
      }, error => console.log(error));

    this.Deregistersubmitted = false;
    this.myDForm = new FormGroup({
      emailId: new FormControl('', [Validators.required, Validators.email]),
    });
  }

  loadDistricts() {
    this.service.getDistricts(this.registerAlertsRequest.state)
      .subscribe(data => {
        this.districtData = data.districts;
      }, error => console.log(error));
  }

  save(form: any) {
    this.service
      .registerAlert(JSON.stringify(form)).subscribe(data => {
        this.registerAlertsRequest = new RegisterAlertRequest();
        this.ngOnInit();
      },
        error => console.log(error));
  }


  onSubmit(form: FormGroup) {
    this.submitted = true;
    if (form.valid) {
      this.save(form.value);
    }
  }

   
  deRegister(email:any) {
    this.service
    .deregisterAlert(email).subscribe(data => {
      console.log("De Registration Request Submitted")
    }, 
    error => console.log(error));
    this.isRegistrationActive = true;
  }

  onSubmitDeRegister(form: FormGroup) {
    this.Deregistersubmitted = true;
    if(form.valid){
      this.deRegister(form.controls.emailId.value);
    }
  }

  deRegisterOption(flag:any){
    this.isRegistrationActive = flag;
  }


}
