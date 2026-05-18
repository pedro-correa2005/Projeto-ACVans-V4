package com.fooengineers.projetoAcVansV4.dto;

import com.fooengineers.projetoAcVansV4.domain.PasswordPolicy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyDTO {
	private int minLength;
	private boolean uppercase;
	private boolean lowercase;
	private boolean number;
	private boolean special;
	
	//Constructor a partir de entity
	public PolicyDTO(PasswordPolicy policy) {
		this.minLength = policy.getMinLength();
		this.uppercase = policy.isRequireUppercase();
		this.lowercase = policy.isRequireLowercase();
		this.number = policy.isRequireNumber();
		this.special = policy.isRequireSpecial();
	}
}
