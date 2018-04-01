package Test;

import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import testex.DateFormatter;
import testex.JokeException;

@RunWith(MockitoJUnitRunner.class)
public class TestDateFormatter {

    @Test
    public void testFormattedDate() throws Exception {
        DateFormatter dateformatter = new DateFormatter();
        String timeZone = "Europe/Copenhagen";
        String expResult = "01 jan. 1970 01:00 AM";
        String result = dateformatter.getFormattedDate(new Date(1), timeZone);
        assertEquals(expResult, result);
    }
    
    @Test(expected = JokeException.class)
    public void testInvalidFormattedDate() throws JokeException {
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.getFormattedDate(new Date(), "InvalidDate");
    }
}