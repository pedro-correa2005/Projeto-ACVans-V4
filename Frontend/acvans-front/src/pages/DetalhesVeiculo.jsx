import { useState, useEffect } from "react";

import DataTable from "../components/table/DataTable";
import SearchBar from "../components/search/SearchBar";
import CrudModal from "../components/modal/CrudModal";
import Pagination from "../components/pagination/Pagination";

import { buscarVeiculo, listarServicoPorVeiculo } from "../services/veiculoService";
import { criarServico, atualizarServico, deletarServico, baixarQrCode } from "../services/servicoService"
import ServicoForm from "../components/forms/ServicoForm.jsx";

import { FaEdit, FaTrash, FaQrcode } from "react-icons/fa";

import { toast } from "react-toastify";
import { Link, useParams } from "react-router-dom";

import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";

function DetalhesVeiculo() {
    const { idVeiculo } = useParams();
    const [termoInput, setTermoInput] = useState("");
    const [termo, setTermo] = useState("");
    const [page, setPage] = useState(null);
    const [pageNumber, setPageNumber] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [loadingSave, setLoadingSave] = useState(false);
    const [editingServico, setEditingServico] = useState(null);
    const [deletingServico, setDeletingServico] = useState(null);
    const [loadingDelete, setLoadingDelete] = useState(false);
    const [sortField, setSortField] = useState("id");
    const [sortDirection, setSortDirection] = useState("desc");

    const [veiculo, setVeiculo] = useState(null);

    const [tipoServico, setTipoServico] = useState(null);
    const [receberNotificacao, setReceberNotificacao] = useState(true);
    const [statusServico, setStatusServico] = useState(null);
    const [veiculoSelecionado, setVeiculoSelecionado] = useState(null);
    const [veiculoInput, setVeiculoInput] = useState(null);

    async function carregar() {
        try {
            const dataServicos = await listarServicoPorVeiculo(idVeiculo, termo, pageNumber, 10, `${sortField},${sortDirection}`);
            setPage(dataServicos);
            const veiculoData = await buscarVeiculo(idVeiculo);
            setVeiculo(veiculoData);
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
        setEditingServico(null);
        setTipoServico(null);
        setReceberNotificacao(true);
        setStatusServico(null);
        setVeiculoSelecionado(veiculo)
        setVeiculoInput(veiculo.placa);
        setShowModal(true);
    }

    async function salvar() {
        if (!veiculoSelecionado) {
            toast.error("Selecione um veículo");
            return;
        }
        try {
            setLoadingSave(true);
            const idVeiculo = veiculoSelecionado?.id;
            const idTipoServico = tipoServico.id;
            const idStatusServico = statusServico.id;
            const servico = { idTipoServico, receberNotificacao, idStatusServico, idVeiculo };
            if (editingServico) {
                await atualizarServico(editingServico.id, servico);
                toast.success("Serviço atualizado com sucesso");
            } else {
                await criarServico(servico);
                toast.success("Serviço criado com sucesso");
            }
            setShowModal(false);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error("Erro ao salvar serviço: " + error.response?.data?.message);
        } finally {
            setLoadingSave(false);
        }
    }

    function editar(servico) {
        setEditingServico(servico);
        setReceberNotificacao(servico.receberNotificacao);
        setTipoServico(servico.tipoServico)
        setStatusServico(servico.statusServico);
        setVeiculoSelecionado(veiculo)
        setVeiculoInput(veiculo.placa);
        setShowModal(true);
    }

    function confirmarDeletar(servico) {
        setDeletingServico(servico);
    }

    async function deletar() {
        if (!deletingServico) {
            return;
        }
        try {
            setLoadingDelete(true);
            await deletarServico(deletingServico.id);
            toast.success("Servico deletado com sucesso");
            setDeletingServico(null);
            carregar();
        } catch (error) {
            console.error(error);
            toast.error(error.response?.data?.message || "Erro ao excluir");
        } finally {
            setLoadingDelete(false);
        }
    }

    async function handleDownloadQrCode(servico) {
        try {
            const blob = await baixarQrCode(servico.id);
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = url;
            link.download = `qrcode-servico-${servico.id}.png`;
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error(error);
            toast.error("Erro ao baixar QR Code");
        }
    }
    return (
        <>
            <Header />
            <main className="container mt-4">
                <h1 className="mb-4">Delhes do veículo</h1>
                <p className="form-description">
                    Informações do veículo
                </p>
                <div className="mt-4">
                    <div className="mb-3">
                        <strong>
                            Placa:
                        </strong>
                        <div>
                            {veiculo?.placa}
                        </div>
                    </div>
                    <div className="mb-3">
                        <strong>
                            Marca: 
                        </strong>
                        <div>
                            {veiculo?.marca}
                        </div>
                    </div>
                    <div className="mb-3">
                        <strong>
                            Modelo: 
                        </strong>
                        <div>
                            {veiculo?.modelo}
                        </div>
                    </div>
                    <div className="mb-3">
                        <h2>
                            Serviços:
                        </h2>
                    </div>
                </div>
                <form className="mb-3">
                    <SearchBar
                        value={termoInput}
                        onChange={setTermoInput}
                        placeholder="Pesquisar por placa ou nome do cliente..."
                    />
                </form>
                <DataTable
                    page={page}
                    columns={[
                        {
                            key: "tokenConsulta",
                            label: "Código"
                        },
                        {
                            key: "tipoServico.descricao",
                            label: "Tipo de serviço"
                        },
                        {
                            key:"statusServico.descricao",
                            label:"Status"
                        },
                        {
                            key: "receberNotificacao",
                            label: "Notificação",
                            render: (value) => value ? "Ativada" : "Desativada"
                        },
                        {
                            key: "dataInicio",
                            label: "Data de início",
                            render: (value) => {
                                if (value === null) {
                                    return "Não finalizado";
                                }
                                const data = new Date(value);
                                return new Intl.DateTimeFormat('pt-BR', {
                                    dateStyle: 'short',
                                    timeStyle: 'short'
                                }).format(data);
                            }

                        },
                        {
                            key: "etapaServico.titulo",
                            label: "Etapa Atual",
                            render: (value) => value ? value : "Não iniciado"
                        },
                        {
                            key: "dataFIm",
                            label: "Data de fim",
                            render: (value) => {
                                if (value === null) {
                                    return "Não finalizado";
                                }
                                const data = new Date(value);
                                return new Intl.DateTimeFormat('pt-BR', {
                                    dateStyle: 'short',
                                    timeStyle: 'short'
                                }).format(data);
                            }
                        }
                    ]}
                    actions={(servico) => (
                        <>
                            <button className="btn btn-sm btn-primary" onClick={() => editar(servico)}><FaEdit /></button>
                            <button className="btn btn-sm btn-secondary" onClick={() => handleDownloadQrCode(servico)}><FaQrcode /></button>
                            <button className="btn btn-sm btn-danger" onClick={() => confirmarDeletar(servico)}><FaTrash /></button>
                        </>
                    )}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    onSort={handleSort}
                />
                <div className="mt-3">
                    <button className="btn btn-primary" onClick={abrirModal}>Novo Serviço</button>
                </div>
                <Pagination page={page} onPageChange={setPageNumber} />
                <CrudModal
                    title={editingServico ? "Editar Servico" : "Cadastrar Servico"}
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
                    <ServicoForm
                        tipoServico={tipoServico}
                        setTipoServico={setTipoServico}
                        receberNotificacao={receberNotificacao}
                        setReceberNotificacao={setReceberNotificacao}
                        statusServico={statusServico}
                        setStatusServico={setStatusServico}
                        veiculoInput={veiculoInput}
                        setVeiculoInput={setVeiculoInput}
                        setVeiculoSelecionado={setVeiculoSelecionado}
                        veiculoInputDisabled={true}
                        editing={editingServico}
                    />
                </CrudModal>
                <CrudModal
                    title="Confirmar exclusão"
                    show={!!deletingServico}
                    onClose={() => setDeletingServico(null)}
                    footer={
                        <>

                            <button className="btn btn-secondary" onClick={() => setDeletingServico(null)}>
                                Cancelar
                            </button>
                            <button className="btn btn-danger" onClick={deletar} disabled={loadingDelete}>
                                {loadingDelete ? "Deletando..." : "Deletar"}
                            </button>
                        </>
                    }>
                    <p className="mb-0">Tem certeza que deseja deletar servico <strong>{deletingServico?.nome}</strong>? Todos os usuários desta servico também serão deletados. Esta ação não pode ser desfeita.</p>
                </CrudModal>
            </main>
            <Footer />
        </>
    );
}

export default DetalhesVeiculo;