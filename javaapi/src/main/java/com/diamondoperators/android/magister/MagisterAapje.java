package com.diamondoperators.android.magister;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Appointment;
import net.ilexiconn.magister.container.School;
import net.ilexiconn.magister.handler.GradeHandler;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;

/**
 * To test the Magister.java api at https://github.com/iLexiconn/Magister.java
 */
public class MagisterAapje {
    public static void main(String[] args) {
        School school = School.findSchool("pantarijn")[0];
        System.out.println(school.name);

        try {
            Magister magister = Magister.login(school, Aapje.USERNAME, Aapje.PASSWORD);
            GradeHandler gradeHandler = magister.getHandler(GradeHandler.class);
            System.out.println(gradeHandler.getAllGrades());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create a new {@link Magister} instance by logging in. Will return null if login fails.
     *
     * @param school   the {@link School} instance. Can't be null.
     * @param username the username of the profile. Can't be null.
     * @param password the password of the profile. Can't be null.
     * @return the new {@link Magister} instance, null if login fails.
     * @throws IOException               if there is no active internet connection.
     * @throws ParseException            if parsing the date fails.
     * @throws InvalidParameterException if one of the arguments is null.
     *//*
    public static Magister login(School school, String username, String password) throws IOException, ParseException, InvalidParameterException {
        if (school == null || username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidParameterException("Parameters can't be null or empty!");
        }
        Magister magister = new Magister();
        AndroidUtil.checkAndroid();
        magister.school = school;
        SchoolUrl url = magister.schoolUrl = new SchoolUrl(school);
        magister.version = magister.gson.fromJson(HttpUtil.httpGet(url.getVersionUrl()), Version.class);
        magister.user = new User(username, password, true);
        magister.logout();
        Map<String, String> nameValuePairMap = magister.gson.fromJson(magister.gson.toJson(magister.user), new TypeToken<Map<String, String>>() {
        }.getType());
        magister.session = magister.gson.fromJson(HttpUtil.httpPost(url.getSessionUrl(), nameValuePairMap), Session.class);
        if (!magister.session.state.equals("active")) {
            LogUtil.printError("Invalid credentials", new InvalidParameterException());
            return null;
        }
        magister.loginTime = System.currentTimeMillis();
        magister.profile = magister.gson.fromJson(HttpUtil.httpGet(url.getAccountUrl()), Profile.class);
        magister.studies = magister.gson.fromJson(HttpUtil.httpGet(url.getStudiesUrl(magister.profile.id)), Study[].class);
        DateFormat format = new SimpleDateFormat("y-m-d", Locale.ENGLISH);
        Date now = new Date();
        for (Study study : magister.studies) {
            if (format.parse(study.endDate.substring(0, 10)).after(now)) {
                magister.currentStudy = study;
            }
        }
        if (magister.currentStudy != null) {
            magister.subjects = magister.getSubjectsOfStudy(magister.currentStudy);
        }
        return magister;
    }*/
}
