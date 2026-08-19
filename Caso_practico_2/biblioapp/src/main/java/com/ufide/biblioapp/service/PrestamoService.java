package com.ufide.biblioapp.service;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.repository.LibroRepository;
import com.ufide.biblioapp.repository.PrestamoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroRepository libroRepository;

    // Listar todos los prestamos
    public List<Prestamo> listarTodos() {
        return prestamoRepository.listarConRelaciones();
    }

    // Listar solamente los prestamos de un usuario
    public List<Prestamo> listarPorUsuario(Usuario usuario) {
        return prestamoRepository.findByUsuario(usuario);
    }

    // Buscar prestamo por ID
    public Prestamo buscarPorId(Long id) {
        return prestamoRepository.findById(id).orElse(null);
    }

    // Registrar un nuevo prestamo
    @Transactional
    public Prestamo registrarPrestamo(Prestamo prestamo) {

        Libro libro = prestamo.getLibro();

        // Validar que se haya seleccionado un libro
        if (libro == null) {
            throw new IllegalArgumentException("Debe seleccionar un libro");
        }

        // Validar que existan copias disponibles
        if (libro.getCopiasDisponibles() <= 0) {
            throw new IllegalStateException(
                    "No hay copias disponibles de este libro"
            );
        }

        // Fecha en que se realiza el prestamo
        LocalDate hoy = LocalDate.now();

        prestamo.setFechaPrestamo(hoy);

        // El prestamo tiene un plazo de 14 dias
        prestamo.setFechaLimite(hoy.plusDays(14));

        // Al crearse, todavia no ha sido devuelto
        prestamo.setFechaDevolucion(null);

        // Descontar una copia disponible
        libro.setCopiasDisponibles(
                libro.getCopiasDisponibles() - 1
        );

        // Guardar el libro actualizado
        libroRepository.save(libro);

        // Guardar el prestamo
        return prestamoRepository.save(prestamo);
    }

    // Registrar la devolucion de un libro
    @Transactional
    public Prestamo registrarDevolucion(Long id) {

        Prestamo prestamo = buscarPorId(id);

        // Validar que el prestamo exista
        if (prestamo == null) {
            throw new IllegalArgumentException(
                    "Prestamo no encontrado"
            );
        }

        // Evitar registrar dos veces la devolucion
        if (prestamo.getFechaDevolucion() != null) {
            throw new IllegalStateException(
                    "Este prestamo ya fue devuelto"
            );
        }

        // Registrar fecha de devolucion
        prestamo.setFechaDevolucion(LocalDate.now());

        Libro libro = prestamo.getLibro();

        // Devolver la copia al inventario
        libro.setCopiasDisponibles(
                libro.getCopiasDisponibles() + 1
        );

        // Guardar libro actualizado
        libroRepository.save(libro);

        // Guardar prestamo actualizado
        return prestamoRepository.save(prestamo);
    }
}