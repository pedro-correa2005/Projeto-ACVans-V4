import api from "./api";

export async function listarRoles(){
    const response = await api.get("/admin/roles");
    return response.data;
}

export async function listarUsuarios(idOficina, termo, pagina, tamanho = 10, sort) {
    const response = await api.get(`/admin/oficinas/${idOficina}/usuarios`, {params: {termo, page: pagina, size: tamanho, sort: sort}});
    return response.data;
}

export async function criarUsuario(usuario, idOficina){
    const response = await api.post(`/admin/oficinas/${idOficina}/usuarios`, usuario);
    return response.data;
}

export async function atualizarRoles(id, roles){
    const response = await api.put(`/admin/usuarios/${id}/atualizar-roles`, roles);
    return response.data;
}

export async function deletarUsuario(id){
    const response = await api.delete(`/admin/usuarios/${id}`);
    return response.data;
}