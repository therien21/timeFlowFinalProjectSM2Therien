package com.mycompany.timeflo.service;

public class GoogleCalendarService {

    private String accessToken;
    private boolean connected = false;

    public boolean connect(String simulatedToken) {
        if (simulatedToken != null && !simulatedToken.trim().isEmpty()) {
            this.accessToken = simulatedToken.trim();
            this.connected = true;
            return true;
        }
        return false;
    }

    public boolean isConnected() {
        return connected;
    }

    public java.util.List<CalendarEvent> fetchTodaysEvents(String apiKey) {
        java.util.List<CalendarEvent> events = new java.util.ArrayList<>();
        if (!connected) return events;

        // Real API call would be:
        // GET https://www.googleapis.com/calendar/v3/calendars/primary/events
        //     ?key={apiKey}&timeMin=today&maxResults=10

        // Simulated for now:
        events.add(new CalendarEvent("9:00",  "BUS 370 Lecture",   "School"));
        events.add(new CalendarEvent("12:00", "Lunch with team",   "Meal"));
        events.add(new CalendarEvent("3:00",  "Hockey Practice",   "Workout"));
        events.add(new CalendarEvent("6:00",  "Study for CSC 261", "School"));
        return events;
}

    public static class CalendarEvent {
        public final String time;
        public final String title;
        public final String type;

        public CalendarEvent(String time, String title, String type) {
            this.time  = time;
            this.title = title;
            this.type  = type;
        }

        @Override
        public String toString() {
            return time + " — " + title + " (" + type + ")";
        }
    }
}