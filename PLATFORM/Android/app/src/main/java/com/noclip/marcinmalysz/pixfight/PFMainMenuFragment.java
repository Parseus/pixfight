package com.noclip.marcinmalysz.pixfight;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class PFMainMenuFragment extends Fragment {

    private static PFMainMenuFragment staticInstance = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pfmain_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        staticInstance = this;

        getView().findViewById(R.id.imageButtonNewGame).setOnClickListener(v -> goToFragment(new PFNewGameFragment()));
        getView().findViewById(R.id.imageButtonMultiplayer).setOnClickListener(v -> goToFragment(new PFMultiplayerFragment()));
        getView().findViewById(R.id.imageButtonLoadGame).setOnClickListener(v -> goToFragment(new PFLoadGameFragment()));
        getView().findViewById(R.id.imageButtonSettings).setOnClickListener(v -> goToFragment(new PFSettingsFragment()));
    }

    @Override
    public void onResume() {
        super.onResume();

        disposeClient();
    }

    private void disconnectPopToRoot() {

        requireActivity().runOnUiThread(() -> {

            FragmentManager fm = getParentFragmentManager();
            int count = fm.getBackStackEntryCount();
            for(int i = 0; i < count; ++i) {
                fm.popBackStackImmediate();
            }

            int duration = Toast.LENGTH_SHORT;
            CharSequence textFinish = "You have been disconnected.";

            Toast toast = Toast.makeText(requireContext(), textFinish, duration);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();
        });
    }

    private void goToFragment(Fragment fragment) {
        PFAudioWrapper.playSelectSound();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static native void disposeClient();

    @Keep
    public static void onDisconnectBridge() {

        if (staticInstance == null) {
            return;
        }

        staticInstance.disconnectPopToRoot();
    }
}
