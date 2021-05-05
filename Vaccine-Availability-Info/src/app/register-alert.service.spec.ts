import { TestBed } from '@angular/core/testing';

import { RegisterAlertService } from './register-alert.service';

describe('RegisterAlertService', () => {
  let service: RegisterAlertService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegisterAlertService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
