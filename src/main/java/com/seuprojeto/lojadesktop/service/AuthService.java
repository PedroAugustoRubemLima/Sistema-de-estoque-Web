package com.seuprojeto.lojadesktop.service;

import com.seuprojeto.lojadesktop.util.CryptoUtil;
import com.seuprojeto.lojadesktop.util.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${app.auth.username}")
    private String encryptedUser;

    @Value("${app.auth.password}")
    private String encryptedPassword;

    public boolean autenticar(String usuario, String senha) {

        String userDecrypt = CryptoUtil.decrypt(encryptedUser);
        String passDecrypt = CryptoUtil.decrypt(encryptedPassword);

        String senhaHash = HashUtil.hashSenha(senha);

        return userDecrypt.equals(usuario)
                && passDecrypt.equals(senhaHash);
    }
}
