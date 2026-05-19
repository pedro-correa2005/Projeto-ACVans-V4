import { useState, useEffect } from "react";

import DataTable from "../../components/table/DataTable";
import SearchBar from "../../components/search/SearchBar";
import CrudModal from "../../components/modal/CrudModal";
import Pagination from "../../components/pagination/Pagination"

import OficinaForm from "../../components/oficina/OficinaForm";

import { listarOficinas, criarOficina, atualizarOficina } from "../../services/oficinaService";
import { toast } from "react-toastify";

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
        loadingSave,
        setLoadingSave
    ] = useState(false);

    const [
        nome,
        setNome
    ] = useState("");

    const [
        ativo,
        setAtivo
    ] = useState(true);

    const [
        showModal,
        setShowModal
    ] = useState(false);

    const [
        page,
        setPage
    ] = useState(null);

    const [
        pageNumber,
        setPageNumber
    ] = useState(0);

    const [
        editingOficina,
        setEditingOfiicna
    ] = useState(null);

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
                            <button className="btn btn-sm btn-danger">Excluir</button>
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
            </main>
            <Footer />
        </>
    );
}

export default Oficinas;