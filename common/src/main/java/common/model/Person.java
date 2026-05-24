package common.model;

import common.enums.Country;
import common.enums.EyeColor;
import common.enums.HairColor;

import java.io.Serializable;

public class Person implements Serializable {
    private Long height; // может быть null
    private EyeColor eyeColor;
    private HairColor hairColor;
    private Country nationality;

    public Person(Long height, EyeColor eyeColor, HairColor hairColor, Country nationality) {
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    public Long getHeight() {
        return height;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    @Override
    public String toString() {
        return "Person{" +
                "height=" + height +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                '}';
    }
}