import { useAuth } from "../context/authContext";
import { useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { getRoleLabel } from "../utils/roleUtils";
import { ativar2FA, desativar2FA } from "../services/authService";
import { Link } from "react-router-dom";

import Header from "../components/Header";
import Footer from "../components/Footer";

function Perfil() {
    const {
        user, logout, refreshUser
    } = useAuth();

    const [loading2FA, setLoading2FA] = useState(false);
    async function handleLogout() {
        await logout();
    }

    async function handleToggle2FA() {
        try {
            setLoading2FA(true);
            if (user?.doisFatores) {
                const response = await desativar2FA();
                toast.success("Autenticação de dois fatores desativada");
            } else {
                const response = await ativar2FA();
                toast.success("Autenticação de dois fatores ativada");
            }
            await logout();
        } catch (error) {
            console.error(error);
            toast.error(error.response?.data?.message || "Erro ao alterar autenticação");
        } finally {
            setLoading2FA(false);
        }
    }

    return (
        <>
            <Header />
            <main className="container mt-4">
                <div className="form-wrapper">
                    <h1>Meu Perfil</h1>
                    <p className="form-description">
                        Informações da sua conta.
                    </p>
                    <div className="mt-4">
                        <div className="mb-3">
                            <strong>
                                Email:
                            </strong>
                            <div>
                                {user?.email}
                            </div>
                        </div>
                        <div className="mb-3">
                            <strong>
                                Autenticação de dois fatores:
                            </strong>
                            <div>
                                {user?.doisFatores ? "Ativada" : "Desativada"}
                            </div>
                        </div>
                        <div className="mb-3">
                            <strong>
                                Permissões:
                            </strong>
                            <div>
                                {user?.roles?.map(getRoleLabel).join(", ")}
                            </div>
                        </div>
                    </div>
                    <div className="form-actions mt-4">
                        <button className="btn btn-danger" onClick={handleLogout}>
                            Sair da Conta
                        </button>
                        <Link to="/alterar-senha" className="btn btn-primary">
                            Alterar senha
                        </Link>
                        <button className={user?.doisFatores ? "btn btn-warning" : "btn btn-success"} onClick={handleToggle2FA} disabled={loading2FA}>
                            {loading2FA? "Processando...": user?.doisFatores? "Desativar autenticação em dois fatores": "Ativar autenticação em dois fatores"}
                        </button>
                    </div>
                </div>
            </main>
            <Footer />
        </>
    );
}

export default Perfil;