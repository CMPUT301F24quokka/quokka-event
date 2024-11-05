package com.example.quokka_event.controllers;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quokka_event.R;
import com.example.quokka_event.models.event.EventAdapter;
import com.example.quokka_event.models.event.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Activity that displays a list of events in a RecyclerView.
 * Each event can be clicked to navigate to its details page.
 */
public class MyEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_my_events_page);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.event_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get list of events
        // Initialize adapter
        // Handle item click
        List<Event> eventList = getEventList();
        EventAdapter adapter = new EventAdapter(eventList, event -> {
            Intent intent = new Intent(this, EventDetailsActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private List<Event> getEventList() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("1", "Sample Event", new Date(2023 - 1900, 8, 21), new Date(), "Location", 100, 10, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        events.add(new Event("2", "Sample Event2", new Date(2024 - 1900, 9, 2), new Date(), "Location2", 100, 10, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        return events;
    }
}