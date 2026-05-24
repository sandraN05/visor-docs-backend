package com.visordocs.backend.service;

import com.visordocs.backend.model.Documento;
import com.visordocs.backend.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public List<Documento> listarTodos() {
        return documentoRepository.findAllByOrderByFechaSubidaDesc();
    }

    public Documento subirDocumento(String titulo, String descripcion, MultipartFile archivo) throws IOException {
        Path carpeta = Paths.get(uploadDir);
        if (!Files.exists(carpeta)) {
            Files.createDirectories(carpeta);
        }
        String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaArchivo = carpeta.resolve(nombreUnico);
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

        Documento doc = new Documento();
        doc.setTitulo(titulo);
        doc.setDescripcion(descripcion);
        doc.setNombreArchivo(nombreUnico);
        doc.setUrlPdf("http://localhost:8080/api/documentos/archivo/" + nombreUnico);

        return documentoRepository.save(doc);
    }

    public void eliminarDocumento(Long id) throws IOException {
        Documento doc = documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        Path archivo = Paths.get(uploadDir).resolve(doc.getNombreArchivo());
        Files.deleteIfExists(archivo);
        documentoRepository.deleteById(id);
    }

    public Path obtenerRutaArchivo(String nombreArchivo) {
        return Paths.get(uploadDir).resolve(nombreArchivo);
    }
}