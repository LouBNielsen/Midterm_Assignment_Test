/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.javaanpr.test;

import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;

import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author louis
 */
@RunWith(value = Parameterized.class)
public class RecognitionAllIT {

    @Parameter(value = 0)
    public String data;

    @Parameters
    public static ArrayList<String> recognitionCars() throws ParserConfigurationException, SAXException, IOException {
        String resultsPath = "src/test/resources/results.properties";
        Properties properties;
        try (InputStream resultsStream = new FileInputStream(new File(resultsPath))) {
            properties = new Properties();
            properties.load(resultsStream);
        }
        assertThat(properties.size(), greaterThan(0));

        String snapshotDirPath = "src/test/resources/snapshots";
        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();
        assertThat(snapshots, not(nullValue()));

        assertThat(snapshots.length, greaterThan(0));

        Intelligence intel = new Intelligence();
        assertThat(intel, not(nullValue()));

        ArrayList<String> cars = new ArrayList();

        boolean correct;
        for (File snap : snapshots) {
            correct = false;
            CarSnapshot carSnap = new CarSnapshot(new FileInputStream(snap));
            assertThat("carSnap is null", carSnap, not(nullValue()));
            assertThat("carSnap.image is null", carSnap.getImage(), not(nullValue()));

            String snapName = snap.getName();
            String plateCorrect = properties.getProperty(snapName);
            assertThat(plateCorrect, not(nullValue()));

            String numberPlate = intel.recognize(carSnap, false);

            if (numberPlate != plateCorrect) {
                cars.add(intel.recognize(carSnap, false) + "," + properties.getProperty(snapName));
            } else {
                System.out.println("The plate recognition was correct");
            }
        }

        return cars;
    }

    @Test
    public void getIncorrectCarRecognition() {
        List<String> cars = Arrays.asList(data.split("\\s*,\\s*"));
        String expectedNumberPlate = cars.get(1);
        String actualNumberPlate = cars.get(0);

        assertThat(expectedNumberPlate, is(actualNumberPlate));
    }
}
