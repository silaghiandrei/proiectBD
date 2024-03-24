import java.sql.Date;

public class Angajat {
    public Integer idangajat;
    public String cnp;
    public String nume;
    public String prenume;
    public String adresa;
    public String email;
    public String telefon;
    public String iban;
    public Date dataAngajarii;
    public String functia;
    public Integer salariu;

    public Integer nrOre;

    private Integer procentAditional;

    public void setProcentAditional(Integer procentAditional) {
        this.procentAditional = procentAditional;
    }


    public String getNume(){
        return nume;
    }

    public Integer getIdangajat() {
        return idangajat;
    }

    public String getCnp() {
        return cnp;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getIban() {
        return iban;
    }

    public Date getDataAngajarii() {
        return dataAngajarii;
    }

    public String getFunctia() {
        return functia;
    }

    public Integer getSalariu() {
        return salariu;
    }

    public void setIdangajat(Integer idangajat) {
        this.idangajat = idangajat;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setDataAngajarii(Date dataAngajarii) {
        this.dataAngajarii = dataAngajarii;
    }

    public void setFunctia(String functia) {
        this.functia = functia;
    }

    public void setSalariu(Integer salariu) {
        this.salariu = salariu;
    }

    public Integer getNrOre() {
        return nrOre;
    }

    public void setNrOre(Integer nrOre) {
        this.nrOre = nrOre;
    }

    @Override
    public String toString() {
        return nume + " " + prenume;
    }
}
