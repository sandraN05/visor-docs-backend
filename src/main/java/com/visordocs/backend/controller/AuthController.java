package com.visordocs.backend.controller;


import com.visordocs.backend.model.Usuario;
import com.visordocs.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String usuario  = credenciales.get("usuario");
        String password = credenciales.get("password");

        if (usuario == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "Usuario y contraseña son requeridos."));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(usuario);

        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPassword().equals(password)) {
            return ResponseEntity.status(401)
                    .body(Map.of("mensaje", "Usuario o contraseña incorrectos."));
        }

        Usuario u = usuarioOpt.get();
        return ResponseEntity.ok(Map.of(
                "mensaje", "Login exitoso",
                "usuario", u.getUsuario(),
                "rol",     u.getRol()
        ));
    }
}