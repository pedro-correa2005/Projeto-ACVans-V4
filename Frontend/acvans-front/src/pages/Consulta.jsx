import { useState } from "react";
import { useNavigate } from "react-router-dom";

import Header from "../components/Header";
import Footer from "../components/Footer";


function Consulta(){

	const navigate = useNavigate();

	const [token, setToken] = useState("");
	const [placa, setPlaca] = useState("");
	
	function handleTokenChange(e){
		let value = e.target.value.toUpperCase();

		value = value.replace(/[^A-Z0-9]/g, "");

		value = value.slice(0, 6);

		setToken(value);
	}

	return(
	<div>
		<Header />
		<main class="container mt-4">
			<div class="form-wrapper">
				<h1>Acompanhamento do Serviço</h1>
				<p class="form-description">
					Digite o código do serviço e a placa do veículo para ver o status.
				</p>
				<form id="formBusca" th:action="@{/consulta}" class="form" method="get">
					<div class="form-group">
						<label for="token">Código do Serviço</label>
						<input type="text" class="form-control" name="token" id="token" placeholder="Ex: A1B2C3" required autocomplete="off"/>
					</div>
					<div class="form-group">
						<label for="placa">Placa do Veículo</label>
						<input type="text" class="form-control" name="placa" id="placa" placeholder="ABC1234 ou ABC1D23" required autocomplete="off"/>
					</div>
					<div class="form-actions">
						<button type="submit" class="btn btn-primary">Buscar Serviço</button>
					</div>
				</form>
			</div>
		</main>
		<Footer />
	</div>
    );
}

export default Consulta;