package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.service.LibroService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroRestController {

    @Autowired
    private LibroService libroService;

    // GET /api/libros
    // Catalogo publico en formato JSON
    @GetMapping
    public ResponseEntity<List<Libro>> listar() {

        return ResponseEntity.ok(
                libroService.listar()
        );
    }

    // GET /api/libros/{id}
    // Devuelve 200 si existe o 404 si no existe
    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Long id) {

        return libroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/libros
    // Solo el bibliotecario puede crear libros
    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).BIBLIOTECARIO.name())")
    @PostMapping
    public ResponseEntity<Libro> crear(
            @Valid @RequestBody Libro libro) {

        // Evita que un POST con id intente modificar un libro existente
        libro.setId(null);

        Libro libroGuardado = libroService.guardar(libro);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(libroGuardado);
    }
}