package com.visordocs.backend.controller;

import com.visordocs.backend.model.Documento;
import com.visordocs.backend.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/api/documentos")
@CrossOrigin(origins = "*")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @GetMapping
    public ResponseEntity<List<Documento>> listar() {
        return ResponseEntity.ok(documentoService.listarTodos());
    }

    @PostMapping("/subir")
    public ResponseEntity<?> subir(
            @RequestParam("titulo")      String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("archivo")     MultipartFile archivo
    ) {
        if (!Objects.requireNonNull(archivo.getContentType()).equals("application/pdf")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "Solo se permiten archivos PDF."));
        }
        try {
            Documento doc = documentoService.subirDocumento(titulo, descripcion, archivo);
            return ResponseEntity.ok(doc);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error al guardar el archivo."));
        }
    }

    @GetMapping("/archivo/{nombre}")
    public ResponseEntity<Resource> verArchivo(@PathVariable String nombre) {
        try {
            Path ruta = documentoService.obtenerRutaArchivo(nombre);
            Resource resource = new UrlResource(ruta.toUri());
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombre + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            documentoService.eliminarDocumento(id);
            return ResponseEntity.ok(Map.of("mensaje", "Documento eliminado correctamente."));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error al eliminar el archivo."));
        }
    }
}