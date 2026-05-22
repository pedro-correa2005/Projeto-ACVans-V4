import api from "./api";

export async function listarServicos(termo, pagian, tamnho = 10, sort){
    const response = await api.get("/servicos", {params: {termo, page: pagian, size: tamanho, sort: sort}});
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


export async function deletarServico(id) {
    const response = await api.delete(`/servicos/${id}`);
    return response.data;
}

export async function baixarQrCode(servico){
    const response = await api.get(`/servicos/baixar-qrcode?idServico=${servico.idServico}`, {responseType: "blob"});
    return response.data;
}