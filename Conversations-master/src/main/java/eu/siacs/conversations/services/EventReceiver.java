package eu.siacs.conversations.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import eu.siacs.conversations.Config;
import mx.gob.sedena.persistance.DatabaseBackend;

public class EventReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent mIntentForService = new Intent(context,XmppConnectionService.class);
		if (intent.getAction() != null) {
			mIntentForService.setAction(intent.getAction());
		} else {
			mIntentForService.setAction("other");
		}
		final String action = intent.getAction();

		if (action.equals("ui") || DatabaseBackend.getInstance(context).hasEnabledAccounts()) {
			context.startService(mIntentForService);
			Log.v("bad","Event receiver");


		}
	}

}
