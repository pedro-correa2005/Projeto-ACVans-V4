import { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { Link, useParams } from "react-router-dom";

import DataTable from "../components/table/DataTable";
import SearchBar from "../components/search/SearchBar";
import CrudModal from "../components/modal/CrudModal";
import Pagination from "../components/pagination/Pagination";

import { criarVeiculo, atualizarVeiculo, deletarVeiculo } from "../services/veiculoService.js";

import { listarVeiculoPorCliente, buscarCliente } from "../services/clienteService.js";

import VeiculoForm from "../components/forms/VeiculoForm.jsx";

import Header from "../components/Header";
import Footer from "../components/Footer";

function DetalhesCliente() {
    const { idCliente } = useParams();
    const [termoInput, setTermoInput] = useState("");
    const [termo, setTermo] = useState("");
    const [page, setPage] = useState(null);
    const [pageNumber, setPageNumber] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [loadingSave, setLoadingSave] = useState(false);
    const [editingVeiculo, setEditingVeiculo] = useState(null);
    const [deletingVeiculo, setDeletingVeiculo] = useState(null);
    const [loadingDelete, setLoadingDelete] = useState(false);
    const [sortField, setSortField] = useState("id");
    const [sortDirection, setSortDirection] = useState("desc");
    const [cliente, setCliente] = useState(null);

    const [placa, setPlaca] = useState("");
    const [marca, setMarca] = useState("");
    const [modelo, setModelo] = useState("");
    const [clienteInput, setClienteInput] = useState("");
    const [clienteSelecionado, setClienteSelecionado] = useState(null);

    async function carregar() {
        try {
            const veiculosData = await listarVeiculoPorCliente(idCliente, termo, pageNumber, 10, `${sortField},${sortDirection}`);
            setPage(veiculosData);
            const clienteData = await buscarCliente(idCliente);
            setCliente(clienteData);
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
    }, [termoInput])

    async function handleSort(field) {
        if (sortField === field) {
            setSortDirection(sortDirection === "asc" ? "desc" : "asc");
        } else {
            setSortField(field);
            setSortDirection("asc");
        }
        setPageNumber(0);
    }

    function abrirModal() {
        setEditingVeiculo(null);
        setPlaca("");
        setMarca("");
        setModelo("");
        setClienteSelecionado(cliente)
        setClienteInput(cliente.nome);
        setShowModal(true);
    }

    async function salvar() {
        if (!clienteSelecionado) {
            toast.error("Selecione um cliente");
            return;
        }
        try {
            setLoadingSave(true);
            const idCliente = clienteSelecionado?.id;
            const veiculo = { placa, marca, modelo, idCliente };
            if (editingVeiculo) {
                await atualizarVeiculo(editingVeiculo.id, veiculo);
                toast.success("Veiculo atualizado com sucesso");
            } else {
                await criarVeiculo(veiculo);
                toast.success("Veiculo criado com sucesso");
            }
            setShowModal(false);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error("Erro ao salvar veiculo: " + error.response?.data?.message);
        } finally {
            setLoadingSave(false);
        }
    }

    function editar(veiculo) {
        setEditingVeiculo(veiculo);
        setPlaca(veiculo.placa);
        setMarca(veiculo.marca);
        setModelo(veiculo.modelo);
        setClienteSelecionado(veiculo.cliente)
        setClienteInput(veiculo.cliente.nome);
        setShowModal(true);
    }

    function confirmarDeletar(veiculo) {
        setDeletingVeiculo(veiculo);
    }

    async function deletar() {
        if (!deletingVeiculo) {
            return;
        }
        try {
            setLoadingDelete(true);
            await deletarVeiculo(deletingVeiculo.id);
            toast.success("Veiculo deletado com sucesso");
            setDeletingVeiculo(null);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error(error.response?.data?.message || "Erro ao excluir");
        } finally {
            setLoadingDelete(false);
        }
    }

    return (
        <>
            <Header />
            <main className="container mt-4">
                <h1 className="mb-4">Detalhes do cliente</h1>
                <p className="form-description">
                    Informações do cliente.
                </p>
                <div className="mt-4">
                    <div className="mb-3">
                        <strong>
                            Nome:
                        </strong>
                        <div>
                            {cliente?.nome}
                        </div>
                    </div>
                    <div className="mb-3">
                        <strong>
                            Celular: 
                        </strong>
                        <div>
                            {cliente?.celular}
                        </div>
                    </div>
                    <div className="mb-3">
                        <h2>
                            Veículos:
                        </h2>
                    </div>
                </div>
                <form className="mb-3">
                    <SearchBar value={termoInput}
                        onChange={setTermoInput} placeholder="Pesquisar por placa, marca ou modelo..."
                    />
                </form>
                <DataTable
                    page={page}
                    columns={[
                        {
                            key: "placa",
                            label: "Placa"
                        },
                        {
                            key: "marca",
                            label: "Marca"
                        },
                        {
                            key: "modelo",
                            label: "Modelo"
                        },
                        {
                            key: "cliente.nome",
                            label: "Cliente"
                        }
                    ]}
                    actions={(veiculo) => (
                        <>
                            <button className="btn btn-sm btn-primary " onClick={() => editar(veiculo)}>Editar</button>
                            <Link to={`/veiculos/${veiculo.id}`} className="btn btn-sm btn-secondary">Detalhes</Link>
                            <button className="btn btn-sm btn-danger" onClick={() => confirmarDeletar(veiculo)}>Deletar</button>
                        </>
                    )}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    onSort={handleSort}
                />
                <div className="mt-3">
                    <button className="btn btn-primary" onClick={abrirModal}>Adicionar Veículo</button>
                    <Link to="/clientes" className="btn btn-secondary ms-3">Voltar</Link>
                </div>
                <Pagination page={page} onPageChange={setPageNumber} />
                <CrudModal
                    title={editingVeiculo ? "Editar Veículo" : "Cadastrar Veículo"}
                    show={showModal}
                    onClose={() => setShowModal(false)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancelar</button>
                            <button className="btn btn-primary" onClick={salvar} disabled={loadingSave}>
                                {loadingSave ? "Salvando..." : "Salvar"}
                            </button>
                        </>
                    }>
                    <VeiculoForm
                        placa={placa}
                        setPlaca={setPlaca}
                        marca={marca}
                        setMarca={setMarca}
                        modelo={modelo}
                        setModelo={setModelo}
                        clienteSelecionado={clienteSelecionado}
                        setClienteSelecionado={setClienteSelecionado}
                        clienteInput={clienteInput}
                        setClienteInput={setClienteInput}
                        editing={true}
                    />
                </CrudModal>
                <CrudModal
                    title="Confirmar exclusão"
                    show={!!deletingVeiculo}
                    onClose={() => setDeletingVeiculo(null)}
                    footer={
                        <>
                            <button className="btn btn-secondary" onClick={() => setDeletingVeiculo(null)}>
                                Cancelar
                            </button>
                            <button className="btn btn-danger" onClick={deletar} disabled={loadingDelete}>
                                {loadingDelete ? "Deletando..." : "Deletar"}
                            </button>
                        </>
                    }>
                    <p className="mb-0">Tem certeza que deseja deletar veiculo <strong>{deletingVeiculo?.placa} {deletingVeiculo?.marca} {deletingVeiculo?.modelo}</strong>? Todos os serviços cadastrados para este veiculo serão apagados. Esta ação não pode ser desfeita.</p>
                </CrudModal>
            </main>
            <Footer />
        </>
    )
}
export default DetalhesCliente;