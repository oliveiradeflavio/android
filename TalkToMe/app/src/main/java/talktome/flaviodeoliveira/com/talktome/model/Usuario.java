package talktome.flaviodeoliveira.com.talktome.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import talktome.flaviodeoliveira.com.talktome.helper.ConfiguracaoFirebase;

/**
 * Created by flaviooliveira on 24/04/17.
 */
public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;

    public Usuario(){

    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child(getId()).setValue(this);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
