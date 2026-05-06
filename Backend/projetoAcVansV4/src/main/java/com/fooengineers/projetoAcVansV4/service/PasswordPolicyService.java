package com.fooengineers.projetoAcVansV4.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.domain.PasswordPolicy;
import com.fooengineers.projetoAcVansV4.entity.Role;

@Service
public class PasswordPolicyService {
	private static final Map<String, PasswordPolicy> ROLE_POLICIES = Map.of(
		    "ADMIN", new PasswordPolicy(12, true, true, true, true),
		    "GERENTE", new PasswordPolicy(8, true, true, true, false),
		    "FUNCIONARIO", new PasswordPolicy(8, false, false, true, false)
		);
	
	public PasswordPolicy getPolicyByRole(Role role) {
		PasswordPolicy policy = ROLE_POLICIES.get(role.getNome());
		return policy;
	}
	
	private PasswordPolicy mergePolicies(PasswordPolicy p1, PasswordPolicy p2) {
	    PasswordPolicy result = new PasswordPolicy();

	    result.setMinLength(Math.max(p1.getMinLength(), p2.getMinLength()));
	    result.setRequireUppercase(p1.isRequireUppercase() || p2.isRequireUppercase());
	    result.setRequireLowercase(p1.isRequireLowercase() || p2.isRequireLowercase());
	    result.setRequireNumber(p1.isRequireNumber() || p2.isRequireNumber());
	    result.setRequireSpecial(p1.isRequireSpecial() || p2.isRequireSpecial());

	    return result;
	}
	
	public PasswordPolicy defaultPolicy() {
		return ROLE_POLICIES.get("ADMIN");
	}
	
	public PasswordPolicy getPolicy(Set<Role> roles) {
		return roles.stream()
				.map(this::getPolicyByRole)
				.reduce(this::mergePolicies)
				.orElse(defaultPolicy());
	}
	
	public List<String> validate(String password, Set<Role> roles) {
		PasswordPolicy policy = getPolicy(roles);
		
		List<String> errors = new ArrayList<>();

        if (password.length() < policy.getMinLength()) {
        	
            errors.add("Senha muito curta. Deve ter pelo menos " + policy.getMinLength() + " caracteres.");
        }

        if (policy.isRequireUppercase() && !password.matches(".*[A-Z].*")) {
            errors.add("Deve conter letra maiúscula");
        }

        if (policy.isRequireLowercase() && !password.matches(".*[a-z].*")) {
            errors.add("Deve conter letra minúscula");
        }

        if (policy.isRequireNumber() && !password.matches(".*\\d.*")) {
            errors.add("Deve conter número");
        }

        if (policy.isRequireSpecial() && !password.matches(".*[^a-zA-Z0-9].*")) {
            errors.add("Deve conter caractere especial");
        }
        return errors;
	}
}
