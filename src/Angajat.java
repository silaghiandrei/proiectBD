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
}
