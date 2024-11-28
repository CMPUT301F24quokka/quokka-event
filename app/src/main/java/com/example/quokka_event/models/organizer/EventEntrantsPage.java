package com.example.quokka_event.models.organizer;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quokka_event.R;
import com.example.quokka_event.controllers.DatabaseManager;
import com.example.quokka_event.controllers.ViewPagerAdapter;
import com.example.quokka_event.controllers.ViewPagerAdapterEventEntrant;
import com.example.quokka_event.models.event.Event;
import com.example.quokka_event.models.event.EventWaitlistFragment;
import com.example.quokka_event.models.event.OverviewFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Date;

/**
 * Activity used when displaying the lists of different types of entrants for an event.
 * @author mylayambao
 * @since project part 4
 */

public class EventEntrantsPage extends AppCompatActivity implements EventWaitlistFragment.eventWaitlistListener, OverviewFragment.overviewEditListener {
    Button backButton;
    TextView eventName;
    Event event;
    private String eventId;
    private DatabaseManager db;

    /**
     * Sets up the activity when it is created.
     * @author mylayambo
     * @param savedInstanceState
     * @since project part 4
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("message","now displaying EventEntrantsActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_entrants_tabs);
        db = DatabaseManager.getInstance(this);

        eventId = getIntent().getStringExtra("eventId");

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        backButton = findViewById(R.id.back_button);

        ViewPagerAdapterEventEntrant adapter = new ViewPagerAdapterEventEntrant(this, eventId);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Attending");
                    break;
                case 1:
                    tab.setText("Invited");
                    break;
                case 2:
                    tab.setText("Waitlist");
                    break;
                case 3:
                    tab.setText("Cancelled");
            }
        }).attach();

        backButton.setOnClickListener(v -> finish());



    }

    @Override
    public void setEventName(String eventTitle) {}
    @Override
    public void setEventDate(Date eventDate) {}
    @Override
    public void setLocation(String location) {}
    @Override
    public void setDeadline(Date deadline) {}

    @Override
    public void setDescription(String description) {

    }
}