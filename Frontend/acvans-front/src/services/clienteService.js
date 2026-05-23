import api from "./api";

export async function buscarCliente(id){
    const response = await api.get(`/clientes/${id}`);
    return response.data;
}

export async function listarClientes(termo, pagina, tamanho = 10, sort) {
    const response = await api.get(`/clientes`, {params: {termo, page: pagina, size: tamanho, sort: sort}});
    return response.data;
}

export async function criarCliente(cliente){
    const response = await api.post(`/clientes`, cliente);
    return response.data;
}

export async function atualizarCliente(id, cliente){
    const response = await api.put(`/clientes/${id}`, cliente);
    return response.data;
}

export async function deletarCliente(id){
    const response = await api.delete(`/clientes/${id}`);
    return response.data;
}

export async function listarVeiculoPorCliente(id, termo, pagina, tamanho = 10, sort) {
    const response = await api.get(`/clientes/${id}/veiculos`, {params: {termo, page: pagina, size: tamanho, sort: sort}});
    return response.data;
}