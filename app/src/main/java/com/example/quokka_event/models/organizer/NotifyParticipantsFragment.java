package com.example.quokka_event.models.organizer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.quokka_event.R;
import com.example.quokka_event.controllers.DatabaseManager;
import com.example.quokka_event.controllers.dbutil.DbCallback;
import com.example.quokka_event.models.Notification;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Map;

/**
 * This is a fragment that is displayed when an organizer clicks on the Notify Particpants button.
 * @author mylayambao
 * @since project part 4
 */
public class NotifyParticipantsFragment extends DialogFragment {
    TextView eventName;
    ImageButton cancelButton;
    Button sendButton;
    EditText notificationText;
    Spinner recipientSpinner;
    DatabaseManager db;
    FirebaseAuth auth;
    EditText notificationTitle;
    String currentEventId;


    /**
     * Factory method for the NotifyParticipantsFragment.
     * @author mylayambao
     * @param currentEventId
     * @return fragment
     */
    public static NotifyParticipantsFragment newInstance(String currentEventId) {
        NotifyParticipantsFragment fragment = new NotifyParticipantsFragment();
        Bundle args = new Bundle();
        args.putString("eventId", currentEventId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On create method for when a NotifyParticipantsFragment is created.
     * @author mylayambao
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the event ID from arguments
        if (getArguments() != null) {
            currentEventId = getArguments().getString("eventId");
        } else {
            Toast.makeText(getContext(), "Error: No event ID provided", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    /**
     * This sets up the notify participants fragment, when it is created.
     * @author mylayambao
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @since project part 4
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(com.example.quokka_event.R.layout.notify_participants_frag, container, false);
        eventName = view.findViewById(com.example.quokka_event.R.id.event_title_label);
        cancelButton = view.findViewById(R.id.cancel_notify_button);
        sendButton = view.findViewById(R.id.send_button);
        notificationText = view.findViewById(R.id.notification_text);
        recipientSpinner = view.findViewById(R.id.recipient_spinner);
        notificationTitle = view.findViewById(R.id.notification_title);
        db = DatabaseManager.getInstance(getContext());

        // spinner setup
        String[] participantStatuses = new String[]{"All","Attending", "Cancelled", "Waiting", "Invited"};
        ArrayAdapter<String> participantStatusAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, participantStatuses);
        participantStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        recipientSpinner.setAdapter(participantStatusAdapter);

        /**
         * On click listener for the cancel icon, to close the fragment when clicked.
         * @author mylayambao
         */
        cancelButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(this) // change later if we have time
                    .commit();
        });

        sendButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Gets the recipients and creates a notification when the send button is clicked.
             * @author mylayambao
             * @param view view
             */
            @Override
            public void onClick(View view) {
                String message = notificationText.getText().toString().trim();
                String title = notificationTitle.getText().toString().trim();

                // ensure there is a message
                if (message.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a notification message!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ensure there is a title
                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a notification title!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Notification notification = new Notification();

                // set the recipients
                int spinnerPos = recipientSpinner.getSelectedItemPosition();
                Log.d("spinner", String.valueOf(spinnerPos));
                ArrayList<Map<String, Object>> recipients = new ArrayList<>();
                if(spinnerPos == 1){
                    db.getAttendingEntrants(currentEventId, new DbCallback() {
                        /**
                         * If the list of  entrants is succesfully retrieved, send the notif to participants who opted in for notifications.
                         * @param result attending entrants
                         */
                        @Override
                        public void onSuccess(Object result) {
                            ArrayList<Map<String, Object>> participants = (ArrayList<Map<String, Object>>) result;
                            // filter participants based on "notifications" being true
                            for (Map<String, Object> participant : participants) {
                                Object notificationsEnabled = participant.get("notifications");
                                if (notificationsEnabled instanceof Boolean && (Boolean) notificationsEnabled) {
                                    // Add participant to recipients if notifications are enabled
                                    recipients.add(participant);
                                }
                            }
                            // dont create a notification with no recipents
                            if (recipients.isEmpty()) {
                                Toast.makeText(getContext(), "No entrants with notifications enabled", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            notification.setRecipients(recipients);
                            notification.setNotifMessage(notificationText.getText().toString());
                            notification.setNotifTitle(notificationTitle.getText().toString());
                            notification.setEventId(currentEventId);
                            //String deviceId = auth.getCurrentUser().getUid();
                            db.addNotification(notification, new DbCallback() {
                                /**
                                 * Creates the notification.
                                 * @param result notification
                                 */
                                @Override
                                public void onSuccess(Object result) {
                                    Log.d("DB", "Sent Notification: " + notification.getNotifTitle() + " to database");
                                    Toast.makeText(getContext(),
                                            "Notification Sent Successfully",
                                            Toast.LENGTH_SHORT).show();
                                }

                                /**
                                 * Indicates there was an error creating a notification.
                                 * @param exception exception
                                 */
                                @Override
                                public void onError(Exception exception) {
                                    Log.e("DB", "Error Sending Notification: ", exception);
                                    Toast.makeText(getContext(),
                                            "Error Sending Notification",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                        }

                        /**
                         * Indicates there is an error fetching the entrants.
                         * @param exception
                         */
                        @Override
                        public void onError(Exception exception) {
                            Log.e("DB", "Error Fetching Attending Entrants: ", exception);
                            Toast.makeText(getContext(),
                                    "Error Fetching Recipients",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                } else if(spinnerPos == 2) {
                    db.getCancelledEntrants(currentEventId, new DbCallback() {
                        /**
                         * If the list of  entrants is succesfully retrieved, send the notif to participants who opted in for notifications.
                         * @param result attending entrants
                         */
                        @Override
                        public void onSuccess(Object result) {
                            ArrayList<Map<String, Object>> participants = (ArrayList<Map<String, Object>>) result;
                            // filter participants based on "notifications" being true
                            for (Map<String, Object> participant : participants) {
                                Object notificationsEnabled = participant.get("notifications");
                                if (notificationsEnabled instanceof Boolean && (Boolean) notificationsEnabled) {
                                    // Add participant to recipients if notifications are enabled
                                    recipients.add(participant);
                                }
                            }
                            // dont create a notification with no recipents
                            if (recipients.isEmpty()) {
                                Toast.makeText(getContext(), "No entrants with notifications enabled", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            notification.setRecipients(recipients);
                            notification.setNotifMessage(notificationText.getText().toString());
                            notification.setNotifTitle(notificationTitle.getText().toString());
                            notification.setEventId(currentEventId);
                            //String deviceId = auth.getCurrentUser().getUid();
                            db.addNotification(notification, new DbCallback() {
                                /**
                                 * Creates the notification.
                                 * @param result notification
                                 */
                                @Override
                                public void onSuccess(Object result) {
                                    Log.d("DB", "Sent Notification: " + notification.getNotifTitle() + " to database");
                                    Toast.makeText(getContext(),
                                            "Notification Sent Successfully",
                                            Toast.LENGTH_SHORT).show();
                                }
                                /**
                                 * Indicates there was an error creating a notification.
                                 * @param exception exception
                                 */
                                @Override
                                public void onError(Exception exception) {
                                    Log.e("DB", "Error Sending Notification: ", exception);
                                    Toast.makeText(getContext(),
                                            "Error Sending Notification",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        /**
                         * Indicates there is an error fetching the entrants.
                         * @param exception
                         */
                        @Override
                        public void onError(Exception exception) {
                            Log.e("DB", "Error Fetching Cancelled Entrants: ", exception);
                            Toast.makeText(getContext(),
                                    "Error Fetching Recipients",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                } else if (spinnerPos == 3) {
                    db.getWaitlistEntrants(currentEventId, new DbCallback() {
                        /**
                         * If the list of  entrants is succesfully retrieved, send the notif to participants who opted in for notifications.
                         * @param result attending entrants
                         */
                        @Override
                        public void onSuccess(Object result) {
                            ArrayList<Map<String, Object>> participants = (ArrayList<Map<String, Object>>) result;
                            // filter participants based on "notifications" being true
                            for (Map<String, Object> participant : participants) {
                                Object notificationsEnabled = participant.get("notifications");
                                if (notificationsEnabled instanceof Boolean && (Boolean) notificationsEnabled) {
                                    // Add participant to recipients if notifications are enabled
                                    recipients.add(participant);
                                }
                            }
                            // dont create a notification with no recipents
                            if (recipients.isEmpty()) {
                                Toast.makeText(getContext(), "No entrants with notifications enabled", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            notification.setRecipients(recipients);
                            notification.setNotifMessage(notificationText.getText().toString());
                            notification.setNotifTitle(notificationTitle.getText().toString());
                            notification.setEventId(currentEventId);
                            //String deviceId = auth.getCurrentUser().getUid();
                            db.addNotification(notification, new DbCallback() {
                                /**
                                 * Creates the notification.
                                 * @param result notification
                                 */
                                @Override
                                public void onSuccess(Object result) {
                                    Log.d("DB", "Sent Notification: " + notification.getNotifTitle() + " to database");
                                    Toast.makeText(getContext(),
                                            "Notification Sent Successfully",
                                            Toast.LENGTH_SHORT).show();
                                }
                                /**
                                 * Indicates there was an error creating a notification.
                                 * @param exception exception
                                 */
                                @Override
                                public void onError(Exception exception) {
                                    Log.e("DB", "Error Sending Notification: ", exception);
                                    Toast.makeText(getContext(),
                                            "Error Sending Notification",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        /**
                         * Indicates there is an error fetching the entrants.
                         * @param exception
                         */
                        @Override
                        public void onError(Exception exception) {
                            Log.e("DB", "Error Fetching Waitlist Entrants: ", exception);
                            Toast.makeText(getContext(),
                                    "Error Fetching Recipients",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                } else if (spinnerPos == 4){
                    db.getInvitedEntrants(currentEventId, new DbCallback() {
                        /**
                         * If the list of  entrants is succesfully retrieved, send the notif to participants who opted in for notifications.
                         * @param result attending entrants
                         */
                        @Override
                        public void onSuccess(Object result) {
                            ArrayList<Map<String, Object>> participants = (ArrayList<Map<String, Object>>) result;
                            // filter participants based on "notifications" being true
                            for (Map<String, Object> participant : participants) {
                                Object notificationsEnabled = participant.get("notifications");
                                if (notificationsEnabled instanceof Boolean && (Boolean) notificationsEnabled) {
                                    // Add participant to recipients if notifications are enabled
                                    recipients.add(participant);
                                }
                            }
                            // dont create a notification with no recipents
                            if (recipients.isEmpty()) {
                                Toast.makeText(getContext(), "No entrants with notifications enabled", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            notification.setRecipients(recipients);
                            notification.setNotifMessage(notificationText.getText().toString());
                            notification.setNotifTitle(notificationTitle.getText().toString());
                            notification.setEventId(currentEventId);
                            //String deviceId = auth.getCurrentUser().getUid();
                            db.addNotification(notification, new DbCallback() {
                                /**
                                 * Creates the notification.
                                 * @param result notification
                                 */
                                @Override
                                public void onSuccess(Object result) {
                                    Log.d("DB", "Sent Notification: " + notification.getNotifTitle() + " to database");
                                    Toast.makeText(getContext(),
                                            "Notification Sent Successfully",
                                            Toast.LENGTH_SHORT).show();
                                }
                                /**
                                 * Indicates there was an error creating a notification.
                                 * @param exception exception
                                 */
                                @Override
                                public void onError(Exception exception) {
                                    Log.e("DB", "Error Sending Notification: ", exception);
                                    Toast.makeText(getContext(),
                                            "Error Sending Notification",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        /**
                         * Indicates there is an error fetching the entrants.
                         * @param exception
                         */
                        @Override
                        public void onError(Exception exception) {
                            Log.e("DB", "Error Fetching Invited Entrants: ", exception);
                            Toast.makeText(getContext(),
                                    "Error Fetching Recipients",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                } else if (spinnerPos == 0) {
                    db.getAllEntrants(currentEventId, new DbCallback() {
                        /**
                         * If the list of  entrants is succesfully retrieved, send the notif to participants who opted in for notifications.
                         * @param result attending entrants
                         */
                        @Override
                        public void onSuccess(Object result) {
                            ArrayList<Map<String, Object>> participants = (ArrayList<Map<String, Object>>) result;
                            // filter participants based on "notifications" being true
                            for (Map<String, Object> participant : participants) {
                                Object notificationsEnabled = participant.get("notifications");
                                if (notificationsEnabled instanceof Boolean && (Boolean) notificationsEnabled) {
                                    // Add participant to recipients if notifications are enabled
                                    recipients.add(participant);
                                }
                            }
                            // dont create a notification with no recipents
                            if (recipients.isEmpty()) {
                                Toast.makeText(getContext(), "No entrants with notifications enabled", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            notification.setRecipients(recipients);
                            notification.setNotifMessage(notificationText.getText().toString());
                            notification.setNotifTitle(notificationTitle.getText().toString());
                            notification.setEventId(currentEventId);
                            //String deviceId = auth.getCurrentUser().getUid();
                            db.addNotification(notification, new DbCallback() {
                                /**
                                 * Creates the notification.
                                 * @param result notification
                                 */
                                @Override
                                public void onSuccess(Object result) {
                                    Log.d("DB", "Sent Notification: " + notification.getNotifTitle() + " to database");
                                    Toast.makeText(getContext(),
                                            "Notification Sent Successfully",
                                            Toast.LENGTH_SHORT).show();
                                }
                                /**
                                 * Indicates there was an error creating a notification.
                                 * @param exception exception
                                 */
                                @Override
                                public void onError(Exception exception) {
                                    Log.e("DB", "Error Sending Notification: ", exception);
                                    Toast.makeText(getContext(),
                                            "Error Sending Notification",
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        /**
                         * Indicates there is an error fetching the entrants.
                         * @param exception
                         */
                        @Override
                        public void onError(Exception exception) {
                            Log.e("DB", "Error Fetching All Entrants: ", exception);
                            Toast.makeText(getContext(),
                                    "Error Fetching Recipients",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });

        return  view;
    }

    /**
     * Method used to set the dialog fragment to fit 90% of the screen width
     * ref: https://stackoverflow.com/questions/12478520/how-to-set-dialogfragments-width-and-height
     * @author mylayambao
     * @since project part 4
     */
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // set dialog width to 90% of screen width
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}