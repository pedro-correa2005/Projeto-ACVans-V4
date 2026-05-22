import AutoCompleteInput from "../input/AutoCompleteInput";
import PlacaInput from "../input/PlacaInput";
import { listarVeiculos } from "../../services/veiculoService";
import { useEffect, useState } from "react";
import { listarTiposServico } from "../../services/tiposServicoService";
import { listarStatusServico } from "../../services/statusServicoService"

function ServicoForm({ tipoServico, setTipoServico, receberNotificacao, setReceberNotificacao, statusServico, setStatusServico, veiculoInput, setVeiculoInput, setVeiculoSelecionado, editing = false}) {
    const [tipos, setTipos] = useState([]);
    const [status, setStatus] = useState([]);

    async function carregarTipos() {
        try {
            const data = await listarTiposServico();
            setTipos(data);
        } catch (error) {
            console.error(error);
        }
    }
    async function carregarStatus() {
        try {
            const data = await listarStatusServico();
            setStatus(data);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        carregarTipos();
        carregarStatus();
    }, []);

    return (
        <>
            <div className="mb-3">
                <label className="form-label">Tipo de Serviço</label>
                <select className="form-select" value={tipoServico?.id || ""} onChange={(e) => {
                    const id = Number(e.target.value);
                    const tipoSelecionado = tipos.find(tipo => tipo.id === id);
                    setTipoServico(tipoSelecionado);
                }}
                disabled={editing}>
                <option value="">Selecione</option>
                    {
                        tipos.map(tipo => (
                            <option key={tipo.id} value={tipo.id}>{tipo.descricao}</option>
                        ))
                    }
                </select>
            </div>
            <div className="form-group">
                <label className="form-label d-block">Notificações</label>
                <div className="btn-group" role="group">
                    <input type="radio" className="btn-check" id="btnTrue" checked={receberNotificacao} name="receberNotificacao" value="true" onChange={(e) => (setReceberNotificacao(e.target.checked))}/>
                    <label className="btn btn-outline-primary" htmlFor="btnTrue">Ativadas</label>
                    <input type="radio" className="btn-check" id="btnFalse" checked={!receberNotificacao} name="ativo" value="false" onChange={(e) => (setReceberNotificacao(!e.target.checked))}/>
                    <label className="btn btn-outline-primary" htmlFor="btnFalse">Desativadas</label>
                </div>
            </div>
            <div className="mb-3">
                <label className="form-label">Status de Serviço</label>
                <select className="form-select" value={statusServico?.id || ""} onChange={(e) => {
                    const id = Number(e.target.value);
                    const statusSelecionado = status.find(s => s.id === id);
                    setStatusServico(statusSelecionado);
                }}>
                <option value="">Selecione</option>
                    {
                        status.map(s => (
                            <option key={s.id} value={s.id}>{s.descricao}</option>
                        ))
                    }
                </select>
            </div>
            <div className="mb-3">
                <label className="form-label">Veículo</label>
                <AutoCompleteInput
                    value={veiculoInput}
                    onChange={setVeiculoInput}
                    onSelect={setVeiculoSelecionado}
                    searchFunction={listarVeiculos}
                    displayField="placa"
                    placeholder="Buscar veículo..."
                    disabled={editing}
                />
            </div>
        </>
    )
}

export default ServicoForm;