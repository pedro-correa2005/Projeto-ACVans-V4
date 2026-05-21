import api from "./api";

export async function listarEtapasServico(idTipo) {
    const response = await api.get(`/tipos-servico/${idTipo}/etapas-servico`);
    return response.data;
}

export async function criarEtapaServico(etapa, idTipo){
    const response = await api.post(`/tipos-servico/${idTipo}/etapas-servico`, etapa);
    return response.data;
}

export async function atualizarEtapaServico(idEtapa, etapa){
    const response = await api.put(`/etapas-servico/${idEtapa}`, etapa);
    return response.data;
}

export async function deletarEtapaServico(idEtapa){
    const response = await api.delete(`/etapas-servico/${idEtapa}`);
    return response.data;
}