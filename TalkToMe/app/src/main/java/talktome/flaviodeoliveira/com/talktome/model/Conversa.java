package talktome.flaviodeoliveira.com.talktome.model;

/**
 * Created by flaviooliveira on 10/05/17.
 */
public class Conversa {

    private String idUsuario;
    private String nome;
    private String mensagem;


    public Conversa() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
