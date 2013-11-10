package it.sportingnola.criptocalcolo;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EnigmiAdapter extends ArrayAdapter<EnigmaString> {
    CriptoCalcoloActivity activity;
    ArrayList<EnigmaString> data;

    public EnigmiAdapter(Context context, int textViewResourceId, ArrayList<EnigmaString> data) {
	super(context, textViewResourceId, data);
	activity=(CriptoCalcoloActivity) context;
	this.data=data;
    }

    // This view starts when we click the spinner.
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
	View row=convertView;
	if (row == null) {
	    LayoutInflater inflater=activity.getLayoutInflater();
	    row=inflater.inflate(R.layout.spinner, parent, false);
	}
	EnigmaString item=data.get(position);
	boolean risolto=activity.isEnigmaRisolto(position);
	if (item != null) {
	    TextView tveni=(TextView) row.findViewById(R.id.enigmaNum);
	    if (tveni != null) {
		tveni.setText(Integer.toString(position + 1));
		if ( ! risolto) {
		    tveni.setBackgroundColor(Color.RED);
		}
		else {
		    tveni.setBackgroundColor(Color.GREEN);
		}
	    }
	    TextView tvris=(TextView) row.findViewById(R.id.isRisolto);
	    if (tvris != null) {
		if ( ! risolto) {
		    tvris.setBackgroundColor(Color.RED);
		    tvris.setText(R.string.nonrisolto);
		}
		else {
		    tvris.setBackgroundColor(Color.GREEN);
		    tvris.setText(R.string.risolto);
		}
	    }
	}
	return row;
    }

}
