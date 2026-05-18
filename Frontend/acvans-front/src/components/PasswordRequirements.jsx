function Requirement({valid, children}){
    return(
        <li className={valid?"text-success":"text-danger"}>
            {valid?"✓ ": "✗ "}{children}
        </li>
    );
}

function PasswordRequirements({validation, policy}){
    return(
        <div className="mt-2">
            <small className="text-muted">Sua senha deve conter:</small>
            <ul className="small mb-0 mt-1 ps-3">
                <Requirement valid={validation.minLength}>
                    Pelo menos {policy.minLength} caracteres
                </Requirement>
                {policy.uppercase && (<Requirement valid={validation.uppercase}>
                    Letra maiúscula
                </Requirement>)}
                {policy.lowercase && (<Requirement valid={validation.lowercase}>
                    Letra minúscula
                </Requirement>)}
                {policy.number && (<Requirement valid={validation.number}>
                    Número
                </Requirement>)}
                {policy.special && (<Requirement valid={validation.special}>
                    Caractere especial
                </Requirement>)}
            </ul>
        </div>
    );
}

export default PasswordRequirements;