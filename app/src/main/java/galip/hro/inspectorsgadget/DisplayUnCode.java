//package galip.hro.inspectorsgadget;
//
//import android.os.Bundle;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * Created by Gebruiker on 13-10-2014.
// */
//public class DisplayUnCode extends Activity {
//
//    int from_Where_I_Am_Coming = 0;
//    private DBHelper mydb ;
//    TextView UnCode ;
//    TextView Description;
//        int id_To_Update = 0;
//
//
//
//
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_display_contact);
//        UnCode = (TextView) findViewById(R.id.editUnCode);
//        Description = (TextView) findViewById(R.id.editDescription);
//
//
//        mydb = new DBHelper(this);
//
//        Bundle extras = getIntent().getExtras();
//        if(extras !=null)
//        {
//            int Value = extras.getInt("id");Create a new Activity as DisplayContact.java that will display the contact on the screen
//
//            if(Value>0){
//                //means this is the view part not the add contact part.
//                Cursor rs = mydb.getData(Value);
//                id_To_Update = Value;
//                rs.moveToFirst();
//                String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
//                String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
//                String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
//                String stree = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_STREET));
//                String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
//                if (!rs.isClosed())
//                {
//                    rs.close();
//                }
//                Button b = (Button)findViewById(R.id.button1);
//                b.setVisibility(View.INVISIBLE);
//
//                name.setText((CharSequence)nam);
//                name.setFocusable(false);
//                name.setClickable(false);
//
//                phone.setText((CharSequence)phon);
//                phone.setFocusable(false);
//                phone.setClickable(false);
//
//                email.setText((CharSequence)emai);
//                email.setFocusable(false);
//                email.setClickable(false);
//
//                street.setText((CharSequence)stree);
//                street.setFocusable(false);
//                street.setClickable(false);
//
//                place.setText((CharSequence)plac);
//                place.setFocusable(false);
//                place.setClickable(false);
//            }
//        }
//    }
//
//}
