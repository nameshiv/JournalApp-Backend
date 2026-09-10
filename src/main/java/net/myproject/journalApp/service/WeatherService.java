package net.myproject.journalApp.service;

import net.myproject.journalApp.api.response.WeatherResponse;
import net.myproject.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    //      added in db
//    private static final String API = "https://api.openweathermap.org/data/2.5/weather?q=CITY&appid=API_KEY&units=metric";


    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private RedisService redisService;

    @Autowired
    private AppCache appCache;

    //without redis

    public WeatherResponse getWeather(String city) {

        String finalAPI = appCache.APP_CACHE
                .get("weather_api")
                .replace("<city>", city)
                .replace("<apiKey>", apiKey);

        ResponseEntity<WeatherResponse> response =
                restTemplate.exchange(
                        finalAPI,
                        HttpMethod.GET,
                        null,
                        WeatherResponse.class
                );

        return response.getBody();
    }


    //using redis

//    public WeatherResponse getWeather(String city){
//        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
//        if(weatherResponse != null){
//            return weatherResponse;
//        }else{
//            String finalAPI = appCache.APP_CACHE.get("weather_api").replace("<city>", city).replace("<apiKey>", apiKey);

//            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
//            WeatherResponse body = response.getBody();
//            if(body != null){
//                redisService.set("weather_of_" + city, body, 300l); //5 minutes -> 300 seconds (long)
//            }
//
//            return body;
//        }
}