import { BrowserRouter, Routes, Route } from "react-router-dom";
import Inicio from "./pages/Inicio";
import Login from "./pages/Login"
import Consulta from "./pages/Consulta"
import RespostaConsulta from "./pages/RespostaConsulta"
import { ToastContainer } from "react-toastify";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Inicio />} />
        <Route path="/login" element={<Login />}></Route>
        <Route path="/consultar-servico" element={<Consulta />}></Route>
        <Route path="/consulta" element={<RespostaConsulta />}></Route>
      </Routes>

      <ToastContainer position="top-right autoClose={300}"/>
    </BrowserRouter>
  );
}

export default App;