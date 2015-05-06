package gens.com.vasinn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import gens.com.vasinn.controllers.UserController;


public class LoginActivity extends ActionBarActivity {
    public final static String USERNAME_KEY = "gens.com.vasinn.USERNAME";
    public final static String PASSWORD_KEY = "gens.com.vasinn.PASSWORD";
    public UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userController = new UserController();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Now we will try to retrieve the data saved i.e. true, 1f and Hello! World
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences;
        mySharedPreferences = getSharedPreferences("vasinnPreference", mode);

        // Retrieve the saved values.
        String un = mySharedPreferences.getString("myUsername", "");

        // TODO delete this shit
        Toast.makeText(getApplicationContext(), un, Toast.LENGTH_SHORT).show();

        // Checking if user is already logged in
        if (!un.isEmpty()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USERNAME_KEY, un);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {
        String userName = ((EditText)findViewById(R.id.edtLoginUsername)).getText().toString();
        String password = ((EditText)findViewById(R.id.edtLoginPassword)).getText().toString();
        if (userController.loginUser(userName, password))  {
            // select your mode to be either private or public.
            int mode = Activity.MODE_PRIVATE;

            // get the sharedPreference of your context.
            SharedPreferences mySharedPreferences;
            mySharedPreferences = getSharedPreferences("vasinnPreference", mode);

            // retrieve an editor to modify the shared preferences
            SharedPreferences.Editor editor = mySharedPreferences.edit();

            // now store your primitive type values. In this case it is true, 1f and Hello! World
            editor.putString("myUsername", userName);

            //save the changes that you made
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USERNAME_KEY, userName);
            intent.putExtra(PASSWORD_KEY, password);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Þú ert ekki skráður í kerfið, vinsamlegast skráðu þig á www.vasinn.is", Toast.LENGTH_SHORT).show();
        }
    }
}
