package be.ac.uclouvain.menuz;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

public class Utility {

    public static final String REQUIRED_ITEM = "be.ac.uclouvain.menuz.REQUIRED_ITEM";

    // Storage permissions and properties
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String DIR = "/Menuz/";
    private static final String FILE = "results.txt";
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static Boolean TRAINING_SESSION = true;

    private static String participant_name = "Invité";
    private static String[] ordered_items = {"Merlot","Shiraz","Chardonnay","Cabernet","Saturne","Vénus","Jupiter","Mercure","France","Angleterre","Espagne","Allemagne","Noix de pécan","Noix","Amande","Pistache"};
    // english version :
    //private static String[] ordered_items = {"Merlot","Shiraz","Chardonnay","Cabernet","Saturn","Venus","Jupiter","Mercury","France","England","Spain","Germany","Pecan","Walnut","Almond","Pistachio"};

    private static int training_length = 2;
    private static int evaluation_length = 10;
    private static int menu_count = 0;
    private static int selection_count = 0;
    private static int count_items = 0;
    private static String[] required_items = new String[evaluation_length];
    private static String[] selected_items = new String[evaluation_length];
    private static int[] wrong_selection = new int[evaluation_length];
    private static int[] position_required_items = new int[evaluation_length];
    private static int[] position_selected_items = new int[evaluation_length];
    private static int[] item_frequency = new int[16];
    private static int[] most_frequent_items = new int[8];

    private static long start_time = 0;

    private static double[] selection_time = new double[evaluation_length];

    public static Boolean isTrainingSession() {
        return TRAINING_SESSION;
    }

    public static Boolean setParticipantName(String new_name) {
        if(new_name.length()>=2) {
            String prefix = new_name.substring(0,1);
            String suffix = new_name.substring(1,new_name.length());

            // cheat code to go directly to one given menu
            if(prefix.equals("#") && Integer.parseInt(suffix) >= 1 && Integer.parseInt(suffix) <= 8) {
                participant_name = "Cheatcode";
                menu_count = Integer.parseInt(suffix)-1; // MenuIntroduction already ++ this count
                TRAINING_SESSION = false; // evaluation session starts immediately
                return false;
            }
            else {
                participant_name = new_name;
                return true;
            }
        }
        else {
            participant_name = new_name;
            return true;
        }
    }

    public static void resetParticipantName() {
        participant_name = "Invité";
    }

    public static String[] getOrderedItems() {
        return ordered_items;
    }

    public static int getTrainingLength() {
        return training_length;
    }

    public static int getEvaluationLength() {
        return evaluation_length;
    }

    public static int getMenuCount() {
        return menu_count;
    }

    public static void incMenuCount() {
        menu_count++;
    }

    public static void resetMenuCount() {
        menu_count = 0;
    }

    public static int getSelectionCount() {
        return selection_count;
    }

    public static void incSelectionCount() {
        selection_count++;
    }

    public static void resetSelectionCount() {
        selection_count = 0;
    }

    public static void setWrongSelection(int index) {
        wrong_selection[index] = 1;
    }

    public static void resetWrongSelection() {
        wrong_selection = new int[evaluation_length];
    }

    public static double[] getSelectionTime() {
        return selection_time;
    }

    public static void resetSelectionTime() {
        selection_time = new double[evaluation_length];
    }

    public static void incItemFrequency(String item){
        for(int i = 0 ; i<ordered_items.length  ; i++) {
            if(item.equals(ordered_items[i])) {
                item_frequency[i] += 1;
                i = ordered_items.length; // stop for loop
            }
        }

    }

