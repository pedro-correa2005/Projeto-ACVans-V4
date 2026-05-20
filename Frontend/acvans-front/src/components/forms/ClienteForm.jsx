import CelularInput from "../input/CelularInput";

function ClienteForm({nome, setNome, celular, setCelular}){
    return(
        <>
            <div className="mb-3">
                <label className="form-label">Nome do cliente</label>
                <input type="text" className="form-control" value={nome} onChange={(e) => setNome(e.target.value)}/>
            </div>
            <div className="mb-3">
                <CelularInput
                    value={nome}
                    onChange={setNome}
                    id={"celular"}
                    label={"Celular do cliente"} placeholder={"(##) 9########"}
                />
            </div>
        </>
    );
}

export default ClienteForm;