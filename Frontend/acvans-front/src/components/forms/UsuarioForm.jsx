import { useEffect, useState } from "react";
import { listarRoles } from "../../services/usuarioService";

function UsuarioForm({email, setEmail, doisFatores, setDoisFatores, roles, setRoles}){
    const [
        availableRoles,
        setAvailableRoles
    ] = useState([]);

    const [
        loading,
        setLoading
    ] = useState(false);

    async function carregarRoles(){
        try{
            const data = await listarRoles();
            setAvailableRoles(data);
        }catch(error){
            console.error(error);
        }
    }

    useEffect(() => {
        carregarRoles();
    }, [])

    const handleCheckboxChange = (roleName) => {
        if(roles.includes(roleName)){
            setRoles(roles.filter((role) => role !== roleName));
            return;
        }
        setRoles([...roles, roleName]);
    };

    if(loading) return <span>Carregando permissões...</span>;

    return(
        <>
            <div className="mb-3">
                <label className="form-label">Email:</label>
                <input type="email" className="form-control" value={email} onChange={(e) => setEmail(e.target.value)}/>
            </div>
            <div className="form-group">
                <label className="form-label d-block">Autenticação de dois fatores</label>
                <div className="btn-group" role="group">
                    <input type="radio" className="btn-check" id="btnTrue" checked={doisFatores} name="doisFatores" value="true" onChange={(e) => (setDoisFatores(e.target.checked))} />
                    <label className="btn btn-outline-primary" htmlFor="btnTrue">Ativada</label>
                    <input type="radio" className="btn-check" id="btnFalse" checked={!doisFatores} name="ativo" value="false" onChange={(e) => (setDoisFatores(!e.target.checked))} />
                    <label className="btn btn-outline-primary" htmlFor="btnFalse">Desativada</label>
                </div>
            </div>
            <div className="form-group">
                <label className="form-label d-block">Permissões de usuário</label>
                <div class="btn-group" role="group">
                    {availableRoles.map((role) =>(
                        <div key={role.id}>
                            <input type="checkbox" class="btn-check" id={`btn-${role.nome}`} name="roles" value={role.nome} checked={roles.includes(role.nome)}
                            onChange={() => handleCheckboxChange(role.nome)}/>
                            <label class="btn btn-outline-primary" htmlFor={`btn-${role.nome}`}>{role.nome.charAt(0) + role.nome.slice(1).toLowerCase()}</label>
                        </div>
                    ))}
                </div>
            </div>
        </>
    )
}

export default UsuarioForm;