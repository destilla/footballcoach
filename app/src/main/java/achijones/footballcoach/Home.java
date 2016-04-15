package achijones.footballcoach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import achijones.footballcoach.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageLogo = (ImageView) findViewById(R.id.imageLogo);
        imageLogo.setImageResource(R.drawable.main_menu_logo);

        Button newGameButton = (Button) findViewById(R.id.buttonNewGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage("What difficulty would you like?\n\n" +
                        "Easy Mode has no injuries, normal rivalry mechanics, and a 50% chance your good players will leave early for the NFL.\n\n" +
                        "Hard Mode has injuries enabled, harder rivalry games, and a 70% chance your good players will leave early for the NFL.\n\n" +
                        "This cannot be changed later.")
                        .setTitle("Choose Difficulty:")
                        .setPositiveButton("EASY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing?
                                Intent myIntent = new Intent(Home.this, MainActivity.class);
                                myIntent.putExtra("SAVE_FILE", "NEW_LEAGUE_EASY");
                                Home.this.startActivity(myIntent);
                            }
                        })
                        .setNegativeButton("HARD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing?
                                Intent myIntent = new Intent(Home.this, MainActivity.class);
                                myIntent.putExtra("SAVE_FILE", "NEW_LEAGUE_HARD");
                                Home.this.startActivity(myIntent);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                TextView msgTxt = (TextView) dialog.findViewById(android.R.id.message);
                msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            }
        });

        Button loadGameButton = (Button) findViewById(R.id.buttonLoadGame);
        loadGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                loadLeague();
            }
        });

        Button tutorialButton = (Button) findViewById(R.id.buttonTutorial);
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent myIntent = new Intent(Home.this, TutorialActivity.class);
                Home.this.startActivity(myIntent);
            }
        });

        Button subredditButton = (Button) findViewById(R.id.buttonSubreddit);
        subredditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://m.reddit.com/r/FootballCoach"));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save League, show dialog to choose which save file to save onto.
     */
    private void loadLeague() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose File to Load:");
        final String[] fileInfos = getSaveFileInfos();
        SaveFilesListArrayAdapter saveFilesAdapter = new SaveFilesListArrayAdapter(this, fileInfos);
        builder.setAdapter(saveFilesAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if (!fileInfos[item].equals("EMPTY")) {
                    Intent myIntent = new Intent(Home.this, MainActivity.class);
                    myIntent.putExtra("SAVE_FILE", "saveFile" + item + ".cfb");
                    Home.this.startActivity(myIntent);
                } else {
                    Toast.makeText(Home.this, "Cannot load empty file!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Get info of the 10 save files for printing in the save file list
     */
    private String[] getSaveFileInfos() {
        String[] infos = new String[10];
        String fileInfo;
        File saveFile;
        for (int i = 0; i < 10; ++i) {
            saveFile = new File(getFilesDir(), "saveFile" + i + ".cfb");
            if (saveFile.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader( new FileReader(saveFile) );
                    fileInfo = bufferedReader.readLine();
                    infos[i] = fileInfo.substring(0, fileInfo.length()-1); //gets rid of % at end
                } catch(FileNotFoundException ex) {
                    System.out.println(
                            "Unable to open file");
                } catch(IOException ex) {
                    System.out.println(
                            "Error reading file");
                } catch (NullPointerException ex) {
                    System.out.println(
                            "Null pointer exception!");
                }
            } else {
                infos[i] = "EMPTY";
            }
        }
        return infos;
    }
}
