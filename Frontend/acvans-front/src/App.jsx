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
//import Oficinas from "./pages/Oficinas";
/*<Route path="/admin/oficinas" element={<PrivateRoute roles={["ADMIN"]}><Oficinas/></PrivateRoute>}/>*/
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
        <Route path="/esqueci-a-senha" element={<EsqueciASenha/>}></Route>
        <Route path="/redefinir-senha" element={<RedefinirSenha/>}></Route>
      </Routes>

      <ToastContainer position="top-right autoClose={300}"/>
    </BrowserRouter>
  );
}

export default App;