package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.service.LibroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping("/libros")
    public String listar(Model model) {
        model.addAttribute("libros", libroService.listar());
        return "libros";
    }

    @GetMapping("/libros/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("libro", libroService.buscarPorId(id).orElse(null));
        return "libro-detalle";
    }

    // ==========================================================
    // CASO PRACTICO 2 - REQUISITO 2:
    // Aca vas a agregar las rutas de PrestamoController (o un
    // controller nuevo PrestamoController.java) para registrar
    // prestamos y devoluciones, protegidas con @PreAuthorize
    // segun el Requisito 3.
    // ========================================================
    // ==
    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).BIBLIOTECARIO.name())")
    @GetMapping("/libros/nuevo")
    public String nuevo(Model model) {

        model.addAttribute("libro", new Libro());

        return "libro-form";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).BIBLIOTECARIO.name())")
    @PostMapping("/libros/guardar")
    public String guardar(@ModelAttribute Libro libro) {

        libroService.guardar(libro);

        return "redirect:/libros";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).BIBLIOTECARIO.name())")
    @GetMapping("/libros/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {

        Libro libro = libroService.buscarPorId(id).orElse(null);

        if (libro == null) {
            return "redirect:/libros";
        }

        model.addAttribute("libro", libro);

        return "libro-form";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.entity.Rol).BIBLIOTECARIO.name())")
    @PostMapping("/libros/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {

        libroService.eliminar(id);

        return "redirect:/libros";
    }
}
