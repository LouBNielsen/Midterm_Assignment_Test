/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.mockito.Mock;
import testex.IDateFormatter;
import testex.Joke;
import testex.JokeException;
import testex.JokeFetcher;
import testex.Jokes;
import testex.jokefetching.ChuckNorris;
import testex.jokefetching.EduJoke;
import testex.jokefetching.IFetcherFactory;
import testex.jokefetching.IJokeFetcher;
import testex.jokefetching.Moma;
import testex.jokefetching.Tambal;

/**
 *
 * @author louis
 */
@RunWith(MockitoJUnitRunner.class)
public class TestJokeFetcher {

    private JokeFetcher jokeFetcher;

    @Mock
    IDateFormatter iDateFormatter;

    @Mock
    IFetcherFactory iFetcherFactory;

    @Mock
    EduJoke eduJoke;

    @Mock
    ChuckNorris chuckNorris;

    @Mock
    Moma moma;

    @Mock
    Tambal tambal;

    @Mock
    List<IJokeFetcher> jokes;

    @Before
    public void before() {
        List<String> types = Arrays.asList("eduprog", "chucknorris", "moma", "tambal");
        when(iFetcherFactory.getAvailableTypes()).thenReturn(types);
        //Or
        when(iFetcherFactory.getAvailableTypes()).thenReturn(Arrays.asList("eduprog", "chucknorris", "moma", "tambal"));

        List<IJokeFetcher> fetchers = Arrays.asList(eduJoke, chuckNorris, moma, tambal);
        when(iFetcherFactory.getJokeFetchers("eduprog,chucknorris,moma,tambal")).thenReturn(fetchers);
        //Or
        when(iFetcherFactory.getJokeFetchers("eduprog,chucknorris,moma,tambal")).thenReturn(Arrays.asList(eduJoke, chuckNorris, moma, tambal));

        jokeFetcher = new JokeFetcher(iFetcherFactory, iDateFormatter); //Constructer kald
    }

    @Test
    public void testFactory() {
        List<IJokeFetcher> jokesList = iFetcherFactory.getJokeFetchers("eduprog,chucknorris,moma,tambal");

        assertAll("Checking each Joke fetcher", () -> {
            assertEquals(4, jokesList.size());
            assertThat(jokesList, hasSize(equalTo(4)));
            assertThat(jokesList.size(), is(4));
        });

        verify(iFetcherFactory).getJokeFetchers("eduprog,chucknorris,moma,tambal");
    }

    @Test
    public void testGetAvailableTypes() {
        List<String> jokesList = jokeFetcher.getAvailableTypes();

        assertAll("Checking jokes list", () -> {
            assertThat(jokesList, hasItems("eduprog", "chucknorris", "moma", "tambal"));
            assertEquals(4, jokesList.size());
            assertThat(jokesList, hasSize(equalTo(4)));
            assertThat(jokesList.size(), is(4));
        });
    }

    @Test
    public void testIsStringValid() {
        boolean legalString = jokeFetcher.isStringValid("eduprog,chucknorris,chucknorris,moma,tambal");
        boolean notLegalString = jokeFetcher.isStringValid("edu_prog,xxx");
        boolean expectedLegal = true;
        boolean expectedNotLegal = false;

        assertThat(legalString, is(expectedLegal));
        assertThat(notLegalString, is(expectedNotLegal));
    }

    @Test(expected = JokeException.class)
    public void testGetException() throws JokeException {
        jokeFetcher.getJokes("notajoke", "Europe/Copenhagen", iDateFormatter);
    }

    @Test
    public void testGetJokes() throws JokeException {
        String expectedDate = "17 feb. 2018 10:56 AM";
        String timeZone = "Europe/Copenhagen";
        String dateFormat = iDateFormatter.getFormattedDate(new Date(), timeZone);

        when(dateFormat).thenReturn(expectedDate);

        String jokesToFetch = "eduprog,chucknorris,moma,tambal";

        Jokes jokes = jokeFetcher.getJokes(jokesToFetch, timeZone, iDateFormatter);

        assertAll("Checking jokes", () -> {
            assertFalse(jokes.getJokes().isEmpty());
            verify(iFetcherFactory, times(1)).getJokeFetchers(any()); //IFethcerFactory og IDateFormatter kaldes i getJoke()
            verify(iDateFormatter, times(1)).getFormattedDate(any(), any()); //any() fordi vi kan ikke teste tid og zone
        });
        assertAll("Checking each joke", () -> {
            jokes.getJokes().forEach(joke -> {
                if (joke != null) {
                    assertFalse(joke.getJoke().isEmpty());
                    assertFalse(joke.getReference().isEmpty());
                }
            });
        });
    }

    @Test
    public void testGetJokes2() {
        String joke_eduJoke = "Teacher asked George: 'How can you prove the earth is round?' "
                + "George replied: 'I can’t. Besides, I never said it was.'";
        String joke_chuckNorris = "Chuck Norris can make a class that is both abstract and final.";
        String joke_moma = "Yo mamas so fat everytime she turns around its her birthday";
        String joke_tambal = "If you reverse tambal you get labmat, and if you swap places";

        when(eduJoke.getJoke()).thenReturn(new Joke(joke_eduJoke, "http://jokes-plaul.rhcloud.com/api/joke"));
        assertThat(eduJoke.getJoke().getReference(), equalTo("http://jokes-plaul.rhcloud.com/api/joke"));

        when(chuckNorris.getJoke()).thenReturn(new Joke(joke_chuckNorris, "http://api.icndb.com/jokes/random"));
        assertThat(chuckNorris.getJoke().getReference(), equalTo("http://api.icndb.com/jokes/random"));

        when(moma.getJoke()).thenReturn(new Joke(joke_moma, "http://api.yomomma.info/"));
        assertThat(moma.getJoke().getReference(), equalTo("http://api.yomomma.info/"));

        when(tambal.getJoke()).thenReturn(new Joke(joke_tambal, "http://tambal.azurewebsites.net/joke/random"));
        assertThat(tambal.getJoke().getReference(), equalTo("http://tambal.azurewebsites.net/joke/random"));
    }
}
