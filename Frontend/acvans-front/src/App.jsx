import { BrowserRouter, Routes, Route } from "react-router-dom";
import Inicio from "./pages/Inicio";
import Login from "./pages/Login"
import Consulta from "./pages/Consulta"
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Inicio />} />
        <Route path="/login" element={<Login />}></Route>
        <Route path="/consultar-servico" element={<Consulta />}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;