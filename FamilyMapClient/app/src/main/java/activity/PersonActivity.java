package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mainactivity.MainActivity;
import Model.Event;
import Model.Person;
import edu.byu.cs240.dallinstewart.familymapclient.CreateToast;
import edu.byu.cs240.dallinstewart.familymapclient.Global;
import edu.byu.cs240.dallinstewart.familymapclient.R;
import model.DataCache;

public class PersonActivity extends AppCompatActivity {
    public static final String TAG = "PersonKey";
    public static final String EXTRA_PERSON_ID =
            "edu.byu.cs240.dallinstewart.familymapclient.personIntent.person_id";
    private final String ERROR = "Error";
    private final String FATHER = "Father";
    private final String MOTHER = "Mother";
    private final String SPOUSE = "Spouse";
    private final String CHILD = "Child";
    private final String EVENT = "0";
    private String personID;
    private Person presentPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Log.d(TAG, "onCreate(Bundle) called");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initialize data for display
        personID = getIntent().getStringExtra(EXTRA_PERSON_ID);
        ExpandableListView details = (ExpandableListView) findViewById(R.id.expandableListView);
        presentPerson = DataCache.getInstance().getPersonByID(personID);
        List<Event> displayEvents = DataCache.getInstance().getPersonEvents(personID);
        if (displayEvents == null) {
            displayEvents = new ArrayList<>();
            Log.d(TAG, "displayEvents initialized null");
        }
        //set the text for the activity based on loaded data
        TextView firstNameView = (TextView) findViewById(R.id.personFirstName);
        TextView lastNameView = (TextView) findViewById(R.id.personLastName);
        TextView genderView = (TextView) findViewById(R.id.personGender);
        firstNameView.setText(presentPerson.getFirstName());
        lastNameView.setText(presentPerson.getLastName());
        genderView.setText(convertGenderValue(presentPerson.getGender()));

