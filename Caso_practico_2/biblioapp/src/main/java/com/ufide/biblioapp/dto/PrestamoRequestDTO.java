package com.ufide.biblioapp.dto;

import jakarta.validation.constraints.NotNull;

public class PrestamoRequestDTO {

    @NotNull(message = "El libro es obligatorio")
    private Long libroId;

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    public PrestamoRequestDTO() {
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}