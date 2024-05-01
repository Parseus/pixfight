package com.noclip.marcinmalysz.pixfight;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PFMultiplayerFragment extends Fragment {

    private Button makeRoomButton = null;
    private Button joinRoomButton = null;
    private Switch privateSwitch = null;
    private boolean connected = false;

    private static PFMultiplayerFragment staticInstance = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pfmultiplayer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //back
        Button backButton = getView().findViewById(R.id.multibutton_back);
        backButton.setOnClickListener(arg0 -> getParentFragmentManager().popBackStack());

        makeRoomButton = getView().findViewById(R.id.imageButtonMakeRoom);
        joinRoomButton = getView().findViewById(R.id.imageButtonJoinRoom);
        privateSwitch = getView().findViewById(R.id.privateSwitch);

        makeRoomButton.setOnClickListener(v -> {

            if (!connected) {
                return;
            }

            makeServerRoom(privateSwitch.isChecked());
        });

        joinRoomButton.setOnClickListener(v -> {

            goToFragment(new PFJoinRoomFragment());
        });

        disableLayout();
        initializeClient();

        connected = connectToServer();

        if (!connected) {

            int duration = Toast.LENGTH_SHORT;
            CharSequence textFinish = "Could not connect to server, please try again.";

            Toast toast = Toast.makeText(requireContext(), textFinish, duration);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();

            getParentFragmentManager().popBackStack();

            return;
        }

        enableLayout();
    }

    @Override
    public void onResume() {
        super.onResume();

        staticInstance = this;
    }


    @Override
    public void onStop() {
        super.onStop();

        staticInstance = null;
    }

    private void navigateToRoom() {

        requireActivity().runOnUiThread(() -> {

            Bundle bundle = new Bundle();
            bundle.putBoolean("master", true);

            PFMakeRoomFragment roomFragment = new PFMakeRoomFragment();
            roomFragment.setArguments(bundle);

            goToFragment(roomFragment);
        });
    }

    private void disableLayout() {

        makeRoomButton.setEnabled(false);
        joinRoomButton.setEnabled(false);
        privateSwitch.setEnabled(false);
    }

    private void enableLayout() {

        makeRoomButton.setEnabled(true);
        joinRoomButton.setEnabled(true);
        privateSwitch.setEnabled(true);
    }

    private void goToFragment(Fragment fragment) {
        PFAudioWrapper.playSelectSound();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //NDK

    public static native void initializeClient();
    public static native boolean connectToServer();
    public static native void makeServerRoom(boolean privateroom);

    //NDK Bridge

    @Keep
    public static void onRoomEventBridge() {

        if (staticInstance == null) {
            return;
        }

        staticInstance.navigateToRoom();
    }
}
