import { BrowserRouter, Routes, Route } from "react-router-dom";
import Inicio from "./pages/Inicio";
import Login from "./pages/Login"
import Consulta from "./pages/Consulta"
import RespostaConsulta from "./pages/RespostaConsulta"
import Perfil from "./pages/Perfil";
import { ToastContainer } from "react-toastify";
import PrivateRoute from "./components/PrivateRoute";
import TwoFactor from "./pages/TwoFactor";
import EsqueciASenha from "./pages/EsqueciASenha";
import RedefinirSenha from"./pages/RedefinirSenha";
import AlterarSenha from "./pages/AlterarSenha";
import Oficinas from "./pages/admin/Oficinas";
import Usuarios from "./pages/admin/Usuarios";
import Cadastros from "./pages/Cadastros";
import Clientes from "./pages/Clientes"
//import DetalhesCliente from "./pages/DetalhesCliente.jsx";
import Veiculos from "./pages/Veiculos.jsx"
import Relatorios from "./pages/gerente/Relatorios";
import ConfigurarServicos from "./pages/gerente/ConfigurarServicos";
import Auditoria from "./pages/gerente/Auditoria";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Inicio />} />
        <Route path="/login" element={<Login />}/>
        <Route path="/consultar-servico" element={<Consulta />}/>
        <Route path="/consulta" element={<RespostaConsulta />}/>
        <Route path="/perfil" element={<PrivateRoute><Perfil/></PrivateRoute>}/>
        <Route path="/2fa" element={<TwoFactor />}/>
        <Route path="/esqueci-a-senha" element={<EsqueciASenha/>}/>
        <Route path="/redefinir-senha" element={<RedefinirSenha/>}/>
        <Route path="/alterar-senha" element={<PrivateRoute><AlterarSenha/></PrivateRoute>}/>
        <Route path="/admin/oficinas" element={<PrivateRoute roles={["ADMIN"]}><Oficinas/></PrivateRoute>}/>
        <Route path="/admin/oficinas/:idOficina/usuarios" element={<PrivateRoute roles={["ADMIN"]}><Usuarios/></PrivateRoute>}/>
        <Route path="/cadastros" element={<PrivateRoute roles={["FUNCIONARIO", "GERENTE"]}><Cadastros/></PrivateRoute>}/>
        <Route path="/clientes" element={<PrivateRoute roles={["FUNCIONARIO", "GERENTE"]}><Clientes/></PrivateRoute>}/>
        {/*<Route path="/clientes/:idCliente" element={<PrivateRoute roles={["FUNCIONARIO", "GERENTE"]}><DetalhesCilente/></PrivateRoute>}/>*/}
        <Route path="/veiculos" element={<PrivateRoute roles={["FUNCIONARIO", "GERENTE"]}><Veiculos/></PrivateRoute>}/>
        <Route path="/configurar-servicos" element={<PrivateRoute roles={["GERENTE"]}><ConfigurarServicos/></PrivateRoute>}/>
        <Route path="/relatorios" element={<PrivateRoute roles={["GERENTE"]}><Relatorios/></PrivateRoute>}/>
        <Route path="/relatorios" element={<PrivateRoute roles={["GERENTE"]}><Auditoria/></PrivateRoute>}/>
      </Routes>

      <ToastContainer position="top-right autoClose={300}"/>
    </BrowserRouter>
  );
}

export default App;