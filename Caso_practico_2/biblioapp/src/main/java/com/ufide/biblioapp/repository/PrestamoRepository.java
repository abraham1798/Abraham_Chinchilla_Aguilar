package com.ufide.biblioapp.repository;

import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuario(Usuario usuario);

    @Query("""
        SELECT p
        FROM Prestamo p
        JOIN FETCH p.libro
        JOIN FETCH p.usuario
        """)
    List<Prestamo> listarConRelaciones();

    // Busca los prestamos que no han sido devueltos
    // y cuya fecha limite ya expiro.
    @Query("SELECT p FROM Prestamo p " +
           "WHERE p.fechaDevolucion IS NULL " +
           "AND p.fechaLimite < CURRENT_DATE")
    List<Prestamo> prestamosAtrasados();
}