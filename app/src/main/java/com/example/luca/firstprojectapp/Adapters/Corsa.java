package com.example.luca.firstprojectapp.Adapters;

/**
 * Created by MatteoOrzes on 27/05/2015.
 */
public class Corsa {
    private static final String DISTANZA = "distanza";
    private final static String CALORIE = "calorie";
    private final static String ORARIO = "orario";
    private final static String DURATA = "durata";

    private String distanza;
    private String calorie;
    private String orario;
    private String durata;

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getDistanza() {
        return distanza;
    }

    public void setDistanza(String distanza) {
        this.distanza = distanza;
    }

    public Corsa(Long IdCorsa) throws NullPointerException{
        if(IdCorsa == null){
            throw new NullPointerException("IdCorsa non deve essere un valore nullo");
        }

        //Prendo le varie informazioni dal database usando IdCorsa come ricerca;

        //Setto le varie info.


    }


}
