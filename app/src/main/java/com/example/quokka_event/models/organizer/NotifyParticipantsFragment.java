package com.example.quokka_event.models.organizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.quokka_event.R;

public class NotifyParticipantsFragment extends DialogFragment {
    TextView eventName;
    ImageButton cancelButton;
    Button sendButton;
    EditText notificationText;
    Spinner recipientSpinner;

    /**
     * @author mylayambao
     * @since project part 4
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(com.example.quokka_event.R.layout.organizer_notify_participants_frag, container, false);
        eventName = view.findViewById(com.example.quokka_event.R.id.event_title_label);
        cancelButton = view.findViewById(R.id.cancel_notify_button);
        sendButton = view.findViewById(R.id.send_button);
        notificationText = view.findViewById(R.id.notification_text);
        recipientSpinner = view.findViewById(R.id.recipient_spinner);

        // spinner setup
        String[] participantStatuses = new String[]{"All","Accepted", "Cancelled", "Waitlisted", "Awaiting Response"};
        ArrayAdapter<String> participantStatusAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, participantStatuses);
        participantStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        recipientSpinner.setAdapter(participantStatusAdapter);


         return  view;
    }
}
