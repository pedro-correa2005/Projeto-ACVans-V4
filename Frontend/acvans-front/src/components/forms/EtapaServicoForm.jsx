import { useState } from "react";

function EtapaServicoForm({titulo, setTitulo, descricao, setDescricao}){
    return (
        <>
            <div className="mb-3">
                <label className="form-label">Titulo</label>
                <input type="text" className="form-control" value={titulo} onChange={(e) => setTitulo(e.target.value)}/>
            </div>
            <div className="mb-3">
                <label className="form-label">Descrição</label>
                <input type="text" className="form-control" value={descricao} onChange={(e) => setDescricao(e.target.value)}/>
            </div>
        </>
    )
}

export default EtapaServicoForm;