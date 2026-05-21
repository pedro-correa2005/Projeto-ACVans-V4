import { FaEdit, FaTrash, FaPlus } from "react-icons/fa";

function TiposServicoCard({tipos, editarTipo, deletarTipo, adicionarEtapa, editarEtapa, deletarEtapa}){
    return(
        <>
        {
            tipos.map(tipo => (
                <div key={tipo.id} className="card mb-3">
                    <div className="card-body">
                        <div className="d-flex justify-content-between align-items-center">
                            <h5 className="mb-0">
                                {tipo.descricao}
                            </h5>
                            <div>
                                <button className="btn btn-outline-secondary" onClick={() => editarTipo(tipo)}><FaEdit/></button>
                                <button className="btn btn-outline-danger" onClick={() => deletarTipo(tipo)}><FaTrash/></button>
                            </div>
                        </div>
                        <ul className="list-group list-group-flush mt-2">
                            {
                                tipo?.etapas?.map((etapa) => (
                                    <li key={etapa.id} className="list-group-item d-flex justify-content-between align-items-center" data-ordem={etapa.ordem}>
                                        <div>
                                            <strong>{etapa.titulo}</strong>: {etapa.descricao}
                                        </div>
                                        <div className="btn-group btn-group-sm">
                                            <button className="btn btn-outline-secondary" onClick={() => editarEtapa(etapa)}><FaEdit/></button>
                                            <button className="btn btn-outline-danger" onClick={() => deletarEtapa(etapa)}><FaTrash/></button>
                                        </div>
                                    </li>
                                ))
                            }
                        </ul>
                        <button className="btn btn-sm btn-outline-primary mt-2 w-100" onClick={() => adicionarEtapa(tipo)}><FaPlus></FaPlus> Adicionar Etapa</button>
                    </div>
                </div>
            ))
        }
        </>
    );
}

export default TiposServicoCard;