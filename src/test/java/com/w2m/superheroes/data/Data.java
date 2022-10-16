package com.w2m.superheroes.data;

import com.w2m.superheroes.models.SuperHero;

import java.util.Arrays;
import java.util.List;

public class Data {

    public static SuperHero SPIDERMAN() {
        return new SuperHero(1L, "Spiderman");
    }

    public static SuperHero SUPERMAN() {
        return new SuperHero(2L, "Superman");
    }

    public static SuperHero MANOLITO() {
        return new SuperHero(3L, "Manolito el fuerte");
    }

    public final static List<SuperHero> SUPER_HEROES() {
        return Arrays.asList(SPIDERMAN(), SUPERMAN(), MANOLITO());
    }

}
