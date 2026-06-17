package com.cefet.controller;

import java.util.List;
import com.cefet.entity.Reserva;
import com.cefet.service.ReservaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reserva> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable Long id) {
        try {
            Reserva reserva = service.buscarPorId(id);
            return ResponseEntity.ok(reserva);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Reserva reserva) {
        try {
            return ResponseEntity.ok(service.salvar(reserva));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Reserva reserva) {
        try {
            service.buscarPorId(id);
            reserva.setId(id);
            return ResponseEntity.ok(service.salvar(reserva));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Reserva reservaAtualizada) {
        try {
            Reserva reserva = service.buscarPorId(id);
            if (reservaAtualizada.getPassageiro() != null) reserva.setPassageiro(reservaAtualizada.getPassageiro());
            if (reservaAtualizada.getAcompanhante() != null) reserva.setAcompanhante(reservaAtualizada.getAcompanhante());
            if (reservaAtualizada.getCidadeOrigem() != null) reserva.setCidadeOrigem(reservaAtualizada.getCidadeOrigem());
            if (reservaAtualizada.getCidadeDestino() != null) reserva.setCidadeDestino(reservaAtualizada.getCidadeDestino());
            if (reservaAtualizada.getModal() != null) reserva.setModal(reservaAtualizada.getModal());
            if (reservaAtualizada.getDataReserva() != null) reserva.setDataReserva(reservaAtualizada.getDataReserva());
            if (reservaAtualizada.getStatus() != null) reserva.setStatus(reservaAtualizada.getStatus());
            reserva.setVendaOnline(reservaAtualizada.isVendaOnline());
            if (reservaAtualizada.getTipoVenda() != null) reserva.setTipoVenda(reservaAtualizada.getTipoVenda());
            return ResponseEntity.ok(service.salvar(reserva));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
