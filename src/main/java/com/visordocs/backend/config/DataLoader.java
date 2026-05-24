package com.visordocs.backend.config;


import com.visordocs.backend.model.Usuario;
import com.visordocs.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByUsuario("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsuario("admin");
            admin.setPassword("admin123");
            admin.setRol("ADMIN");
            usuarioRepository.save(admin);
            System.out.println("✅ Usuario admin creado: admin / admin123");
        }
    }
}