    public static int[][] getMostFrequentItems() {
        // [index of most frequent item1][item1 frequency]
        // [index of most frequent item2][item2 frequency]
        // [index of most frequent item3][item3 frequency]
        int[][] most_frequent = new int[3][2];

        // Pick most frequent items
        for(int i = 0 ; i < item_frequency.length ; i++) {
            if(item_frequency[i] > most_frequent[0][1]) {
                // item2 becomes item3
                most_frequent[2][0] = most_frequent[1][0];
                most_frequent[2][1] = most_frequent[1][1];

                // item1 becomes item2
                most_frequent[1][0] = most_frequent[0][0];
                most_frequent[1][1] = most_frequent[0][1];

                // new item1
                most_frequent[0][0] = i; // item index
                most_frequent[0][1] = item_frequency[i]; // item frequency
            }
            else if(item_frequency[i] > most_frequent[1][1]) {
                // item2 becomes item3
                most_frequent[2][0] = most_frequent[1][0];
                most_frequent[2][1] = most_frequent[1][1];

                // new item2
                most_frequent[1][0] = i; // item index
                most_frequent[1][1] = item_frequency[i]; // item frequency
            }
            else if(item_frequency[i] > most_frequent[2][1]) {
                // new item 3
                most_frequent[2][0] = i; // item index
                most_frequent[2][1] = item_frequency[i]; // item frequency
            }
        }

        // Categorical ordering
        // most_frequent0 > most_frequent1 OR most_frequent1 > most_frequent2
        while(most_frequent[0][0] > most_frequent[1][0]
                || most_frequent[1][0] > most_frequent[2][0]) {
            // most_frequent0 > most_frequent1
            if(most_frequent[0][0] > most_frequent[1][0]) {
                int tmp_index = most_frequent[0][0];
                int tmp_freq = most_frequent[0][1];

                most_frequent[0][0] = most_frequent[1][0];
                most_frequent[0][1] = most_frequent[1][1];
                most_frequent[1][0] = tmp_index;
                most_frequent[1][1] = tmp_freq;
            }
            // most_frequent 1 > most_frequent2
            if(most_frequent[1][0] > most_frequent[2][0]) {
                int tmp_index = most_frequent[1][0];
                int tmp_freq = most_frequent[1][1];

                most_frequent[1][0] = most_frequent[2][0];
                most_frequent[1][1] = most_frequent[2][1];
                most_frequent[2][0] = tmp_index;
                most_frequent[2][1] = tmp_freq;
            }
        }

        return most_frequent;
    }

    /**
     *  Between each menu :
     *      - 8 items are chosen randomly to be the most frequently selected items
     *      - must reset the frequency counter of each item
     */
    public static void resetItemFrequency() {
        item_frequency = new int[16]; // reset counter

        // choose 8 most frequently selected items
        Random random = new Random();
        for(int i = 0 ; i < 8 ; i++) {
            int random_nbr = random.nextInt(16);

            // check if the item was already chosen before
            for(int j=0 ; j < i ; j++) {
                if (most_frequent_items[j] != random_nbr) {
                    most_frequent_items[i] = random_nbr;
                    if(i<4) item_frequency[random_nbr] = 3;
                    else if(i<8) item_frequency[random_nbr] = 1;
                }
                else i=i-1; // stay at this level and chose another one
           }
        }
    }

    public static void resetRequiredItems() {
        required_items = new String[evaluation_length];
        count_items = 0;
    }

    public static void resetSelectedItems() {
        selected_items = new String[evaluation_length];
        count_items = 0;
    }

    public static void resetPositionRequiredItems() {
        position_required_items = new int[evaluation_length];
        count_items = 0;
    }

    public static void resetPositionSelectedItems() {
        position_selected_items = new int[evaluation_length];
        count_items = 0;
    }

    public static void startTimer() {
        start_time = SystemClock.elapsedRealtime();
    }

    public static void stopTimer(int index) {
        long end_time = SystemClock.elapsedRealtime();
        long elapsed_milli_sec = end_time - start_time;
        double elapsed_sec = elapsed_milli_sec / 1000.0;

        selection_time[index] = elapsed_sec;
    }

    public static void resetAll() {
        resetParticipantName();
        resetMenuCount();
        resetSelectionCount();
        resetWrongSelection();
        resetSelectionTime();
        resetItemFrequency();
        resetRequiredItems();
        resetSelectedItems();
        resetPositionRequiredItems();
        resetPositionSelectedItems();
    }

    public static String getRandomItem() {
        Random random = new Random();
        int random_nbr = random.nextInt(51);

        if(random_nbr<14) {
            return Utility.getOrderedItems()[most_frequent_items[0]];
        }
        else if(random_nbr<22) {
            return Utility.getOrderedItems()[most_frequent_items[1]];
        }
        else if(random_nbr<26) {
            return Utility.getOrderedItems()[most_frequent_items[2]];
        }
        else if(random_nbr<29) {
            return Utility.getOrderedItems()[most_frequent_items[3]];
        }
        else if(random_nbr<31) {
            return Utility.getOrderedItems()[most_frequent_items[4]];
        }
        else if(random_nbr<33) {
            return Utility.getOrderedItems()[most_frequent_items[5]];
        }
        else if(random_nbr<34) {
            return Utility.getOrderedItems()[most_frequent_items[6]];
        }
        else if(random_nbr<35) {
            return Utility.getOrderedItems()[most_frequent_items[7]];
        }
        else {
            return Utility.getOrderedItems()[random_nbr%16];
        }
    }

