import java.util.Date;

public class Programare {
    private String numePac;
    private String prenumePac;
    private String numeMed;
    private String prenumeMed;
    private Date data;
    private Integer pret;

    private  Integer id_medic;
    private  Integer id_pacient;
    private   Integer id_programare;

    public String getNumePac() {
        return numePac;
    }

    public void setNumePac(String numePac) {
        this.numePac = numePac;
    }

    public String getPrenumePac() {
        return prenumePac;
    }

    public void setPrenumePac(String prenumePac) {
        this.prenumePac = prenumePac;
    }

    public String getNumeMed() {
        return numeMed;
    }

    public void setNumeMed(String numeMed) {
        this.numeMed = numeMed;
    }

    public String getPrenumeMed() {
        return prenumeMed;
    }

    public void setPrenumeMed(String prenumeMed) {
        this.prenumeMed = prenumeMed;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getPret() {
        return pret;
    }

    public void setPret(Integer pret) {
        this.pret = pret;
    }

    public void setId_medic(Integer id_medic) {
        this.id_medic = id_medic;
    }

    public void setId_pacient(Integer id_pacient) {
        this.id_pacient = id_pacient;
    }

    public void setId_programare(Integer id_programare) {
        this.id_programare = id_programare;
    }

    public Integer getId_medic() {
        return id_medic;
    }

    public Integer getId_pacient() {
        return id_pacient;
    }

    public Integer getId_programare() {
        return id_programare;
    }

    @Override
    public String toString() {
        return
                numePac  + " "+
                 prenumePac + " " +
                 numeMed + " " +
                prenumeMed + " " +
               data + " " +
               pret
                ;
    }
}
