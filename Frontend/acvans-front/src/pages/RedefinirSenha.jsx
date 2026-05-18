import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { toast } from "react-toastify";

import Header from "../components/Header";
import Footer from "../components/Footer";

import { resetPassword } from "../services/authService";
import { validarResetToken } from "../services/authService";
import { validatePassword } from "../utils/passwordValidator";
import PasswordRequirements from "../components/PasswordRequirements";

function RedefinirSenha(){
    const navigate = useNavigate();

    const [checkingToken, setCheckingToken] = useState(true);

    const [policy, setPolicy] = useState(null);

    const [searchParams] = useSearchParams();

    const token = searchParams.get("token");

    useEffect(() => {
        async function validarToken(){
            if(!token){
                toast.error("Token inválido");
                navigate("/login");
                return;
            }
            try {
                const response = await validarResetToken(token);
                setPolicy(response.data);
            } catch (error) {
                console.log(error);
                toast.error(error.response?.data?.message || "Link inválido");
                navigate("/login");
            }finally{
                setCheckingToken(false);
            }
        }
        validarToken();
    }, [token, navigate]);
    
    const [novaSenha, setNovaSenha] = useState("");
    
    const [repetirNovaSenha, setRepetirNovaSenha] = useState("");
    
    const [loading, setLoading] = useState(false);
    
    const passwordValidation = validatePassword(novaSenha, policy);

    const senhaValida = Object.values(passwordValidation).every(Boolean);

    if (checkingToken || !policy) {
        return (
            <div className="d-flex justify-content-center align-items-center min-vh-100">
                <div className="spinner-border text-primary"/>
            </div>
        );
    }

    async function handleSubmit(e){
        e.preventDefault();
        if(novaSenha != repetirNovaSenha){
            toast.error("As senhas não coincidem");
            return;
        }
        try {
            setLoading(true);
            const response = await resetPassword(token, novaSenha, repetirNovaSenha);
            toast.success(response);
            navigate("/login");
        } catch (error) {
            console.log(error.response.data);
            if (error.response?.status === 410) {
                toast.error("Link expirado");
                navigate("/login");
                return;
            }
            toast.error("Erro ao redefinir senha.");
        }finally{
            setLoading(false);
        }
    }

    return (
        <>
            <Header />
            <main className="container mt-4">
                <div className="form-wrapper">
                    <h1>Redefinir Senha</h1>
                    <p className="form-description">Digite sua nova senha.</p>
                    <form className="form" onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="novaSenha">Nova Senha</label>
                            <input type="password" id="novaSenha" className="form-control" value={novaSenha} onChange={(e) => setNovaSenha(e.target.value)} />
                            <PasswordRequirements validation={passwordValidation} policy={policy}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="repetirNovaSenha">Repetir Nova Senha</label>
                            <input type="password" id="repetirNovaSenha" className="form-control" value={repetirNovaSenha} onChange={(e) => setRepetirNovaSenha(e.target.value)} />
                            {repetirNovaSenha && (
                                <small className={novaSenha === repetirNovaSenha?"text-success":"text-danger"}>
                                    {novaSenha === repetirNovaSenha?"✓ Senhas coincidem":"✗ Senhas diferentes"}
                                </small>
                            )}
                        </div>
                        <div className="form-actions">
                            <button type="submit" className="btn btn-primary" disabled={loading||!senhaValida||novaSenha !== repetirNovaSenha}>
                                {loading?"Redefinindo...":"Redefinir"}
                            </button>
                        </div>
                    </form>
                </div>
            </main>
            <Footer />
        </>
    );
}

export default RedefinirSenha;