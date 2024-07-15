package com.example.demo.model;

public enum Idioma {
    en("en"),
    pt("pt"),
    es("es"),
    fr("fr");

    private String idiomaBook;

    Idioma (String idiomaBook) {
        this.idiomaBook = idiomaBook;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaBook.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Nenhum idioma encontrado para string fornecida: ");
    }

}
