import api from "./api";

export async function listarOficinas(termo, pagina, tamanho = 10){
    const response = await api.get("/admin/oficinas", {params: {termo, page: pagina, size: tamanho}});
    return response.data;
}

export async function criarOficina(oficina){
    const response = await api.post("/admin/oficinas", oficina);
    return response.data;
}

export async function atualizarOficina(id, oficina) {
    const response = await api.put(`/admin/oficinas/${id}`, oficina);
    return response.data;
}

export async function deletarOfician(id){
    const response = await api.delete(`/admin/oficinas/${id}`);
    return response.data;
}