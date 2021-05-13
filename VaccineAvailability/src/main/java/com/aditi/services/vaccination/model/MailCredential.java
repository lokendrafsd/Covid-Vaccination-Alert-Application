package com.aditi.services.vaccination.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailCredential {

	private String host;
	private int port;
	private String email;
	private String password;

}
