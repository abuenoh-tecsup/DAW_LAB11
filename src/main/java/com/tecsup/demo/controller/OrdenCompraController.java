package com.tecsup.demo.controller;

import com.tecsup.demo.dto.OrdenCompraRequestDTO;
import com.tecsup.demo.model.Laboratorio;
import com.tecsup.demo.model.OrdenCompra;
import com.tecsup.demo.repository.LaboratorioRepository;
import com.tecsup.demo.repository.OrdenCompraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordencompra")
public class OrdenCompraController {
    @Autowired
    private OrdenCompraRepository repo;
    @Autowired
    private LaboratorioRepository repoLab;

    @GetMapping
    public List<OrdenCompra> obtenerOrdenCompra() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<OrdenCompra> obtenerOrdenCompra(@PathVariable Long id) {
        return repo.findById(id);
    }

    @PostMapping
    public OrdenCompra guardarOrdenCompra(@RequestBody OrdenCompraRequestDTO request) {
        Optional<Laboratorio> laboratorioOpt = repoLab.findById(request.getCodLab());
        if (laboratorioOpt.isEmpty()) {
            throw new RuntimeException("Laboratorio no encontrado");
        }

        OrdenCompra ordenCompra = new OrdenCompra();
        ordenCompra.setFechaEmision(request.getFechaEmision());
        ordenCompra.setSituacion(request.getSituacion());
        ordenCompra.setTotal(request.getTotal());
        ordenCompra.setNroFacturaProv(request.getNroFacturaProv());
        ordenCompra.setLaboratorio(laboratorioOpt.get());
        return repo.save(ordenCompra);
    }

    @PutMapping("/{id}")
    public OrdenCompra actualizarOrdenCompra(@RequestBody OrdenCompraRequestDTO request, @PathVariable Long id) {
        Optional<Laboratorio> laboratorioOpt = repoLab.findById(request.getCodLab());
        if (laboratorioOpt.isEmpty()) {
            throw new RuntimeException("Laboratorio no encontrado");
        }

        OrdenCompra ordenCompra = new OrdenCompra();
        ordenCompra.setNroOrdenC(id);
        ordenCompra.setFechaEmision(request.getFechaEmision());
        ordenCompra.setSituacion(request.getSituacion());
        ordenCompra.setTotal(request.getTotal());
        ordenCompra.setNroFacturaProv(request.getNroFacturaProv());
        ordenCompra.setLaboratorio(laboratorioOpt.get());
        return repo.save(ordenCompra);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrdenCompra(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);

        boolean existe = repo.existsById(id);
        System.out.println("¿Todavía existe después de borrar? " + existe);

        return ResponseEntity.noContent().build();
    }

}
