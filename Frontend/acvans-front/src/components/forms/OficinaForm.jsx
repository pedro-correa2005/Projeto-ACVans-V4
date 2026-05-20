function OficinaForm({nome, setNome, ativo, setAtivo}) {
    return (
        <>
            <div className="mb-3">
                <label className="form-label">Nome</label>
                <input type="text" className="form-control" value={nome} onChange={(e) => setNome(e.target.value)} />
            </div>
            <div className="form-group">
                <label className="form-label d-block">Ativo</label>
                <div className="btn-group" role="group">
                    <input type="radio" className="btn-check" id="btnTrue" checked={ativo} name="ativo" value="true" onChange={(e) => (setAtivo(e.target.checked))} />
                    <label className="btn btn-outline-primary" htmlFor="btnTrue">Sim</label>
                    <input type="radio" className="btn-check" id="btnFalse" checked={!ativo} name="ativo" value="false" onChange={(e) => (setAtivo(!e.target.checked))} />
                    <label className="btn btn-outline-primary" htmlFor="btnFalse">Não</label>
                </div>
            </div>
        </>
    )
}

export default OficinaForm;