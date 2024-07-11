package com.example.final_am_acn4b_baez_raimondi;

public class DataModel {

    private String name, surname, fecha, hora;

    public DataModel(String name, String surname, String fecha, String hora) {
        this.name = name;
        this.surname = surname;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }
}
