import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import Header from "../components/Header";
import Footer from "../components/Footer";

import PasswordRequirements from "../components/PasswordRequirements";

import { validatePassword } from "../utils/passwordValidator";
import { alterarSenha } from "../services/authService";
import { useAuth } from "../context/authContext";

import { getHomeByRole } from "../utils/roleUtils";

function AlterarSenha(){
    const navigate = useNavigate();

    const { user, logout } = useAuth();

    const [senhaAtual, setSenhaAtual] = useState("");
    const [novaSenha, setNovaSenha] = useState("");
    const [repetirNovaSenha, setRepetirNovaSenha] = useState("");
    const [loading, setLoading] = useState(false);

    const policy = user?.passwordPolicy;

    const passwordValidation = validatePassword(novaSenha, policy);

    const senhaValida = Object.values(passwordValidation).every(Boolean);

    async function handleLogout() {
        await logout();
    }

    async function handleSubmit(e) {
        e.preventDefault();
        if(novaSenha !== repetirNovaSenha){
            toast.error("As senhas não coincidem");
            return;
        }
        try {
            setLoading(true);

            await alterarSenha(senhaAtual, novaSenha, repetirNovaSenha);

            toast.success("Senha alterada com sucesso");

            await logout();

            window.location.href = "/login";
        } catch (error) {
            console.error(error);
            const erros = error.response?.data;

            if(Array.isArray(erros)){
                toast.error(
                    <div>
                        <strong>
                            Corrija os erros:
                        </strong>
                        <ul className="mb-0 mt-2">
                            {
                                erros.map((erro, index) => (
                                    <li key={index}> 
                                        {erro}
                                    </li>
                                    )
                                )
                            }

                        </ul>

                    </div>
                );
                return;
            }
            toast.error(error.response?.data?.message || "Erro ao alterar senha");
        } finally {
            setLoading(false);
        }
    }
    return(
        <>
            <Header />
            <main className="container mt-4">
                <div className="form-wrapper">
                    <h1>Mudar Senha</h1>
                    <p className="form-description">
                        Você precisa alterar a senha antes de continuar.
                    </p>
                    <form className="form" onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label>Senha atual</label>
                            <input type="password" className="form-control" value={senhaAtual} onChange={(e) => setSenhaAtual(e.target.value)}/>
                        </div>
                        <div className="form-group">
                            <label>Nova senha</label>
                            <input type="password" className="form-control" value={novaSenha} onChange={(e) => setNovaSenha(e.target.value)}/>
                            <PasswordRequirements validation={passwordValidation} policy={policy}/>
                        </div>
                        <div className="form-group">
                            <label>Repetir nova senha</label>
                            <input type="password" className="form-control" value={repetirNovaSenha} onChange={(e) => setRepetirNovaSenha(e.target.value)}/>
                            {repetirNovaSenha && (
                                <small className={novaSenha === repetirNovaSenha?"text-success":"text-danger"}>
                                    {novaSenha === repetirNovaSenha?"✓ Senhas coincidem":"✗ Senhas diferentes"}
                                </small>
                            )}
                        </div>
                        <div className="form-actions">
                            <button type="submit" className="btn btn-primary" disabled={loading || !senhaValida || novaSenha !== repetirNovaSenha}>
                                {loading? "Alterando..." : "Alterar senha"}
                            </button>
                            <button className="btn btn-danger" onClick={handleLogout}>
                            Sair da Conta
                        </button>
                        </div>
                    </form>
                </div>
            </main>
            <Footer />
        </>
    );
}

export default AlterarSenha;