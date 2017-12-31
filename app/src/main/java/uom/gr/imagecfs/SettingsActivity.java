package uom.gr.imagecfs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import uom.gr.imagecfs.data.ImageEntry;

public class SettingsActivity extends PreferenceActivity  implements Preference.OnPreferenceClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_settings);
        Preference preference  =findPreference("reset");
        preference.setOnPreferenceClickListener(this);
;

    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.equals(findPreference("reset"))) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.reset_db)
                    .setMessage("Do you really want to reset?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            SettingsActivity.this.getContentResolver().delete(ImageEntry.SafeTable.CONTENT_URI,null,null);
                            Toast.makeText(SettingsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }})
                    .setNegativeButton(R.string.no, null).show();
        }
        return true;
    }
}


