import { useEffect, useState } from "react";
import { listarRoles } from "../../services/usuarioService";

function UsuarioForm({email, setEmail, doisFatores, setDoisFatores, roles, setRoles, editing}){
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
                <input type="email" className="form-control" value={email} onChange={(e) => setEmail(e.target.value)} disabled={editing}/>
            </div>
            <div className="form-group">
                <label className="form-label d-block">Autenticação de dois fatores</label>
                <div className="btn-group" role="group">
                    <input type="radio" className="btn-check" id="btnTrue" checked={doisFatores} name="doisFatores" value="true" onChange={(e) => (setDoisFatores(e.target.checked))} disabled={editing}/>
                    <label className="btn btn-outline-primary" htmlFor="btnTrue">Ativada</label>
                    <input type="radio" className="btn-check" id="btnFalse" checked={!doisFatores} name="ativo" value="false" onChange={(e) => (setDoisFatores(!e.target.checked))} disabled={editing}/>
                    <label className="btn btn-outline-primary" htmlFor="btnFalse">Desativada</label>
                </div>
            </div>
            <div className="form-group">
                <label className="form-label d-block">Permissões de usuário</label>
                <div className="btn-group" role="group">
                    {availableRoles.map((role) =>(
                        <div key={role.id}>
                            <input type="checkbox" className="btn-check" id={`btn-${role.name}`} name="roles" value={role.name} checked={roles.includes(role.name)} onChange={() => handleCheckboxChange(role.name)}/>
                            <label className="btn btn-outline-primary" htmlFor={`btn-${role.name}`}>{role.name}</label>
                        </div>
                    ))}
                </div>
            </div>
        </>
    )
}

export default UsuarioForm;