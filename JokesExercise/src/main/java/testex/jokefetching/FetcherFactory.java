/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testex.jokefetching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author louis
 */
public class FetcherFactory implements IFetcherFactory {

    private final List<String> availableTypes = Arrays.asList("EduJoke", "ChuckNorris", "Moma", "Tambal");

    @Override
    public List<String> getAvailableTypes() {
        return availableTypes;
    }

    @Override
    public List<IJokeFetcher> getJokeFetchers(String jokesToFetch) {
        String[] tokens = jokesToFetch.split(",");
        List<IJokeFetcher> jokes = new ArrayList<>();
        for (String token : tokens) {
            switch (token) {
                case "eduprog":
                    jokes.add(new EduJoke());
                    break;
                case "chucknorris":
                    jokes.add(new ChuckNorris());
                    break;
                case "moma":
                    jokes.add(new Moma());
                    break;
                case "tambal":
                    jokes.add(new Tambal());
                    break;
                default:
            }
        }
        return jokes;
    }

}
