package com.fooengineers.projetoAcVansV4.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fooengineers.projetoAcVansV4.dto.ServicoReqDTO;
import com.fooengineers.projetoAcVansV4.dto.ServicoResDTO;
import com.fooengineers.projetoAcVansV4.entity.Servico;
import com.fooengineers.projetoAcVansV4.exception.ServicoNaoEncontradoException;
import com.fooengineers.projetoAcVansV4.repository.ServicoRepository;
import com.fooengineers.projetoAcVansV4.util.QrCodeUtil;
import com.google.zxing.WriterException;

@Service
public class ServicoService {
	@Autowired
	private ServicoRepository servicoRepository;
	@Value("${app.base-url}")
	private String baseUrl;

	public Servico buscar(String token, String placa) {
		Servico s = servicoRepository.findByTokenConsulta(token).orElseThrow(() -> new ServicoNaoEncontradoException());
		if(placa.equals(s.getVeiculo().getPlaca())) {
			return s;
		}else{
			return null;
		}
	}
	
	public Servico buscar(String tokenLongo) {
		return servicoRepository.findByTokenAtualizacao(tokenLongo).orElseThrow(() -> new ServicoNaoEncontradoException());
	}
	
	public byte[] gerarQrCode(Long idServico) throws WriterException, IOException {
		Servico s = servicoRepository.findById(idServico).orElseThrow(() -> new ServicoNaoEncontradoException());
		
		String url = baseUrl + "/servicos/atualizar-status?token=" + s.getTokenAtualizacao();
		return QrCodeUtil.gerarQrCode(url);
	}
}
