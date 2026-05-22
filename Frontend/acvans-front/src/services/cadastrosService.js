import api from "./api";

export async function listarCadastros(termo, pagina, tamanho = 10, sort, idStatusServico) {
    const response = await api.get(`/cadastros?idStatusServico=${idStatusServico}`, {params: {termo, page: pagina, size: tamanho, sort: sort}});
    return response.data;
}