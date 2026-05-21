import AutoCompleteInput from "../input/AutoCompleteInput";
import PlacaInput from "../input/PlacaInput";
import { listarClientes } from "../../services/clienteService";
import { useState } from "react";

function VeiculoForm({placa, setPlaca, marca, setMarca, modelo, setModelo, clienteInput, setClienteInput, clienteSelecionado, setClienteSelecionado, editing}){
    const MARCAS = ["Mercedes-Benz", "Fiat", "Renault", "Ford", "Citroën", "Peugeot"];
    const [marcaSelect, setMarcaSelect] = useState("");
    return(
        <>
            <div className="mb-3">
                <PlacaInput
                    value={placa}
                    onChange={setPlaca}
                    id={"placa"}
                    label={"Placa"}
                />
            </div>
            <div className="mb-3">
                <label className="form-label">Marca</label>
                <select className="form-select" value={marcaSelect}
                    onChange={(e) => {
                        const value = e.target.value;
                        setMarcaSelect(value);
                        if(value === "Outra"){
                            setMarca("");
                        }else{
                            setMarca(value);
                        }
                    }}>
                    <option value="">Selecione</option>
                    {
                        MARCAS.map(marcaOption => (
                            <option key={marcaOption} value={marcaOption}>{marcaOption}</option>
                        ))
                    }
                    <option value="Outra" style={{fontWeight: "bold"},{color: "blue"}}>+ OUTRA (DIGITAR)</option>
                </select>
                {
                    marcaSelect === "Outra" && (
                        <input type="text" class="form-control" placeholder="Digite o nome da marca" value={marca} onChange={(e) => setMarca(e.target.value)}></input>
                    )
                }
            </div>
            <div className="mb-3">
                <label className="form-label">Modelo</label>
                <input type="text" className="form-control" value={modelo} onChange={(e) => setModelo(e.target.value)} />
            </div>
            <div className="mb-3">
                <label className="form-label">Cliente</label>
                <AutoCompleteInput
                    value={clienteInput}
                    onChange={setClienteInput}
                    onSelect={setClienteSelecionado}
                    searchFunction={listarClientes}
                    displayField="nome"
                    placeholder="Buscar cliente..."
                    disabled={editing}
                />
            </div>
        </>
    )
}

export default VeiculoForm;