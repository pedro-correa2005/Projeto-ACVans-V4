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
import Oficinas from "./pages/admin/Oficinas.jsx";
import Usuarios from "./pages/admin/Usuarios.jsx";
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
      </Routes>

      <ToastContainer position="top-right autoClose={300}"/>
    </BrowserRouter>
  );
}

export default App;