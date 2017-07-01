package mx.gob.sedena.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import eu.siacs.conversations.R;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.ui.ConversationActivity;
import eu.siacs.conversations.ui.EditAccountActivity;
import eu.siacs.conversations.ui.XmppActivity;

public class WelcomeActivity extends XmppActivity {

	//public static String PASSFRASE = null;

	@Override
	protected void refreshUiReal() {

	}

	@Override
	protected void onBackendConnected() {

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.getString("primero?").equals("verdadero")) {
				//this.PASSFRASE = extras.getString("pass");
				if (getResources().getBoolean(R.bool.portrait_only)) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				}

				super.onCreate(savedInstanceState);

				XmppActivity.PASSFRASE =  extras.getString("pass");

				setContentView(R.layout.welcome);


				final ActionBar ab = getActionBar();
				if (ab != null) {
					ab.setDisplayShowHomeEnabled(false);
					ab.setDisplayHomeAsUpEnabled(false);
				}

				//final Button ingresar = (Button) findViewById(R.id.boton_ingresar);
				//final EditText pass = (EditText) findViewById(R.id.edit_pass);


				final Button cuenta = (Button) findViewById(R.id.registrarCuenta);
				cuenta.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						List<Account> accounts = xmppConnectionService.getAccounts();
						Intent intent = new Intent(WelcomeActivity.this, EditAccountActivity.class);
						if (accounts.size() == 1) {
							intent.putExtra("jid", accounts.get(0).getJid().toBareJid().toString());
							intent.putExtra("init", true);
						} else if (accounts.size() >= 1) {
							intent = new Intent(WelcomeActivity.this, ManageAccountActivity.class);
						}
						startActivity(intent);
					}
				});

			} else if (extras.getString("primero?").equals("falso")) {

				this.PASSFRASE = extras.getString("pass");
				super.onCreate(savedInstanceState);
				Intent intent = new Intent(WelcomeActivity.this, ConversationActivity.class);
				startActivity(intent);
			}

			//The key argument here must match that used in the other activity


		}


	}
}
