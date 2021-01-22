package com.example.trackapiapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTrackDialog extends DialogFragment{

    public interface EditDialogListener {
        public void onDialogSongEdited(DialogFragment dialog);
        public void errorEditingSong(DialogFragment dialog);
        public void deletedSong(DialogFragment dialog);
    }

    EditTrackDialog.EditDialogListener listener;

    private TextInputLayout nameInput, singerInput;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (EditTrackDialog.EditDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("activity" + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialog = inflater.inflate(R.layout.popup_layout,null );
        nameInput = dialog.findViewById(R.id.nameInputLayout);
        singerInput = dialog.findViewById(R.id.singerInputLayout);
        nameInput.getEditText().setText(MainActivity.clickedSong);
        singerInput.getEditText().setText(MainActivity.clickedArtist);
        builder.setTitle("Editar Track");
        builder.setView(dialog);
        builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                String name = nameInput.getEditText().getText().toString();
                String singer = singerInput.getEditText().getText().toString();
                Track newTrack = new Track(name, singer, MainActivity.clickedID);
                Call<Track> upTrack = MainActivity.service.updateTrack(newTrack);
                upTrack.enqueue(new Callback<Track>() {
                    @Override
                    public void onResponse(Call<Track> call, Response<Track> response) {
                        listener.onDialogSongEdited(EditTrackDialog.this);
                    }
                    @Override
                    public void onFailure(Call<Track> call, Throwable t) {
                        listener.errorEditingSong(EditTrackDialog.this);
                    }
                });
                EditTrackDialog.this.getDialog().cancel();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditTrackDialog.this.getDialog().cancel();
            }
        });
        builder.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Void> delTrack = MainActivity.service.deleteTrack(MainActivity.clickedID);
                delTrack.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        listener.deletedSong(EditTrackDialog.this);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                EditTrackDialog.this.getDialog().cancel();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
