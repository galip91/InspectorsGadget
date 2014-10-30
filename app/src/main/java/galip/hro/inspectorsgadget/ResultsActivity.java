package galip.hro.inspectorsgadget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultsActivity extends Activity {
    public static final String UNNumber = "1234";
    public HashMap UNMap = new HashMap();
    DBHelper database = new DBHelper(this);
    private String UNCode="Android";
    private List<Card> mCards;
    private GestureDetector mGestureDetector;
    private CardScrollView mCardScroller;
    private View mView;

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://api.androidhive.info/contacts/";

    // JSON Node names
    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
    static ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
        mCardScroller = new CardScrollView(this);
        mCards = new ArrayList<Card>();
        contactList = new ArrayList<HashMap<String, String>>();
        mJSONParser = new GetContacts();
        mJSONParser.execute();

        UNMap.put("1428", "Sodium");
        UNMap.put("2734", "Amines, liquid, corrosive, flammable, n.o.s. or Polyamines, liquid, corrosive, flammable, n.o.s.");
        UNMap.put("2796", "Battery fluid, acid or Sulfuric acid with not more than 51 percent acid");
        UNMap.put("3077", "Environmentally hazardous substance, solid, n.o.s. (not including waste)");
        UNMap.put("3166", "Vehicle, flammable gas powered");

        UNMap.put("3163", "Liquefied gas, n.o.s.");
        UNMap.put("3174", "Titanium disulphide");
        UNMap.put("1713", "Zinc cyanide");


        if(getIntent().hasExtra(UNNumber)){
            UNCode = getIntent().getStringExtra(UNNumber);
        }

        mCardScroller.setAdapter(new DeveloperAdapter(mCards));

        // Handle the TAP event.
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Plays disallowed sound to indicate that TAP actions are not supported.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.DISALLOWED);
            }
        });

        setContentView(mCardScroller);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }
    private View buildView() {
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);

        card.setText(R.string.hello_world);
        return card.getView();
    }

    public void setContactList(ArrayList<HashMap<String, String>> list)
    {
        contactList = list;
    }

    GetContacts mJSONParser;
    private void findCode(String code){

        Card card1 = new Card(this);

        String text1 = "UN number: " + code;
        card1.setText(text1);
        card1.setTimestamp("time");

        Card card2 = new Card(this);
        String text2 = "Name"  + "\n";
        text2 += contactList.get(Integer.parseInt(UNCode)).get("name");
        //text2 += UNMap.get("1428");
        card2.setText(text2);

        mCards.add(card1);
        mCards.add(card2);

        mCardScroller.setSelection(0);
    }

    private com.google.android.glass.touchpad.GestureDetector createGestureDetector(Context context) {
        com.google.android.glass.touchpad.GestureDetector gestureDetector = new com.google.android.glass.touchpad.GestureDetector(context);

        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new com.google.android.glass.touchpad.GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    openOptionsMenu();
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    return true;
                } else if (gesture == Gesture.SWIPE_DOWN){
                    finish();
                }
                return false;
            }
        });

        gestureDetector.setFingerListener(new com.google.android.glass.touchpad.GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
            }
        });

        gestureDetector.setScrollListener(new com.google.android.glass.touchpad.GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling
                return true;
            }
        });

        return gestureDetector;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS || featureId ==  Window.FEATURE_OPTIONS_PANEL) {
            switch (item.getItemId()) {
               case R.id.developer_hire:
                    Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_LONG).show();
                    break;
                case R.id.go_back:
                    break;
                case R.id.c_0001:
                    Toast.makeText(getApplicationContext(), "0001", Toast.LENGTH_LONG).show();
                    break;
                case R.id.c_0002:
                    Toast.makeText(getApplicationContext(), "0002", Toast.LENGTH_LONG).show();
                    break;
                case R.id.c_0003:
                    Toast.makeText(getApplicationContext(), "0003", Toast.LENGTH_LONG).show();
                    break;
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }


    // Async class for getting and processing the Json
    private class GetContacts extends AsyncTask<Void, Void, String> {
        ArrayList<HashMap<String, String>> lstContacts;
        @Override
        protected void onPreExecute() {
          super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ResultsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            lstContacts = new ArrayList<HashMap<String, String>>();

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String email = c.getString(TAG_EMAIL);
                        String address = c.getString(TAG_ADDRESS);
                        String gender = c.getString(TAG_GENDER);

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject(TAG_PHONE);
                        String mobile = phone.getString(TAG_PHONE_MOBILE);
                        String home = phone.getString(TAG_PHONE_HOME);
                        String office = phone.getString(TAG_PHONE_OFFICE);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_EMAIL, email);
                        contact.put(TAG_PHONE_MOBILE, mobile);

                        // adding contact to contact list
                        lstContacts.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            setContactList(lstContacts);
            findCode(UNCode);

//          Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
}



























//    public void getJson()
//    {
//        // Creating service handler class instance
//        ServiceHandler sh = new ServiceHandler();
//
//        // Making a request to url and getting response
//        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
//
//        Log.d("Response: ", "> " + jsonStr);
//
//        if (jsonStr != null) {
//            try {
//                JSONObject jsonObj = new JSONObject(jsonStr);
//
//                // Getting JSON Array node
//                contacts = jsonObj.getJSONArray(TAG_CONTACTS);
//
//                // looping through All Contacts
//                for (int i = 0; i < contacts.length(); i++) {
//                    JSONObject c = contacts.getJSONObject(i);
//
//                    String id = c.getString(TAG_ID);
//                    String name = c.getString(TAG_NAME);
//                    String email = c.getString(TAG_EMAIL);
//                    String address = c.getString(TAG_ADDRESS);
//                    String gender = c.getString(TAG_GENDER);
//
//                    // Phone node is JSON Object
//                    JSONObject phone = c.getJSONObject(TAG_PHONE);
//                    String mobile = phone.getString(TAG_PHONE_MOBILE);
//                    String home = phone.getString(TAG_PHONE_HOME);
//                    String office = phone.getString(TAG_PHONE_OFFICE);
//
//                    // tmp hashmap for single contact
//                    HashMap<String, String> contact = new HashMap<String, String>();
//
//                    // adding each child node to HashMap key => value
//                    contact.put(TAG_ID, id);
//                    contact.put(TAG_NAME, name);
//                    contact.put(TAG_EMAIL, email);
//                    contact.put(TAG_PHONE_MOBILE, mobile);
//
//                    // adding contact to contact list
//                    contactList.add(contact);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else
//        {
//            Log.e("ServiceHandler", "Couldn't get any data from the url");

