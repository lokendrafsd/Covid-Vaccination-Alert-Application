import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegisterAlertService {

  private cowinUrl = 'https://cdn-api.co-vin.in/api/v2/admin/location';

  constructor(private http: HttpClient) { }

  getDistricts(id: any): Observable<any> {
    return this.http.get(`${this.cowinUrl}/districts/${id}`);
  }

  getStates(): Observable<any> {
    return this.http.get(`${this.cowinUrl}/states`);
  }

  registerAlert(request: Object): Observable<Object> {
    const headers = new HttpHeaders()
            .set("Content-Type", "application/json");
    return this.http.post('/api/register-alert', request, {headers});
  }

  deregisterAlert(email: String): Observable<any> {
    return this.http.delete('/api/deregister-alert/'+email);
  }
}
