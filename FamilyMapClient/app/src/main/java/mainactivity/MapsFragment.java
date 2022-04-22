package mainactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.PersonActivity;
import activity.SearchActivity;
import activity.SettingsActivity;
import Model.Event;
import Model.Person;
import edu.byu.cs240.dallinstewart.familymapclient.Global;
import edu.byu.cs240.dallinstewart.familymapclient.R;
import model.DataCache;
import model.Settings;

public class MapsFragment extends Fragment {
    private List<String> eventTypes;
    private GoogleMap googleMap;
    private String associatedPersonID;
    private String eventID;
    private View view;
    private TextView mapDetails;
    private ImageView mapPersonIcon;
    private List<Polyline> presentPolylines;
    private final String TAG = "MapsFragment";
    private final String START_ICON = "1";

    private final int DEFAULT_WIDTH = 10;

    public MapsFragment() {
        this.eventID = "";
    }
    public MapsFragment(String eventID) {
        this.eventID = eventID;
        DataCache.getInstance().setLastEventID(eventID);
    }

    @Override
    public void onResume() {
        super.onResume();
        String formatted = "";
        Event event;
        //update the event id
        eventID = DataCache.getInstance().getLastEventID();
        //update the associated person id
        if (eventID != null && !eventID.equals("")) {
            associatedPersonID = DataCache.getInstance().getEventByID(eventID).getPersonID();
        }
        //if the settings were changed
        if (DataCache.getInstance().isEventsUpdated()) {
            //refresh the map
            googleMap.clear();
            setMarkers();
            //if there is no event id, default to the last event displayed and update the map
            if (eventID.equals("")) {
                int lastElement = DataCache.getInstance().getDisplayEvents().length - 1;
                if (lastElement >= 0) {
                    event = DataCache.getInstance().getDisplayEvents()[lastElement];
                    formatted = formatEventDetails(event);
                    mapPersonIcon.setImageDrawable(Global.getIcon(getContext(), associatedPersonID));
                    removeAllLines();
                    drawAllLines(event);
                }
            } else {
                //otherwise, update the map by the eventID
                event = DataCache.getInstance().getEventByID(eventID);
                formatted = formatEventDetails(event);
                mapPersonIcon.setImageDrawable(Global.getIcon(getContext(), associatedPersonID));
                removeAllLines();
                drawAllLines(event);
            }
            //update the event details
            mapDetails.setText(formatted);
            DataCache.getInstance().setEventsUpdated(false);
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMapSetter) {
            eventTypes = new ArrayList<>();
            associatedPersonID = "";
            eventTypes.add("birth");
            eventTypes.add("death");
            eventTypes.add("marriage");
            googleMap = googleMapSetter;
            //update the lines and the associated personID
            if (!DataCache.getInstance().getLastEventID().equals("")) {
                Event event = DataCache.getInstance().getEventByID(DataCache.getInstance().getLastEventID());
                associatedPersonID = event.getPersonID();
                drawAllLines(event);
            }
            //when a marker is clicked,
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    if (marker != null) {
                        Event event = (Event) marker.getTag();
                        String formatted = "";
                        if (event != null) {
                            //update eventID,  personID, and event details
                            eventID = event.getEventID();
                            DataCache.getInstance().setLastEventID(event.getEventID());
                            associatedPersonID = event.getPersonID();
                            formatted = formatEventDetails(event);
                        }
                        //update the camera, lines, and display details
                        mapDetails = (TextView) view.findViewById(R.id.mapTextView);
                        mapDetails.setText(formatted);
                        mapPersonIcon.setImageDrawable(Global.getIcon(getContext(), associatedPersonID));
                        drawAllLines(event);
                        return true;
                    }
                    return false;
                }
            });
            //update the markers on the UI
            setMarkers();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_maps, container, false);
        setHasOptionsMenu(true);
        Iconify.with(new FontAwesomeModule());
        //initialize variables
        DataCache.getInstance().updateDisplayEvents();
        mapDetails = (TextView) view.findViewById(R.id.mapTextView);
        mapPersonIcon = (ImageView) view.findViewById(R.id.mapPersonIcon);
        //if the events were updated, update the map
        if (DataCache.getInstance().isLastEventUpdated()) {
            eventID = DataCache.getInstance().getLastEventID();
            Event event = DataCache.getInstance().getEventByID(eventID);
            mapDetails.setText(formatEventDetails(event));
            mapPersonIcon.setImageDrawable(Global.getIcon(getContext(), associatedPersonID));
            associatedPersonID = event.getPersonID();
        } else {
            mapDetails.setText(formatEventDetails());
            mapPersonIcon.setImageDrawable(Global.getIcon(getContext(), START_ICON));
        }

        //if the details are clicked, open a new person activity corresponding
        //to the person associated with the event clicked
        mapDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventID != null && !eventID.equals("")) {
                    associatedPersonID = DataCache.getInstance().getEventByID(eventID).getPersonID();
                }
                Intent intent = PersonActivity.newIntent(getActivity(), associatedPersonID);
                startActivity(intent);
            }
        });

        return view;
    }
    //helper method to convert event elements to displayable text
    private String formatEventDetails(Event event) {
        associatedPersonID = event.getPersonID();
        Person toFullName = DataCache.getInstance().getPersonByID(associatedPersonID);
        String fullName = toFullName.getFirstName() + " " + toFullName.getLastName();
        String eventType = event.getEventType().toUpperCase(Locale.ROOT);
        String city = event.getCity();
        String country = event.getCountry();
        int year = event.getYear();

        return fullName + "\n" + eventType + ": " + city + ", " + country + " (" + year + ")";
    }
    //default event details
    private String formatEventDetails() {
        return "Click on a marker to see event details";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //build the settings and search buttons
        if (DataCache.getInstance().getLastEventID().equals("")) {
            inflater.inflate(R.menu.main_menu, menu);

            MenuItem settingsItem = menu.findItem(R.id.settingsMenuItem);
            settingsItem.setIcon(new IconDrawable(this.getContext(), FontAwesomeIcons.fa_gear)
                    .colorRes(R.color.white)
                    .actionBarSize());
            MenuItem searchItem = menu.findItem(R.id.searchMenuItem);
            searchItem.setIcon(new IconDrawable(this.getContext(), FontAwesomeIcons.fa_search)
                    .colorRes(R.color.white)
                    .actionBarSize());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.settingsMenuItem:
                //go to the settings activity
                Intent settingsIntent = SettingsActivity.newIntent(getActivity());
                startActivity(settingsIntent);
                return true;
            case R.id.searchMenuItem:
                //go to the search activity
                Intent searchIntent = SearchActivity.newIntent(getActivity());
                startActivity(searchIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }
    //helper method to set markers on the maps
    private void setMarkers() {
        Event[] displayEvents = DataCache.getInstance().getDisplayEvents();
        Marker[] markers = new Marker[displayEvents.length];
        int index = 0;
        //build a list of markers so that they can be cleared easily
        for (Event event : displayEvents) {
            markers[index] = addMarkerFeatures(event);
            markers[index].setTag(event);
            index++;
        }
        //update the map UI based on the event
        Event presentEvent = DataCache.getInstance()
                .getEventByID(DataCache.getInstance().getLastEventID());
        if (presentEvent != null) {
            LatLng presentEventLoc = new
                    LatLng(presentEvent.getLatitude(), presentEvent.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(presentEventLoc));
        } else {
            Log.d(TAG,"event '" + DataCache.getInstance().getLastEventID() + "' not found, marker not set");
        }
        if (displayEvents.length > 0) {
            associatedPersonID = displayEvents[displayEvents.length - 1].getPersonID();
        }
    }
    //helper method to build a marker with the correct features based on the event
    private Marker addMarkerFeatures(Event event) {
        float lat = event.getLatitude();
        float lng = event.getLongitude();
        String city = event.getCity();
        String country = event.getCountry();
        String type = event.getEventType().toLowerCase(Locale.ROOT);
        String eventType = type.substring(0, 1).toUpperCase() + type.substring(1);
        LatLng eventLoc = new LatLng(lat, lng);
        float markerColor = chooseColor(type);
        return googleMap.addMarker(new MarkerOptions()
                .position(eventLoc)
                .title(eventType + " in " + city + ", " + country)
                .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
        );
    }
    //helper method to draw lines based on current settings
    private void drawAllLines(Event event) {
        removeAllLines();
        if (event != null) {
            if (Settings.getInstance().isFamilyTreeLines()) {
                drawFamilyLines(event, DEFAULT_WIDTH + 6);
            }
            if (Settings.getInstance().isLifeStoryLines()) {
                drawLifeLines();
            }
            if (Settings.getInstance().isSpouseLines()) {
                drawSpouseLines(event);
            }
        }
    }
    //helper method to draw life lines as appropriate
    private void drawLifeLines() {
        List<Event> myEvents = DataCache.getInstance().getPersonEvents(associatedPersonID);
        myEvents = Global.orderEvents(myEvents);
        Event prevEvent = null;
        for (Event event : myEvents) {
            if (prevEvent != null) {
                drawAndRemoveLine(prevEvent, event,
                        getActivity().getResources().getColor(R.color.teal_200), DEFAULT_WIDTH);
            }
            prevEvent = event;
        }
    }
    //helper method to draw spouse lines as appropriate
    private void drawSpouseLines(Event eventClicked) {
        String spouseID = DataCache.getInstance().getPersonByID(associatedPersonID).getSpouseID();
        if (!spouseID.equals("")) {
            List<Event> spouseEvents = DataCache.getInstance().getPersonEvents(spouseID);
            if (spouseEvents.size() > 0) {
                spouseEvents = Global.orderEvents(spouseEvents);
                drawAndRemoveLine(eventClicked, spouseEvents.get(0),
                        getActivity().getResources().getColor(R.color.purple_200), DEFAULT_WIDTH);
            }
        }
    }
    //recursive helper method to draw family lines
    private void drawFamilyLines(Event eventClicked, int width) {
        Person assocPerson = DataCache.getInstance().getPersonByID(eventClicked.getPersonID());
        String fatherID = assocPerson.getFatherID();
        String motherID = assocPerson.getMotherID();
        //reduce the width of the line
        int newWidth = Math.max(width - 4, 1);
        if (fatherID != null && !fatherID.equals("")) {
            //if the person has a father, find their first event and draw a line to it
            List<Event> fatherEvents = DataCache.getInstance().getPersonEvents(fatherID);
            if (fatherEvents.size() > 0) {
                fatherEvents = Global.orderEvents(fatherEvents);
                drawAndRemoveLine(eventClicked, fatherEvents.get(0),
                        getActivity().getResources().getColor(R.color.purple_700), width);
                //call the function again recursively with new personId and width
                drawFamilyLines(fatherEvents.get(0), newWidth);
            }
        }
        if (motherID != null && !motherID.equals("")) {
            //if the person has a mother, find their first event and draw a line to it
            List<Event> motherEvents = DataCache.getInstance().getPersonEvents(motherID);
            if (motherEvents.size() > 0) {
                motherEvents = Global.orderEvents(motherEvents);
                drawAndRemoveLine(eventClicked, motherEvents.get(0),
                        getActivity().getResources().getColor(R.color.purple_700), width);
                //call the function again recursively with new personId and width
                drawFamilyLines(motherEvents.get(0), newWidth);
            }
        }
    }
    //helper method to draw a single line based on endpoints, desired color, and width
    private void drawAndRemoveLine(Event startEvent, Event endEvent, int googleColor, float width) {
        // Create start and end points for the line
        LatLng startPoint = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint= new LatLng(endEvent.getLatitude(), endEvent.getLongitude());
        // Add line to map by specifying its endpoints, color, and width
        PolylineOptions options = new PolylineOptions()
                .add(startPoint, endPoint)
                .color(googleColor)
                .width(width);
        Polyline line = googleMap.addPolyline(options);
        if (presentPolylines == null) {
            presentPolylines = new ArrayList<>();
        }
        presentPolylines.add(line);
    }
    //helper method to remove all lines from the map
    private void removeAllLines() {
        if (presentPolylines != null) {
            for (Polyline line : presentPolylines) {
                line.remove();
            }
        }
    }
    //helper method to choose the correct color for a given event type
    private float chooseColor(String eventType) {
        float color = 0;
        Float[] colors = setColorArray();
        int index = 0;
        boolean eventFound = false;
        for (String ev : eventTypes) {
            if (!eventFound && eventType.equals(ev)) {
                color = colors[index];
                eventFound = true;
            }
            index = ++index % colors.length;
        }
        if (!eventFound) {
            eventTypes.add(eventType);
            color = colors[index];
        }
        return color;
    }
    //put all bitmapdescriptorfactory colors into an array
    private Float[] setColorArray() {
        Float[] colors = new Float[DEFAULT_WIDTH];
        colors[0] = BitmapDescriptorFactory.HUE_RED;
        colors[1] = BitmapDescriptorFactory.HUE_BLUE;
        colors[2] = BitmapDescriptorFactory.HUE_GREEN;
        colors[3] = BitmapDescriptorFactory.HUE_AZURE;
        colors[4] = BitmapDescriptorFactory.HUE_CYAN;
        colors[5] = BitmapDescriptorFactory.HUE_MAGENTA;
        colors[6] = BitmapDescriptorFactory.HUE_ORANGE;
        colors[7] = BitmapDescriptorFactory.HUE_ROSE;
        colors[8] = BitmapDescriptorFactory.HUE_VIOLET;
        colors[9] = BitmapDescriptorFactory.HUE_YELLOW;
        return colors;
    }
}



