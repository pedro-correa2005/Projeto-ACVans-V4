import { useState, useEffect } from "react";

import DataTable from "../components/table/DataTable";
import SearchBar from "../components/search/SearchBar";
import CrudModal from "../components/modal/CrudModal";
import Pagination from "../components/pagination/Pagination";

import ClienteForm from "../components/forms/ClienteForm";

import { listarClientes, criarCliente, atualizarCliente, deletarCliente } from "../services/clienteService.js";

import { toast } from "react-toastify";

import { Link } from "react-router-dom";

import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";
import { listarUsuarios } from "../services/usuarioService.js";

function Clientes(){
    const [termoInput, setTermoInput] = useState("");
    const [termo, setTermo] = useState("");
    const [page, setPage] = useState(null);
    const [pageNumber, setPageNumber] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [loadingSave, setLoadingSave] = useState(false);
    const [editingCliente, setEditingCliente] = useState(null);
    const [deletingCliente, setDeletingCliente] = useState(null);
    const [loadingDelete, setLoadingDelete] = useState(false);
    const [sortField, setSortField] = useState("id");
    const [sortDirection,setSortDirection] = useState("desc");
    
    const [nome, setNome] = useState("");
    const [celular, setCelular] = useState(false);

    async function carregar(){
        try {
            const data = await listarClientes(termo, pageNumber, 10, `${sortField},${sortDirection}`);
            setPage(data);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        carregar();
    }, [termo, pageNumber, sortField, sortDirection]);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setPageNumber(0);
            setTermo(termoInput);
        }, 500);
        return () => {
            clearTimeout(timeout);
        }
    }, [termoInput]);

    async function handleSort(field){
        if(sortField === field){
            setSortDirection(sortDirection === "asc"?"desc":"asc");
        }else{
            setSortField(field);
            setSortDirection("asc");
        }
        setPageNumber(0);
    }

    function abrirModal(){
        setEditingCliente(null);
        setNome("");
        setCelular("");
        setShowModal(true);
    }

    async function salvar() {
        try {
            setLoadingSave(true);
            const cliente = {nome, celular};
            if(editingCliente){
                await atualizarCliente(editingCliente.id, cliente);
                toast.success("Cliente atualizado com sucesso");
            }else{
                await criarCliente(cliente);
                toast.success("Cliente criado com sucesso");
            }
            setShowModal(false);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error("Erro ao salvar usuário: " + error.response?.data?.message);
        }finally{
            setLoadingSave(false);
        }
    }

    function editar(cliente){
        setEditingCliente(cliente);
        setNome(cliente.nome);
        setCelular(cliente.celular);
        setShowModal(true);
    }

    function confirmarDeletar(cliente){
        setDeletingCliente(cliente);
    }

    async function deletar() {
        if(!deletingCliente){
            return;
        }
        try {
            setLoadingDelete(true);
            await deletarCliente(deletingCliente.id);
            toast.success("Cliente deletado com sucesso");
            setDeletingCliente(null);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error(error.response?.data?.message || "Erro ao excluir");
        } finally {
            setLoadingDelete(false);
        }
    }

    return(
        <>
            <Header/>
            <main className="container mt-4">
                <h1 className="mb-4">Clientes</h1>
                <form className="mb-3">
                    <SearchBar value={termoInput} 
                    onChange={setTermoInput} placeholder="Pesquisar por nome ou celular..."
                    />
                </form>
                <DataTable
                    page={page}
                    columns={[
                        {
                            key:"nome",
                            label:"Nome"
                        },
                        {
                            key:"celular",
                            label:"Celular"
                        }
                    ]}
                    actions={(cliente) => (
                        <>
                            <button className="btn btn-sm btn-primary" onClick={()=>editar(cliente)}>Editar</button>
                            <Link to={`/clientes/${cliente.id}`} className="btn btn-sm btn-secondary">Detalhes</Link>
                            <button className="btn btn-sm btn-danger" onClick={()=>confirmarDeletar(cliente)}>Deletar</button>
                        </>
                    )}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    onSort={handleSort}
                />
                <button className="btn btn-primary" onClick={abrirModal}>Adicionar Cliente</button>
                <Link to="/cadastros" className="btn btn-secondary">Voltar</Link>
                <Pagination page={page} onPageChange={setPageNumber}/>
                <CrudModal
                    title={editingCliente?"Editar Cliente":"Cadastrar Cliente"}
                    show={showModal}
                    onClose={() => setShowModal(false)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancelar</button>
                            <button className="btn btn-primary" onClick={salvar} disabled={loadingSave}>
                                {loadingSave?"Salvando...":"Salvar"}
                            </button>
                        </>
                    }>
                    <ClienteForm
                        nome={nome}
                        setNome={setNome}
                        celular={celular}
                        setCelular={setCelular}
                    />
                </CrudModal>
                <CrudModal
                    title="Confirmar exclusão"
                    show={!!deletingCliente}
                    onClose={() => setDeletingCliente(null)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setDeletingCliente(null)}>
                                Cancelar
                            </button>
                            <button className="btn btn-danger" onClick={deletar} diabled={loadingDelete}>
                                {loadingDelete?"Deletando...":"Deletar"}
                            </button>
                        </>
                    }>
                    <p className="mb-0">Tem certeza que deseja deletar cliente <strong>{deletingCliente?.nome}</strong>? Todos os veículos e serviços cadastrados para este cliente serão apagados. Esta ação não pode ser desfeita.</p>
                </CrudModal>
            </main>
            <Footer/>
        </>
    )
}

export default Clientes;