package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.service.LibroService;
import com.ufide.biblioapp.service.PrestamoService;
import com.ufide.biblioapp.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private UsuarioService usuarioService;

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).BIBLIOTECARIO.name())")

    // Mostrar todos los prestamos
    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "prestamos",
                prestamoService.listarTodos());

        return "prestamos/lista";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).BIBLIOTECARIO.name())")
    // Mostrar formulario para registrar un prestamo
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute(
                "prestamo",
                new Prestamo());

        model.addAttribute(
                "libros",
                libroService.listar());

        model.addAttribute(
                "usuarios",
                usuarioService.listar());

        return "prestamos/formulario";
    }

    // Registrar un nuevo prestamo
    @PostMapping("/guardar")
    public String guardar(
            @RequestParam Long libroId,
            @RequestParam Long usuarioId) {

        Optional<Libro> libroOptional = libroService.buscarPorId(libroId);
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        if (libroOptional.isEmpty() || usuario == null) {
            return "redirect:/prestamos/nuevo";
        }

        Prestamo prestamo = new Prestamo();

        prestamo.setLibro(libroOptional.get());
        prestamo.setUsuario(usuario);

        prestamoService.registrarPrestamo(prestamo);

        return "redirect:/prestamos";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).BIBLIOTECARIO.name())")
    // Registrar devolucion
    @PostMapping("/{id}/devolver")
    public String devolver(@PathVariable Long id) {

        prestamoService.registrarDevolucion(id);

        return "redirect:/prestamos";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).LECTOR.name())")
    @GetMapping("/mis")
    public String misPrestamos(Authentication auth, Model model) {

        Usuario usuario = usuarioService.buscarPorUsername(auth.getName());

        model.addAttribute(
                "prestamos",
                prestamoService.listarPorUsuario(usuario));

        return "prestamos/lista";
    }
}