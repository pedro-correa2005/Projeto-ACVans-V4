import api from "./api";

export async function listarVeiculos(termo, pagina, tamanho = 10, sort){
    const response = await api.get(`/veiculos`, {params: {termo, page: pagina, size: tamanho, sort: sort}});
    return response.data;
}

export async function criarVeiculo(veiculo) {
    const response = await api.post(`/veiculos`, veiculo);
    return response.data;
}

export async function atualizarVeiculo(id, veiculo){
    const response = await api.put(`/veiculos/${id}`, veiculo);
    return response.data;
}

export async function deletarVeiculo(id){
    const response = await api.delete(`/veiculos/${id}`);
    return response.data;
}