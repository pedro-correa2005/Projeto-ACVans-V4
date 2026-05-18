export function validatePassword(password, policy){
    if (!policy) {
        return {
        minLength: false,
        uppercase: false,
        lowercase: false,
        number: false,
        special: false,
        };
    }
    return {
        minLength: password.length >= policy.minLength,
        uppercase: !policy.uppercase || /[A-Z]/.test(password),
        lowercase: !policy.lowercase || /[a-z]/.test(password),
        number: !policy.number || /\d/.test(password),
        special: !policy.special || /[^A-Za-z0-9]/.test(password)
    }
}