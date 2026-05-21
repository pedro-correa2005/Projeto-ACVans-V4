import api from "./api";

export async function listarTiposServico(){
    const response = await api.get(`/tipos-servico`);
    return response.data;
}

export async function criarTipoServico(tipoServico){
    const response = await api.post(`/tipos-servico`, tipoServico);
    return response.data;
}

export async function atualizarTipoServico(id, tipoServico){
    const response = await api.put(`/tipos-servico/${id}`, tipoServico);
    return response.data;
}

export async function deletarTipoServico(id){
    const response = await api.delete(`/tipos-servico/${id}`);
    return response.data;
}