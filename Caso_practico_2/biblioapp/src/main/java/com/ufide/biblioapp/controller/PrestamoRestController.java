package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.service.PrestamoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoRestController {

    @Autowired
    private PrestamoService prestamoService;

    // Devuelve los prestamos que se encuentran atrasados
    @GetMapping("/atrasados")
    public ResponseEntity<List<Prestamo>> atrasados() {

        List<Prestamo> prestamos = prestamoService.listarAtrasados();

        return ResponseEntity.ok(prestamos);
    }
}