    /**
     *  Checks if external storage is available for read and write
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            Log.e("FILE", "permission granted");
        }
    }

    public static boolean printResults(Activity activity) {
        verifyStoragePermissions(activity);
        if(isExternalStorageWritable()) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DIR);
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DIR + FILE);

                if(!dir.exists()) {
                    if(!dir.mkdirs()) Log.e("FILE","Failed to create directory");

                }

                if(!file.exists()) {
                    if(!file.createNewFile()) Log.e("FILE","Failed to create file");
                }

                // output format is :
                // participant_name ; menu_count ; required_items[40] ; position_required_items[40]
                // ; selected_items ; position_selected_items[40] ; wrong_selection[40] ; error_rate
                // ; selection_time[40] ; avg_selection_time \n
                OutputStreamWriter stream_writer = new OutputStreamWriter(new FileOutputStream(file, true));

                double total_wrong_selection = 0;

                double total_selection_time = 0;

                stream_writer.append(participant_name+";"+menu_count+";");

                for(int i = 0 ; i<required_items.length ; i++) {
                    stream_writer.append(required_items[i]+";");
                }

                for(int i = 0 ; i<position_required_items.length ; i++) {
                    stream_writer.append(position_required_items[i]+";");
                }

                for(int i = 0 ; i<selected_items.length ; i++) {
                    stream_writer.append(selected_items[i]+";");
                }

                for(int i = 0 ; i<position_selected_items.length ; i++) {
                    stream_writer.append(position_selected_items[i]+";");
                }

                for(int i = 0 ; i<wrong_selection.length ; i++) {
                    stream_writer.append(wrong_selection[i]+";");
                    total_wrong_selection += wrong_selection[i];
                }
                stream_writer.append((total_wrong_selection/evaluation_length)+";");

                for(int i = 0 ; i<selection_time.length ; i++) {
                    stream_writer.append(selection_time[i]+";");
                    total_selection_time += selection_time[i];
                }
                stream_writer.append((total_selection_time/evaluation_length)+"\n");

                stream_writer.flush();
                stream_writer.close();
                return true;
            } catch (IOException e) {
                Log.e("FILE", "File write failed: " + e.toString());
                return false;
            }
        }
        else return false;
    }

    public static void selectionPerformed(Activity activity, View view, Button pressed_button, String required_item, int position_required_item, String selected_item, int position_selected_item) {
        Intent intent = new Intent(activity, MainActivity.class); // default activity

        // Take into account parameters only during the evaluation session
        if(!TRAINING_SESSION) {
            // Take into account a selection time
            Utility.stopTimer(selection_count-1);

            // Take into account a wrong answer
            String pressed_item = pressed_button.getText().toString();
            if(!TRAINING_SESSION && !required_item.equals(pressed_item))
                Utility.setWrongSelection(selection_count-1);

            // Take into account the required item, its position and the selected item
            required_items[count_items] = required_item;
            position_required_items[count_items] = position_required_item;
            selected_items[count_items] = selected_item;
            position_selected_items[count_items] = position_selected_item;
            count_items++;

            // for split menus only, inc the frequency
            if(activity instanceof SplitMenuActivity
                    || activity instanceof PaginatedSplitMenuActivity
                    || activity instanceof MultiColSplitMenuActivity
                    || activity instanceof MultiColPaginatedSplitMenuActivity){
                Utility.incItemFrequency(pressed_item);
            }
        }

        if(TRAINING_SESSION && selection_count==training_length && menu_count<8)
            intent = new Intent(activity, MenuIntroductionActivity.class);
        else if(TRAINING_SESSION && selection_count==training_length && menu_count==8) {
            TRAINING_SESSION = false; // training session is now over
            resetMenuCount();
            intent = new Intent(activity, MenuIntroductionActivity.class);
        }
        else if(!TRAINING_SESSION && selection_count==evaluation_length && menu_count<=8) { // after evaluation
            // must wait for the end of printing to continue
            // because of Activity dependence with the permission granting
            if(Utility.printResults(activity)) {
                intent = new Intent(activity, SurveyRequestActivity.class);
            }
        }
        else if(selection_count < evaluation_length)
            intent = new Intent(activity, NextSelectionActivity.class);

        activity.startActivity(intent);
    }

}
