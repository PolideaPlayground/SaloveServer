package com.polidea.salove.infrastructure;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static java.util.Arrays.asList;

public class GoogleRegistration {

  private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

  private static final File DATA_STORE_DIR = new File("google-credentials.json");

  private static FileDataStoreFactory DATA_STORE_FACTORY;

  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static HttpTransport HTTP_TRANSPORT;

  private static final List<String> SCOPES = asList(CalendarScopes.CALENDAR);

  static {
    try {
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

  private static Credential authorize() throws IOException {
    // Load client secrets.
    InputStream in = GoogleRegistration.class.getResourceAsStream("/google-credentials.json");
    GoogleClientSecrets clientSecrets = GoogleClientSecrets
        .load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
        JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY)
        .setAccessType("offline").build();
    Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
        .authorize("user");
    System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
    return credential;
  }

  public static Calendar getCalendarService() throws IOException {
    Credential credential = authorize();
    return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME).build();
  }
}
