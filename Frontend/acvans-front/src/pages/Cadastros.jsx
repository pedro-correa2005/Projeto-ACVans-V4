import { useState, useEffect } from "react";

import DataTable from "../components/table/DataTable";
import SearchBar from "../components/search/SearchBar";
import CrudModal from "../components/modal/CrudModal";
import Pagination from "../components/pagination/Pagination";

import { listarCadastros } from "../services/cadastrosService";
import { criarServico, atualizarServico, deletarServico, baixarQrCode } from "../services/servicoService"
import ServicoForm from "../components/forms/ServicoForm.jsx";

import { FaEdit, FaTrash, FaQrcode } from "react-icons/fa";

import { toast } from "react-toastify";
import { Link } from "react-router-dom";

import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";

function Cadastros() {
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

    const [filtroStatus, setFiltroStatus] = useState(2);

    const [tipoServico, setTipoServico] = useState(null);
    const [receberNotificacao, setReceberNotificacao] = useState(true);
    const [statusServico, setStatusServico] = useState(null);
    const [veiculoSelecionado, setVeiculoSelecionado] = useState(null);
    const [veiculoInput, setVeiculoInput] = useState(null);

    async function carregar() {
        try {
            const data = await listarCadastros(termo, pageNumber, 10, `${sortField},${sortDirection}`, filtroStatus);
            setPage(data);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        carregar();
    }, [termo, pageNumber, sortField, sortDirection, filtroStatus]);

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
        setVeiculoSelecionado(null)
        setVeiculoInput(null);
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
            console.log(idVeiculo);
            const idTipoServico = tipoServico.id;
            const idStatusServico = statusServico.id;
            const servico = { idTipoServico, receberNotificacao, idStatusServico, idVeiculo };
            if (editingServico) {
                await atualizarServico(editingServico.idServico, servico);
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
        setReceberNotificacao(true);
        setTipoServico(servico.tipo)
        console.log(servico.descricaoTipoServico);
        setStatusServico(servico.status);
        setVeiculoSelecionado(servico.veiculo)
        setVeiculoInput(servico.veiculo.placa);
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
            await deletarServico(deletingServico.idServico);
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
            const blob = await baixarQrCode(servico);
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = url;
            link.download = `qrcode-servico-${servico.idServico}.png`;
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
                <h1 className="mb-4">Cadastros</h1>
                <form className="mb-3">
                    <SearchBar
                        value={termoInput}
                        onChange={setTermoInput}
                        placeholder="Pesquisar por placa ou nome do cliente..."
                    />
                </form>
                <ul className="nav nav-tabs">
                    <li className="nav-item">
                        <button
                            className={`nav-link ${filtroStatus === 1 ? "active" : ""}`}
                            onClick={() => setFiltroStatus(1)}
                        >
                            Agendados
                        </button>
                    </li>
                    <li className="nav-item">
                        <button
                            className={`nav-link ${filtroStatus === 2
                                ? "active"
                                : ""
                                }`}
                            onClick={() =>
                                setFiltroStatus(2)
                            }
                        >
                            Em andamento
                        </button>

                    </li>

                    <li className="nav-item">

                        <button

                            className={`nav-link ${filtroStatus === 3
                                ? "active"
                                : ""
                                }`}

                            onClick={() =>
                                setFiltroStatus(3)
                            }
                        >
                            Finalizados
                        </button>

                    </li>

                </ul>
                <DataTable
                    page={page}
                    columns={[
                        {
                            key: "tokenConsulta",
                            label: "Código"
                        },
                        {
                            key: "tipo.descricao",
                            label: "Tipo de serviço"
                        },
                        {
                            key: "veiculo.placa",
                            label: "Veiculo"
                        },
                        {
                            key: "veiculo.cliente.nome",
                            label: "Cliente"
                        },
                        {
                            key: "veiculo.cliente.celular",
                            label: "Celular"
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
                                const data = new Date(value);
                                return new Intl.DateTimeFormat('pt-BR', {
                                    dateStyle: 'short',
                                    timeStyle: 'short'
                                }).format(data);
                            }

                        },
                        {
                            key: "etapaTitulo",
                            label: "Etapa Atual",
                            render: (value) => value ? value : "Não iniciado"
                        },
                        {
                            key: "dataFim",
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
                    <Link to="/clientes" className="btn btn-secondary ms-3">Ver Clientes</Link>
                    <Link to="/veiculos" className="btn btn-secondary ms-3">Ver veículos</Link>
                    <Link to="/configurar-servicos" className="btn btn-secondary ms-3">Configurar Serviços</Link>
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

export default Cadastros;