package com.noclip.marcinmalysz.pixfight;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;

public class PFLoadGameFragment extends Fragment {

    private TableLayout tableView = null;
    private Button loadButton = null;
    private Button deleteButton = null;
    private File[] files = null;
    private int selectedFile = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pfload_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tableView = getView().findViewById(R.id.saveFilesTable);

        Button backButton = getView().findViewById(R.id.loadgame_back);
        backButton.setOnClickListener(arg0 -> getParentFragmentManager().popBackStack());

        loadButton = getView().findViewById(R.id.loadgamebuttonstart);
        loadButton.setOnClickListener(arg0 -> loadGame());

        deleteButton = getView().findViewById(R.id.loadgamebuttondelete);
        deleteButton.setOnClickListener(arg0 -> deleteSave());

        loadSaveFiles();
    }

    private void loadGame() {

        if (selectedFile == -1) {
            return;
        }

        File dir = new File(Environment.getExternalStorageDirectory() + "/PIXFIGHTDATA", "save");
        File fileToLoad = files[selectedFile];
        File fullPath = new File(dir, fileToLoad.getName());

        if (!fullPath.exists()) {
            return;
        }

        Bundle bundle =  new Bundle();
        bundle.putString("savepath", fullPath.toString());

        PFRenderFragment renderFragment = new PFRenderFragment();
        renderFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, renderFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void deleteSave() {

        if (selectedFile == -1) {
            return;
        }

        File dir = new File(Environment.getExternalStorageDirectory() + "/PIXFIGHTDATA", "save");
        File fileToDelete = files[selectedFile];

        File fullPath = new File(dir, fileToDelete.getName());

        if (!fullPath.delete()) {
            return;
        }

        loadSaveFiles();
    }

    private void loadSaveFiles() {

        File dir = new File(Environment.getExternalStorageDirectory() + "/PIXFIGHTDATA", "save");

        if (!dir.exists()) {
            loadButton.setEnabled(false);
            deleteButton.setEnabled(false);
            return;
        }

        tableView.removeAllViews();
        files = dir.listFiles();

        TableRow tr;
        TextView tv;

        for (File file : files) {
            tr = new TableRow(getContext());
            tv = new TextView(getContext());

            tv.setTextSize(18);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setText(file.getName());
            tv.setTextColor(Color.BLACK);

            tr.setClickable(true);
            tr.setMinimumHeight(32);
            tr.setPadding(0, 15, 0, 15);
            tr.addView(tv);
            tr.setOnClickListener(tablerowOnClickListener);

            tableView.addView(tr);
        }

        boolean isEmpty = files.length == 0;

        if (isEmpty) {
            loadButton.setEnabled(false);
            deleteButton.setEnabled(false);
            return;
        }

        selectedFile = 0;
        View row = tableView.getChildAt(0);
        row.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.rowSelected));
    }

    private final View.OnClickListener tablerowOnClickListener = v -> {

        for (int i = 0; i < tableView.getChildCount(); ++i) {

            View row = tableView.getChildAt(i);

            if (row.equals(v)) {

                row.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.rowSelected));
            }
            else {

                row.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
            }
        }

        selectedFile = tableView.indexOfChild(v);
    };
}
