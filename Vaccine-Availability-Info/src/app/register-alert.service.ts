import { Injectable, Inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { getBaseUrl } from './app.module';

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

  registerAlert(request: Object, @Inject(BASE_URL) baseUrl: string): Observable<Object> {
    const headers = new HttpHeaders()
            .set("Content-Type", "application/json");
    return this.http.post(baseUrl+'vaccination/register-alert', request, {headers});
  }

  deregisterAlert(email: String, @Inject(BASE_URL) baseUrl: string): Observable<any> {
    return this.http.delete(baseUrl+'/vaccination/deregister-alert/${email}');
  }


}
