package net.myproject.journalApp.api.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WeatherResponse {

    private Main main;
    private String name;

    @Getter
    @Setter
    public  class Main {
        private double temp;
        private double feels_like;
        private int humidity;
        private int pressure;
    }

}

