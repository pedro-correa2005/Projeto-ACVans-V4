import { useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import Header from "../components/Header";
import Footer from "../components/Footer";

import {
  useAuth
} from "../context/authContext";

import { getHomeByRole } from "../utils/roleUtils";

function Login() {
  const navigate = useNavigate();

  const {
    login,
    authenticated
  } = useAuth();

  const [email, setEmail] = useState("");
  
  const [senha, setSenha] = useState("");

  const [loading, setLoading] = useState(false);
  
  if(authenticated){
    return(
      <Navigate to="/" />
    );
  }

  async function handleSubmit(e){
    e.preventDefault();
    try{
      setLoading(true);
      const response = await login(email, senha);

      if(response.status === 200){
        toast.success("Login realizado com sucesso!");
        navigate(getHomeByRole(response.data.usuario));
      }

      if(response.status === 202){
        navigate("/2fa", {state: {tempToken: response.data.tempToken}});
      }

    }catch(error){
      console.error(error);
      const mensagem = error.respones?.data?.message || "Erro ao realizar login";
      toast.error(mensagem);
    } finally {
      setLoading(false);
    }
  }

  return (
    <>
      <Header />
      <main className="container mt-4">
        <div className="form-wrapper">
          <h1>Acesso ao Sistema</h1>        
          <form className="form" onSubmit={handleSubmit}>
              <div className="form-group">
                  <label htmlFor="email">Email</label>
                  <input type="email" id="email" name="email" value={email} onChange={(e) => {setEmail(e.target.value)}}/>
              </div>
              <div className="form-group">
                  <label htmlFor="password">Senha</label>
                  <input type="password" id="password" name="password" value={senha} onChange={(e) => {setSenha(e.target.value)}}/>
              </div>
              <div className="form-actions">
                  <button type="submit" className="btn btn-primary" disabled={loading}>{loading?"Entrando...":"Entrar"}</button>
              </div>
          </form>
        </div>
      </main>
    <Footer />
  </>
  );
}

export default Login;