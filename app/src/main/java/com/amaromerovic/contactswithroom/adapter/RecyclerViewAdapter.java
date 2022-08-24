package com.amaromerovic.contactswithroom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amaromerovic.contactswithroom.R;
import com.amaromerovic.contactswithroom.model.Contact;
import com.amaromerovic.contactswithroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final OnContactClickListener onContactClickListener;
    private final List<Contact> contactList;
    private final Context context;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, OnContactClickListener onContactClickListener) {
        this.contactList = contactList;
        this.context = context;
        this.onContactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(view, onContactClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = Objects.requireNonNull(contactList.get(position));
        Bitmap bitmap = null;
        Uri uri = Uri.parse(contact.getImage());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageBitmap(bitmap);
        holder.fistName.setText(contact.getFirstName().trim());
        holder.lastName.setText(contact.getLastName().trim());
        holder.deleteButton.setOnClickListener(view -> {
            onContactClickListener.onDeleteButtonClicked(contact);
            ContactViewModel.deleteContact(contact);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnContactClickListener onContactClickListener;
        public ImageView imageView;
        public TextView fistName;
        public TextView lastName;
        public FloatingActionButton deleteButton;

        public ViewHolder(@NonNull View itemView, OnContactClickListener onContactClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewRow);
            fistName = itemView.findViewById(R.id.rowFirstNameTextView);
            lastName = itemView.findViewById(R.id.rowLastNameTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            this.onContactClickListener = onContactClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            onContactClickListener.onContactClick(getAdapterPosition());
        }
    }


    public interface OnContactClickListener {
        void onContactClick(int position);
        void onDeleteButtonClicked(Contact contact);
    }
}
