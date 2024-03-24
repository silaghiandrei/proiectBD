public class Medic {
    private String postdidactic;
    private String grad;
    private String codparafa;
    private String titlustintiific;
    private Integer procentAditional;
    private Integer angajat_idangajat;
    private Integer idspecialitate;

    private String nume;
    private String prenume;



    public String getPostdidactic() {
        return postdidactic;
    }

    public String getGrad() {
        return grad;
    }

    public String getCodparafa() {
        return codparafa;
    }

    public String getTitlustintiific() {
        return titlustintiific;
    }

    public Integer getProcentaditional() {
        return procentAditional;
    }

    public Integer getAngajat_idangajat() {
        return angajat_idangajat;
    }

    public Integer getIdspecialitate() {
        return idspecialitate;
    }

    public void setAngajat_idangajat(Integer angajat_idangajat) {
        this.angajat_idangajat = angajat_idangajat;
    }

    public void setIdspecialitate(Integer idspecialitate) {
        this.idspecialitate = idspecialitate;
    }

    public void setPostdidactic(String postdidactic) {
        this.postdidactic = postdidactic;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public void setCodparafa(String codparafa) {
        this.codparafa = codparafa;
    }

    public void setTitlustintiific(String titlustintiific) {
        this.titlustintiific = titlustintiific;
    }

    public void setProcentaditional(Integer procentaditional) {
        this.procentAditional = procentaditional;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    @Override
    public String toString() {
        return nume + " " + prenume;
    }
}
