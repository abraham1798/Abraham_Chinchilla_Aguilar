package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.dto.PrestamoRequestDTO;
import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.service.LibroService;
import com.ufide.biblioapp.service.PrestamoService;
import com.ufide.biblioapp.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoRestController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private UsuarioService usuarioService;

    // Devuelve los prestamos que se encuentran atrasados
    @GetMapping("/atrasados")
    public ResponseEntity<List<Prestamo>> atrasados() {

        List<Prestamo> prestamos =
                prestamoService.listarAtrasados();

        return ResponseEntity.ok(prestamos);
    }

    // Bonus: registrar un prestamo desde la API
    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).BIBLIOTECARIO.name())")
    @PostMapping
    public ResponseEntity<?> registrarPrestamo(
            @Valid @RequestBody PrestamoRequestDTO datos) {

        Libro libro = libroService
                .buscarPorId(datos.getLibroId())
                .orElse(null);

        Usuario usuario = usuarioService
                .buscarPorId(datos.getUsuarioId());

        if (libro == null || usuario == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Libro o usuario no encontrado");
        }

        Prestamo prestamo = new Prestamo();

        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);

        try {

            Prestamo guardado =
                    prestamoService.registrarPrestamo(prestamo);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(guardado);

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}