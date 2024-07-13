package com.example.final_am_acn4b_baez_raimondi;

public class DataModel {

    private String name, surname, fecha, hora;
    private Integer id;

    public DataModel(String name, String surname, String fecha, String hora, Integer id) {
        this.name = name;
        this.surname = surname;
        this.fecha = fecha;
        this.hora = hora;
        this.id = id;
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

    public Integer getId() {
        return id;
    }
}
