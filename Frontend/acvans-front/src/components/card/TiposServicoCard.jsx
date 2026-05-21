import { FaEdit, FaTrash, FaPlus } from "react-icons/fa";
import { DndContext, closestCenter } from "@dnd-kit/core";
import { SortableContext, verticalListSortingStrategy, arrayMove } from "@dnd-kit/sortable";
import SortableEtapaItem from "../sortable/SortableEtapaItem";

function TiposServicoCard({ tipos, editarTipo, deletarTipo, adicionarEtapa, editarEtapa, deletarEtapa, moverEtapa }) {
    async function handleDragEnd(event, tipo) {
        const { active, over } = event;
        if (!over) {
            return;
        }
        if (active.id === over.id) {
            return;
        }
        const etapas = tipo.etapas;
        const oldIndex = etapas.findIndex(e => e.id === active.id);
        const newIndex = etapas.findIndex(e => e.id === over.id);
        const reordered = arrayMove(etapas, oldIndex, newIndex);
        const etapaMovida = reordered[newIndex];
        const etapaAnterior = reordered[newIndex - 1];
        const etapaProxima = reordered[newIndex + 1];
        await moverEtapa(

            tipo.id,

            oldIndex,

            newIndex,

            etapaMovida.id,

            etapaAnterior
                ? etapaAnterior.ordem
                : null,

            etapaProxima
                ? etapaProxima.ordem
                : null
        );
    }
    return (
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
                                    <button className="btn btn-outline-secondary" onClick={() => editarTipo(tipo)}><FaEdit /></button>
                                    <button className="btn btn-outline-danger" onClick={() => deletarTipo(tipo)}><FaTrash /></button>
                                </div>
                            </div>
                            <DndContext
                                collisionDetection={closestCenter}
                                onDragEnd={(event) => handleDragEnd(event, tipo)}
                            >
                                <SortableContext
                                    items={tipo?.etapas?.map(etapa => etapa.id)}
                                    strategy={verticalListSortingStrategy}
                                >
                                    <ul className="list-group list-group-flush mt-2">
                                        {
                                            tipo?.etapas?.map((etapa) => (
                                                <SortableEtapaItem
                                                    key={etapa.id}
                                                    etapa={etapa}
                                                    editarEtapa={editarEtapa}
                                                    deletarEtapa={deletarEtapa}
                                                />
                                                /*<li key={etapa.id} className="list-group-item d-flex justify-content-between align-items-center" data-ordem={etapa.ordem}>
                                                    <div>
                                                        <strong>{etapa.titulo}</strong>: {etapa.descricao}
                                                    </div>
                                                    <div className="btn-group btn-group-sm">
                                                        <button className="btn btn-outline-secondary" onClick={() => editarEtapa(etapa)}><FaEdit /></button>
                                                        <button className="btn btn-outline-danger" onClick={() => deletarEtapa(etapa)}><FaTrash /></button>
                                                    </div>
                                                </li>*/
                                            ))
                                        }
                                    </ul>
                                </SortableContext>
                            </DndContext>
                            <button className="btn btn-sm btn-outline-primary mt-2 w-100" onClick={() => adicionarEtapa(tipo)}><FaPlus></FaPlus> Adicionar Etapa</button>
                        </div>
                    </div>
                ))
            }
        </>
    );
}

export default TiposServicoCard;