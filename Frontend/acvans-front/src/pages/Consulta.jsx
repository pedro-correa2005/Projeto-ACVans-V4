import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import '../styles/consulta.css'

import Header from "../components/Header";
import Footer from "../components/Footer";
import PlacaInput from "../components/input/PlacaInput";

import { validarPlaca } from "../utils/placaUtils";


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

	function handlePlacaChange(e){
		let value = e.target.value.toUpperCase();

		value = value.replace(/[^A-Z0-9]/g, "");

		value = value.slice(0, 7);

		setPlaca(value);
	}

	function handleSubmit(e){
		e.preventDefault();

		if(token.length !== 6 ){
			toast.error("Código inválido");
			return;
		}

		if(!validarPlaca(placa)){
			toast.error("Placa inválida");
			return;
		}

		navigate(`/consulta?token=${token}&placa=${placa}`);
	}

	return(
	<div>
		<Header />
		<main className="container mt-4">
			<div className="form-wrapper">
				<h1>Acompanhamento do Serviço</h1>
				<p className="form-description">
					Digite o código do serviço e a placa do veículo para ver o status.
				</p>
				<form className="form" id="formBusca" onSubmit={handleSubmit}>
					<div className="form-group">
						<label htmlFor="token">Código do Serviço</label>
						<input type="text" className="form-control" name="token" id="token" placeholder="Ex: A1B2C3" autoComplete="off" value={token} onChange={handleTokenChange}/>
					</div>
					<div className="form-group">
						<PlacaInput value={placa} onChange={setPlaca} required={true} />
					</div>
					<div className="form-actions">
						<button type="submit" className="btn btn-primary">Buscar Serviço</button>
					</div>
				</form>
			</div>
		</main>
		<Footer />
	</div>
    );
}

export default Consulta;