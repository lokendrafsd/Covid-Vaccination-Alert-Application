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

  statesData : any;
  districtData : any;
  selectedDistrict:any;
  registerAlertsRequest: RegisterAlertRequest = new RegisterAlertRequest();

  constructor(private service:RegisterAlertService) { }

  ngOnInit(): void {
    this.submitted = false;
    this.myForm = new FormGroup({
      name: new FormControl('',Validators.required),
      emailId: new FormControl('', [Validators.required, Validators.email]),
      filterAge: new FormControl('', Validators.required),
      districtId: new FormControl('', Validators.required),
      phone: new FormControl(''),
      state : new FormControl('', Validators.required)
    });

    this.statesData
    this.service.getStates()
      .subscribe(data => {
        this.statesData = data.states;
      }, error => console.log(error));
  }

  loadDistricts(){
    if(this.registerAlertsRequest.state){
      console.log("State Selected: ", this.registerAlertsRequest.state);
    }
    this.service.getDistricts(this.registerAlertsRequest.state)
      .subscribe(data => {
        console.log("District Data:" + data.states)
        this.districtData = data.districts;
      }, error => console.log(error));
  }

  save(form:any) {
    this.service
    .registerAlert(JSON.stringify(form)).subscribe(data => {
      console.log("Submited Data: ", data)
      this.registerAlertsRequest = new RegisterAlertRequest();
      this.ngOnInit();
    }, 
    error => console.log(error));
  }

  // onSubmit() {
  //   console.log("Submitted Data: ", JSON.stringify(this.registerAlertsRequest));
    
  //   this.save();    
  // }

  onSubmit(form: FormGroup) {
    console.log("Form Submitted");
    this.submitted = true;
    if(form.valid){
      console.log("Form is valid");
      console.log("Form data",form.value);
      this.save(form.value);
    }
  }
  

}
