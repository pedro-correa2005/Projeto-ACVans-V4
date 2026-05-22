package com.fooengineers.projetoAcVansV4.auditoria.formatter;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.EtapaServico;
import com.fooengineers.projetoAcVansV4.entity.Servico;
import com.fooengineers.projetoAcVansV4.entity.TipoServico;
import com.fooengineers.projetoAcVansV4.entity.Veiculo;

public class AuditoriaFormatter {
	
	public static Map<String, Object> formatarCliente(Cliente cliente){
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Nome", cliente.getNome());
		map.put("Celular", cliente.getCelular());
		return map;
	}
	
	public static Map<String, Object> formatarVeiculo(Veiculo veiculo){
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Placa", veiculo.getPlaca());
		map.put("Marca", veiculo.getMarca());
		map.put("Modelo", veiculo.getModelo());
		return map;
	}
	
	public static Map<String, Object> formatarTipo(TipoServico tipo){
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Descrição", tipo.getDescricao());
		return map;
	}
	
	public static Map<String, Object> formatarEtapa(EtapaServico etapa){
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Ordem", etapa.getOrdem());
		map.put("Título", etapa.getTitulo());
		map.put("Descrição", etapa.getDescricao());
		return map;
	}
	
	public static Map<String, Object> formatarServico(Servico servico){
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("Token de consulta", servico.getTokenConsulta());
		map.put("Notificação", servico.getReceberNotificacao()? "Ativada": "Desativada");
		map.put("Data de início", formatarData(servico.getDataInicio()));
		map.put("Data de fim", servico.getDataFim() != null?formatarData(servico.getDataFim()): "Não finalizado");
		map.put("Tipo de serviço", servico.getTipoServico().getDescricao());
		map.put("Status de serviço", servico.getStatusServico().getDescricao());
		map.put("Etapa de Serviço",servico.getEtapaServico() != null? servico.getEtapaServico().getTitulo() : "Não iniciado");
        return map;
	}
	
	private static String formatarData(Timestamp timestamp) {
		if(timestamp == null) {
			return null;
		}
		
		return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
	}
}
