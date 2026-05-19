import { useState, useEffect } from "react";

import DataTable from "../../components/table/DataTable";
import SearchBar from "../../components/search/SearchBar";
import CrudModal from "../../components/modal/CrudModal";
import Pagination from "../../components/pagination/Pagination"

import OficinaForm from "../../components/oficina/OficinaForm";

import { listarOficinas, criarOficina, atualizarOficina, deletarOficina } from "../../services/oficinaService";
import { toast } from "react-toastify";

import { Link } from "react-router-dom";

import Header from "../../components/Header";
import Footer from "../../components/Footer";

function Oficinas() {
    const[
        termoInput,
        setTermoInput
    ] = useState("");

    const [
        termo,
        setTermo
    ] = useState("");

    
    const [
        nome,
        setNome
    ] = useState("");
    
    const [
        ativo,
        setAtivo
    ] = useState(true);
    
    
    const [
        page,
        setPage
    ] = useState(null);
    
    const [
        pageNumber,
        setPageNumber
    ] = useState(0);
    
    const [
        showModal,
        setShowModal
    ] = useState(false);

    const [
        loadingSave,
        setLoadingSave
    ] = useState(false);

    const [
        editingOficina,
        setEditingOfiicna
    ] = useState(null);

    const[
        deletingOficina,
        setDeletingOficina
    ] = useState(null);

    const[
        loadingDelete,
        setLoadinDelete
    ] = useState(false);

    async function carregar() {
        try {
            const data = await listarOficinas(termo, pageNumber);
            setPage(data);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        carregar();
    }, [termo, pageNumber]);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setPageNumber(0);
            setTermo(termoInput);
        }, 500);
        
        return () => {
            clearTimeout(timeout);
        };
    }, [termoInput]);

    function abrirModal() {
        setEditingOfiicna(null)
        setNome("");
        setAtivo(true);
        setShowModal(true);
    }

    async function salvar() {
        try {
            setLoadingSave(true);
            const oficina = { nome, ativo };

            if(editingOficina){
                await atualizarOficina(editingOficina.id, oficina);
                toast.success("Oficina atualizada com sucesso")
            }else{
                await criarOficina(oficina);
                toast.success("Oficina criada com sucesso");
            }
            setShowModal(false);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error("Erro ao salvar oficina: " + error.response?.data?.message);
        } finally {
            setLoadingSave(false);
        }
    }

    function editar(oficina){
        setEditingOfiicna(oficina);
        setNome(oficina.nome);
        setAtivo(oficina.ativo);
        setShowModal(true);
    }

    function confirmarDeletar(oficina){
        setDeletingOficina(oficina);
    }

    async function deletar() {
        if(!deletingOficina){
            return;
        }
        try {
            setLoadinDelete(true);
            await deletarOficina(deletingOficina.id);
            toast.success("Oficina deletada com sucesso")
            setDeletingOficina(null);
            carregar();
        } catch (error) {
            console.error(error)
            toast.error(error.response?.data?.message || "Erro ao excluir");
        }finally{
            setLoadinDelete(false);
        }
    }

    return (
        <>
            <Header />
            <main className="container mt-4">
                <h1 className="mb-4">Oficinas</h1>
                <form className="mb-3">
                    <SearchBar value={termoInput} onChange={setTermoInput} placeholder="Pesquisar por nome..." />
                </form>
                <DataTable
                    page={page}
                    columns={[
                        {
                            key: "nome",
                            label: "Nome"
                        },
                        {
                            key: "ativo",
                            label: "Ativo",
                            render: (value) => value ? "Sim" : "Não"
                        }
                    ]}
                    actions={(oficina) => (
                        <>
                            <button className="btn btn-sm btn-primary" onClick={() => editar(oficina)}>Editar</button>
                            <Link to={`/admin/oficinas/${oficina.id}/usuarios`} className="btn btn-sm btn-secondary">Detalhes</Link>
                            <button className="btn btn-sm btn-danger" onClick={() => confirmarDeletar(oficina)}>Excluir</button>
                        </>
                    )}
                />
                <button className="btn btn-primary" onClick={abrirModal}>Nova Oficina</button>
                <Pagination page={page} onPageChange={setPageNumber}/>
                <CrudModal
                    title={editingOficina?"Editar Oficina":"Cadastrar Oficina"}
                    show={showModal}
                    onClose={() => setShowModal(false)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setShowModal(false)}>
                                Cancelar
                            </button>
                            <button className="btn btn-primary" onClick={salvar} disabled={loadingSave}>
                                {loadingSave ? "Salvando..." : "Salvar"}
                            </button>
                        </>
                    }>
                    <OficinaForm 
                        nome={nome}
                        setNome={setNome}
                        ativo={ativo}
                        setAtivo={setAtivo}
                    />
                </CrudModal>
                <CrudModal
                    title="Confirmar exclusão"
                    show={!!deletingOficina}
                    onClose={() => setDeletingOficina(null)}
                    footer={
                        <>
                            
                            <button className="btn btn-secondary" onClick={() => setDeletingOficina(null)}>
                                Cancelar
                            </button>
                            <button className="btn btn-danger" onClick={deletar} disabled={loadingDelete}>
                                {loadingDelete ? "Deletando..." : "Deletar"}
                            </button>
                        </>
                    }>
                    <p className="mb-0">Tem certeza que deseja deletar oficina <strong>{deletingOficina?.nome}</strong>? Todos os usuários desta oficina também serão deletados. Esta ação não pode ser desfeita.</p>
                </CrudModal>
            </main>
            <Footer />
        </>
    );
}

export default Oficinas;