package com.fooengineers.projetoAcVansV4.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordPolicy {
	private int minLength;
	private boolean requireUppercase;
	private boolean requireLowercase;
	private boolean requireNumber;
	private boolean requireSpecial;	
}
