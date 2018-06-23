package com.weilay.pos.util;


import android.app.Activity;
import android.os.Build;
import android.view.View;

import com.framework.utils.L;

public class NavigationBarHide {
	public static void toggleHideyBar(Activity activity) {

		// The UI options currently enabled are represented by a bitfield.
		// getSystemUiVisibility() gives us that bitfield.
		int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
		int newUiOptions = uiOptions;
		boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
		if (isImmersiveModeEnabled) {
			L.i("gg", "Turning immersive mode mode off. ");
		} else {
			L.i("gg", "Turning immersive mode mode on.");
		}

		// Navigation bar hiding: Backwards compatible to ICS.
		if (Build.VERSION.SDK_INT >= 14) {
			newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		}

		// Status bar hiding: Backwards compatible to Jellybean
		if (Build.VERSION.SDK_INT >= 16) {
			newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
		}

		// Immersive mode: Backward compatible to KitKat.
		// Note that this flag doesn't do anything by itself, it only augments
		// the behavior
		// of HIDE_NAVIGATION and FLAG_FULLSCREEN. For the purposes of this
		// sample
		// all three flags are being toggled together.
		// Note that there are two immersive mode UI flags, one of which is
		// referred to as "sticky".
		// Sticky immersive mode differs in that it makes the navigation and
		// status bars
		// semi-transparent, and the UI flag does not get cleared when the user
		// interacts with
		// the screen.
		if (Build.VERSION.SDK_INT >= 18) {
			newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		}

		activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
	}
}
