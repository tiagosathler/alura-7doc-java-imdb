package br.com.sathler.model.records;

import java.io.Serializable;

public record Movie(String title, String urlImage, Integer year, Double rating) implements Serializable {

}
