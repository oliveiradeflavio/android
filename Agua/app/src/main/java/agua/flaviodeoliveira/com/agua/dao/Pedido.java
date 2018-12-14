package agua.flaviodeoliveira.com.agua.dao;

/**
 * Created by flaviooliveira on 05/08/17.
 */

public class Pedido {

    private String uuid;
    private String nome;
    private String endereco;
    private String telefone;
    private String celular;
    private String garrafapct;
    private String garrafao10;
    private String garrafao20;
    private String troco;
    private String totalgeral;
    private String dataehorario;
    private String status;

    public Pedido(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getGarrafapct() {
        return garrafapct;
    }

    public void setGarrafapct(String garrafapct) {
        this.garrafapct = garrafapct;
    }

    public String getGarrafao10() {
        return garrafao10;
    }

    public void setGarrafao10(String garrafao10) {
        this.garrafao10 = garrafao10;
    }

    public String getGarrafao20() {
        return garrafao20;
    }

    public void setGarrafao20(String garrafao20) {
        this.garrafao20 = garrafao20;
    }

    public String getTroco() {
        return troco;
    }

    public void setTroco(String troco) {
        this.troco = troco;
    }

    public String getTotalgeral() {
        return totalgeral;
    }

    public void setTotalgeral(String totalgeral) {
        this.totalgeral = totalgeral;
    }

    public String getDataehorario() {
        return dataehorario;
    }

    public void setDataehorario(String dataehorario) {
        this.dataehorario = dataehorario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
