import api from "./api";

export async function listarCadastros(termo, pagina, tamnho = 10, sort) {
    const response = await api.get("/cadastros", {params: {termo, page: pagina, size: tamanho, sort: sort}});
    return response.data;
}