        details.setAdapter(new ExpandableListAdapter(presentPerson, displayEvents));
    }
    private String convertGenderValue(String gender) {
        //convert stored verson of gender to a presentable version
        if (gender.equals("m")) {
            return "Male";
        } else {
            return "Female";
        }
    }

    public static Intent newIntent(Context packageContext, String personID) {
        //Handle new intents when another activity wants to call Person Activity
        String staticTAG = "PersonActivity";
        Log.d(staticTAG, "newIntent called");
        Intent intent = new Intent(packageContext, PersonActivity.class);
        intent.putExtra(EXTRA_PERSON_ID, personID);
        return intent;
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int PEOPLE_GROUP_POSITION = 1;
        private static final int EVENT_GROUP_POSITION = 0;

        private final List<String> displayPerson;
        private final List<Event> displayEvents;
        private List<String> children;

        public ExpandableListAdapter(Person displayPerson, List<Event> displayEvents) {
            this.displayPerson = new ArrayList<>();
            fillDisplayPerson(displayPerson);
            this.displayEvents = Global.orderEvents(displayEvents);
        }


        private void fillDisplayPerson(Person displayPerson) {
            //Builds a list of people to display in the relationship section
            String fatherID = displayPerson.getFatherID();
            String motherID = displayPerson.getMotherID();
            String spouseID = displayPerson.getSpouseID();
            children = DataCache.getInstance().getChildren(displayPerson.getPersonID());
            String fatherFull, motherFull, spouseFull, childFull;
            //Set the text for the name fields
            if (fatherID == null || fatherID.equals("")) {
                fatherFull = "No Father in Records";
            } else {
                Person father = DataCache.getInstance().getPersonByID(fatherID);
                fatherFull = father.getFirstName() + " " + father.getLastName();
            }
            if (motherID == null || motherID.equals("")) {
                motherFull = "No Mother in Records";
            } else {
                Person mother = DataCache.getInstance().getPersonByID(motherID);
                motherFull = mother.getFirstName() + " " + mother.getLastName();
            }
            if (spouseID == null || spouseID.equals("")) {
                spouseFull = "No Spouse in Records";
            } else {
                Person spouse = DataCache.getInstance().getPersonByID(spouseID);
                spouseFull = spouse.getFirstName() + " " + spouse.getLastName();
            }
            this.displayPerson.add(fatherFull);
            this.displayPerson.add(motherFull);
            this.displayPerson.add(spouseFull);
            if (children != null) {
                for (String child : children) {
                    if (!child.equals("")) {
                        Person childToName = DataCache.getInstance().getPersonByID(child);
                        childFull = childToName.getFirstName() + " " + childToName.getLastName();
                        this.displayPerson.add(childFull);
                    }
                }
            }
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case PEOPLE_GROUP_POSITION:
                    return displayPerson.size();
                case EVENT_GROUP_POSITION:
                    return displayEvents.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            // Not used
            return null;
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // Not used
            return null;
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            //Build the view and set the titles of the lists
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }
            TextView titleView = convertView.findViewById(R.id.listTitle);
            switch (groupPosition) {
                case PEOPLE_GROUP_POSITION:
                    titleView.setText(R.string.familyTitle);
                    break;
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.lifeEventsTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;
            switch(groupPosition) {
                case PEOPLE_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item_person, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item_event, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return itemView;
        }

        private void initializePersonView(View personItemView, final int childPosition) {
            //Formats the data for each person display in the expandable list
            ImageView personIcon = personItemView.findViewById(R.id.personIcon);
            String relativeID = getRelativeID(displayRelation(childPosition));
            //determine how many children the person has
            if (relativeID != null && relativeID.equals(ERROR)) {
                int index = Math.min(childPosition - 3, children.size());
                index = Math.max(index, 0);
                relativeID = children.get(index);
            }
            //determine which icon to display
            if (relativeID != null && !relativeID.equals("")) {
                personIcon.setImageDrawable(Global.getIcon(PersonActivity.this, relativeID));
            }
            //update the displayed information
            TextView personNameView = personItemView.findViewById(R.id.relativeTitle);
            personNameView.setText("");
            TextView relativeView = personItemView.findViewById(R.id.relativeFirstName);
            relativeView.setText(displayPerson.get(childPosition));
            TextView relationView = personItemView.findViewById(R.id.relativeRelation);
            relationView.setText(displayRelation(childPosition));
            //build an on click listener to open a new person activity corresponding to
            //the person clicked
            personItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String relativeID = setRelativeID(relationView.getText().toString(),
                            relativeView.getText().toString());
                    if (relativeID != null) {
                        if (!relativeID.equals(ERROR)) {
                            Intent intent = newIntent(PersonActivity.this, relativeID);
                            startActivity(intent);
                        } else {
                            CreateToast.showToast(PersonActivity.this, "No person found");
                        }
                    } else {
                        CreateToast.showToast(PersonActivity.this, "No person found");
                    }
                }
            });
        }
        //helper method to convert an index to a relationship status
        private String displayRelation(int childPosition) {
            if (childPosition == 0) {
                return FATHER;
            } else if (childPosition == 1) {
                return MOTHER;
            } else if (childPosition == 2) {
                return SPOUSE;
            } else {
                return CHILD;
            }
        }
        //helper method to convert a relationship type the the relative's personID
        private String getRelativeID(String relationType) {
            if (relationType == null) {
                return null;
            } else if (relationType.equals(FATHER)) {
                return presentPerson.getFatherID();
            } else if (relationType.equals(MOTHER)) {
                return presentPerson.getMotherID();
            } else if (relationType.equals(SPOUSE)) {
                return presentPerson.getSpouseID();
            } else {
                return ERROR;
            }
        }
        //helper method to convert a relative's name to their personID
        private String setRelativeID(String relation, String relativeName) {
            String relativeID = "";
            if (relation != null) {
                if (relation.equals(CHILD)) {
                    List<String> childrenID = DataCache.getInstance().getChildren(personID);
                    if (childrenID.size() == 0) {
                        relativeID = ERROR;
                    } else if (childrenID.size() == 1) {
                        relativeID = childrenID.get(0);
                    } else {
                        for (String childId : childrenID) {
                            String childName = DataCache.getInstance()
                                    .getPersonByID(childId)
                                    .getFirstName();
                            if (relativeName.contains(childName)) {
                                relativeID = childId;
                                break;
                            }
                        }
                        if (relativeID.equals("")) {
                            return ERROR;
                        }
                    }
                } else {
                    relativeID = getRelativeID(relation);
                }
            } else {
                return ERROR;
            }
            return relativeID;
        }

        private void initializeEventView(View eventItemView, final int childPosition) {
            //Formats the data for each event display in the expandable list
            ImageView eventIcon = eventItemView.findViewById(R.id.eventIcon);
            eventIcon.setImageDrawable(Global.getIcon(PersonActivity.this, EVENT));

            TextView detailsView = eventItemView.findViewById(R.id.eventDetails);
            detailsView.setText(formatEventDetails(displayEvents.get(childPosition)));

            TextView associatedPerson = eventItemView.findViewById(R.id.associatedName);
            associatedPerson.setText(formatAssociatedName());
            //build an on click listener to open a new event activity corresponding to
            //the event clicked
            eventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String eventID = getIDFromDetails(childPosition);
                    if (!eventID.equals(ERROR)) {
                        Intent intent = EventActivity
                                .newIntent(PersonActivity.this, eventID);
                        startActivity(intent);
                    } else {
                        CreateToast.showToast(PersonActivity.this, "No event found");
                    }
                }
            });
        }
        //helper method to format details from an event in the correct display format
        private String formatEventDetails(Event event) {
            if (event == null) {
                return ERROR + ": No Event Found";
            }
            String eventType = event.getEventType().toUpperCase(Locale.ROOT);
            String city = event.getCity();
            String country = event.getCountry();
            int year = event.getYear();
            return eventType + ": " + city + ", " + country + " (" + year + ")";
        }
        //helper method to combine the first and last name of the current person
        private String formatAssociatedName() {
            return presentPerson.getFirstName() + " " + presentPerson.getLastName();
        }
        //helper method to convert display position to the relative's personID
        private String getIDFromDetails(int position) {
            if (0 <= position && position < displayEvents.size()) {
                String eventID = displayEvents.get(position).getEventID();
                if (eventID.equals("")) {
                    return ERROR;
                }
                return eventID;
            }
            return ERROR;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Build the up button
        Log.d(TAG, "onOptionsItemSelected called");
        if(item.getItemId() == android.R.id.home) {
            Intent intent= new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}