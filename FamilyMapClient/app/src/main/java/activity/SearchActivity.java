package activity;

import mainactivity.MainActivity;
import Model.Person;
import Model.Event;
import edu.byu.cs240.dallinstewart.familymapclient.Global;
import edu.byu.cs240.dallinstewart.familymapclient.R;
import model.DataCache;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.view.MenuItem;

public class SearchActivity extends AppCompatActivity {
    private Map<String, Person> people;
    private Map<String, Event> events;
    private List<Person> searchResultPeople;
    private List<Event> searchResultEvents;
    private RecyclerView searchRecyclerView;
    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;
    public static final String TAG = "SearchKey";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_search);
        Iconify.with(new FontAwesomeModule());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set class variables
        searchRecyclerView = (RecyclerView) findViewById(R.id.search_view);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        people = DataCache.getInstance().getAllPersons();
        events = DataCache.getInstance().getAllEvents();
        updateUI();
        //set a text watcher to make the search bar work
        TextView searchBox = findViewById(R.id.searchBar);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //update the search results and the UI every time the search query is changed
                String searchQuery = searchBox.getText().toString();
                searchResultPeople = Global.searchPerson(people, searchQuery.toLowerCase(Locale.ROOT));
                searchResultEvents = Global.searchEvent(events, searchQuery.toLowerCase(Locale.ROOT));
                updateUI();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        searchBox.addTextChangedListener(textWatcher);
    }
    private void updateUI() {
        //update the the search adapter
        Log.d(TAG, "updateUI called");
        SearchAdapter searchAdapter = new SearchAdapter(searchResultPeople, searchResultEvents);
        searchRecyclerView.setAdapter(searchAdapter);
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {
        private final List<Person> people;
        private final List<Event> events;

        public SearchAdapter(List<Person> people, List<Event> events) {
            Log.d(TAG, "Search Adapter constructed");
            this.people = people;
            this.events = events;
        }

        @NonNull
        @Override
        public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "Search Adapter onCreateViewHolder called");
            View view;
            //inflate the elements of the search activity view
            if  (viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.item_person, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.item_event, parent, false);
            }
            return new SearchHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchHolder searchHolder, int position) {
            Log.d(TAG, "Search Adapter onBindViewHolder called");
            if (position < people.size()) {
                searchHolder.bind(people.get(position));
            } else {
                searchHolder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "Search Adapter getItemCount called");
            int count = 0;
            if (people != null) {
                count += people.size();
            }
            if (events != null) {
                count += events.size();
            }
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            int size = 0;
            if (people != null) {
                size = people.size();
            }
            return position < size ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }
    }

    private class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView topLine;
        private final TextView bottomLine;
        private final ImageView resultIcon;
        private Person person;
        private Event event;

        public SearchHolder(View view, int viewType) {
            super(view);
            Log.d(TAG, "SearchHolder constructed");
            //set the display elements
            itemView.setOnClickListener(this);
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                topLine = itemView.findViewById(R.id.relativeFirstName);
                bottomLine = itemView.findViewById(R.id.relativeRelation);
                resultIcon = itemView.findViewById(R.id.personIcon);
            } else {
                topLine = itemView.findViewById(R.id.eventDetails);
                bottomLine = itemView.findViewById(R.id.associatedName);
                resultIcon = itemView.findViewById(R.id.eventIcon);
            }
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Search holder  onClick called");
            Intent intent;
            //open the corresponding person or event activity when a search result is clicked
            if (getItemViewType() == PERSON_ITEM_VIEW_TYPE) {
                intent = PersonActivity.newIntent(SearchActivity.this, person.getPersonID());
            } else {
                intent = EventActivity.newIntent(SearchActivity.this, event.getEventID());
            }
            startActivity(intent);
        }

        public void bind(Event event) {
            //update event result display element
            Log.d(TAG, "Search holder event bind called");
            this.event = event;
            topLine.setText(formatEventInfo(event));
            bottomLine.setText(formatEventInfo(event.getPersonID()));
            Drawable eventIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.black).sizeDp(40);
            resultIcon.setImageDrawable(eventIcon);
        }

        public void bind(Person person) {
            //update person result display element
            Log.d(TAG, "Search holder person bind called");
            this.person = person;
            topLine.setText(formatEventInfo(person.getPersonID()));
            bottomLine.setText("");
            resultIcon.setImageDrawable(setIcon());
        }

        private String formatEventInfo(Event event) {
            //convert event elements to a formatted display text
            String eventType = event.getEventType().toUpperCase();
            return eventType + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
        }

        private String formatEventInfo(String personID) {
            //convert the associated person name to a formatted display text
            Person getNames = DataCache.getInstance().getPersonByID(personID);
            return getNames.getFirstName() + " " + getNames.getLastName();
        }

        private Drawable setIcon() {
            //set the icon next to a person result based on their gender
            FontAwesomeIcons icon;
            int color;
            if (person.getGender().equals("m")) {
                icon = FontAwesomeIcons.fa_male;
                color = R.color.male_icon;
            } else {
                icon = FontAwesomeIcons.fa_female;
                color = R.color.female_icon;
            }
            return new IconDrawable(SearchActivity.this, icon).colorRes(color).sizeDp(40);
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

    public static Intent newIntent(Context packageContext) {
        //Handle new intents when another activity wants to call Search Activity
        Log.d(TAG, "newIntent called");
        return new Intent(packageContext, SearchActivity.class);
    }
}