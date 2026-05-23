import api from "./api";

export async function listarServicos(termo, pagian, tamnho = 10, sort){
    const response = await api.get("/servicos", {params: {termo, page: pagian, size: tamanho, sort: sort}});
    return response.data;
}

export async function validarTokenAtualizacao(token){
    const response = await api.get("/servicos/atualizar-etapa/validar-token", {params: {token}});
    return response.data;
}

export async function criarServico(servico) {
    const response = await api.post("/servicos", servico);
    return response.data;
}

export async function atualizarServico(id, servico) {
    const response = await api.put(`/servicos/${id}`, servico);
    return response.data;
}

export async function atualizarEtapa(token) {
    const response = await api.patch(`/servicos/atualizar-etapa/${token}`);
    return response.data;
}

export async function deletarServico(id) {
    const response = await api.delete(`/servicos/${id}`);
    return response.data;
}

export async function baixarQrCode(id){
    const response = await api.get(`/servicos/baixar-qrcode?idServico=${id}`, {responseType: "blob"});
    return response.data;
}