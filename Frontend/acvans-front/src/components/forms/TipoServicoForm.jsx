import { useState } from "react";

function TipoServicoForm({descricao, setDescricao}){
    return(
        <>
            <div className="mb-3">
                <label className="form-label">Descrição</label>
                <input type="text" className="form-control" value={descricao} onChange={(e) => setDescricao(e.target.value)}/>
            </div>
        </>
    )
}

export default TipoServicoForm;