package com.fooengineers.projetoAcVansV4.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooengineers.projetoAcVansV4.auditoria.dto.Detalhes;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Acao;
import com.fooengineers.projetoAcVansV4.auditoria.entity.Entidade;
import com.fooengineers.projetoAcVansV4.auditoria.service.AuditoriaService;
import com.fooengineers.projetoAcVansV4.dto.VeiculoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.VeiculoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Cliente;
import com.fooengineers.projetoAcVansV4.entity.Oficina;
import com.fooengineers.projetoAcVansV4.entity.Veiculo;
import com.fooengineers.projetoAcVansV4.exception.ClienteNaoEncontradoExcepiton;
import com.fooengineers.projetoAcVansV4.exception.VeiculoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.repository.ClienteRepository;
import com.fooengineers.projetoAcVansV4.repository.VeiculoRepository;
import com.fooengineers.projetoAcVansV4.specification.VeiculoSpecification;

@Service
public class VeiculoService {
	@Autowired
	VeiculoRepository veiculoRepository;
	@Autowired
	ClienteRepository clienteRepository;
	@Autowired
	private AuditoriaService auditoriaService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public Page<VeiculoResDTO> listar(Integer idOficina, String termo, Pageable pageable) {
		return veiculoRepository.findAll(VeiculoSpecification.filtroGeral(termo, idOficina), pageable).map(VeiculoResDTO::new);
	}
	
	public VeiculoResDTO buscar(Long idVeiculo) {
		Veiculo veiculo = veiculoRepository.findById(idVeiculo).orElseThrow(() -> new VeiculoNaoEncontradoException(idVeiculo));
		return new VeiculoResDTO(veiculo);
	}

	public List<VeiculoResDTO> buscarPorCliente(Long idCliente, String termo) {
		clienteRepository.findById(idCliente).orElseThrow(() -> new ClienteNaoEncontradoExcepiton(idCliente));
		List<Veiculo> veiculos = veiculoRepository.findAll(VeiculoSpecification.filtrarPorCliente(termo, idCliente));
		return veiculos.stream().map(VeiculoResDTO::new).collect(Collectors.toList());
	}
	
	public VeiculoResDTO criar(VeiculoReqDTO dto, Oficina oficina) {
		Cliente cliente = clienteRepository.findById(dto.getIdCliente()).orElseThrow(() -> new ClienteNaoEncontradoExcepiton(dto.getIdCliente()));
		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca(dto.getPlaca());
		veiculo.setMarca(dto.getMarca());
		veiculo.setModelo(dto.getModelo());
		veiculo.setCliente(cliente);
		veiculo.setOficina(oficina);
		
		return new VeiculoResDTO(veiculoRepository.save(veiculo));
	}
	public VeiculoResDTO atualizar(VeiculoReqDTO dto, Long idVeiculo) {
		Veiculo veiculo = veiculoRepository.findById(idVeiculo).orElseThrow(() -> new VeiculoNaoEncontradoException(idVeiculo));
		Map<String, Object> antes =
				objectMapper.convertValue(
						new VeiculoResDTO(veiculo),
						new TypeReference<Map<String, Object>>() {}
						);
		
		veiculo.setPlaca(dto.getPlaca());
		veiculo.setMarca(dto.getMarca());
		veiculo.setModelo(dto.getModelo());
		
		Veiculo salvo = veiculoRepository.save(veiculo);

		Map<String, Object> depois =
				objectMapper.convertValue(
						new VeiculoResDTO(salvo),
						new TypeReference<Map<String, Object>>() {}
						);
		
		Detalhes detalhes = new Detalhes(antes, depois);
		auditoriaService.registrar(
				Acao.UPDATE,
				Entidade.VEICULO,
				idVeiculo,
				detalhes);
		
		return new VeiculoResDTO(salvo);
	}
	public void deletar(Long idVeiculo) {
		Veiculo veiculo = veiculoRepository.findById(idVeiculo).orElseThrow(() -> new VeiculoNaoEncontradoException(idVeiculo));
		veiculoRepository.delete(veiculo);
	}
}
