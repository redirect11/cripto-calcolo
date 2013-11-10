/**
 * 
 */
package it.sportingnola.criptocalcolo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * @author paolo
 */
public class PopupDialog extends Dialog {

    CriptoCalcoloActivity ctx;

    private int height=0;
    private int width=0;

    /**
     * @param context
     */
    public PopupDialog(Context context) {
	super(context);
	ctx=(CriptoCalcoloActivity) context;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	TableLayout ll=(TableLayout) LayoutInflater.from(ctx).inflate(R.layout.popup, null);
	setContentView(ll);
	Display display=ctx.getDisplay();
	// ci sono cinque righe, ma divido per sei per avere margine
	height=display.getHeight() / 6;
	width=height;
	settaDimensioneImmagine((TableRow) findViewById(R.id.tableRow1));
	settaDimensioneImmagine((TableRow) findViewById(R.id.tableRow2));
	settaDimensioneImmagine((TableRow) findViewById(R.id.tableRow3));
	settaDimensioneImmagine((TableRow) findViewById(R.id.tableRow4));
    }

    private void settaDimensioneImmagine(TableRow ll) {
	ImageView iv=null;
	for (int i=0; i < ll.getChildCount(); i++) {
	    iv=(ImageView) ll.getChildAt(i);
	    iv.getLayoutParams().height=height;
	    iv.getLayoutParams().width=width;
	}
    }

}
