/**
 * 
 */
package it.sportingnola.criptocalcolo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TableLayout;

/**
 * @author paolo
 */
public class PopupDialog extends Dialog {

    Context ctx;

    /**
     * @param context
     */
    public PopupDialog(Context context) {
	super(context);
	ctx=context;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	TableLayout ll=(TableLayout) LayoutInflater.from(ctx).inflate(R.layout.popup, null);
	setContentView(ll);
    }

}
