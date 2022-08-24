package com.amaromerovic.contactswithroom;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amaromerovic.contactswithroom.adapter.RecyclerViewAdapter;
import com.amaromerovic.contactswithroom.databinding.ActivityMainBinding;
import com.amaromerovic.contactswithroom.model.Contact;
import com.amaromerovic.contactswithroom.model.ContactViewModel;
import com.amaromerovic.contactswithroom.ui.SwipeToCall;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {

    public static final String CONTACT_ID_KEY = "CONTACT_ID_KEY";
    private ContactViewModel contactViewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        mainBinding.recyclerView.setHasFixedSize(true);
        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(ContactViewModel.class);


        contactViewModel.getAllContacts().observe(this, contacts -> {
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, MainActivity.this, this);
            mainBinding.recyclerView.setAdapter(recyclerViewAdapter);
            for (Contact contact : contacts) {
                Log.d("ForLoopMain", "onCreate: " + contact.getImage() + " " + contact.getId() + " " + contact.getFirstName());
            }
        });

        mainBinding.addContactButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            activityResultLauncher.launch(intent);
        });

        swipeToMessageOrCall();

    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult (
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == RESULT_OK) {
                    Intent newContactIntent = result.getData();
                    assert newContactIntent != null;
                    String image = newContactIntent.getStringExtra(NewContact.IMAGE_KEY);
                    String firstName = newContactIntent.getStringExtra(NewContact.FIRST_NAME_KEY);
                    String lastName = newContactIntent.getStringExtra(NewContact.LAST_NAME_KEY);
                    String occupation = newContactIntent.getStringExtra(NewContact.OCCUPATION_KEY);
                    String phoneNumber = newContactIntent.getStringExtra(NewContact.PHONE_NUMBER_KEY);
                    ContactViewModel.addContact(new Contact(image, firstName, lastName, occupation, phoneNumber));
                }
            });


    @Override
    public void onContactClick(int position) {
        Contact contact = Objects.requireNonNull(contactViewModel.getAllContacts().getValue()).get(position);
        Intent intent = new Intent(MainActivity.this, NewContact.class);
        intent.putExtra(CONTACT_ID_KEY, contact.getId());
        startActivity(intent);

    }

    @Override
    public void onDeleteButtonClicked(Contact contact) {
        Snackbar snackbar = Snackbar.make(mainBinding.recyclerView, "Contact is deleted!", 3000)
                .setAction("UNDO", view -> {
                    ContactViewModel.addContact(contact);
                    Snackbar snackbarOne = Snackbar.make(mainBinding.recyclerView, "Contact is restored!", 1500);
                    snackbarOne.setBackgroundTint(Color.BLACK);
                    view = snackbarOne.getView();
                    TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
                    if(tv!=null) {
                        tv.setTextSize(20);
                        tv.setBackgroundColor(Color.BLACK);
                        tv.setTextColor(ContextCompat.getColor(this, R.color.backgroundColor));
                    }
                    snackbarOne.show();
                });

        View view = snackbar.getView();
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        snackbar.setBackgroundTint(Color.BLACK);
        TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
        TextView actionTextView = view.findViewById(com.google.android.material.R.id.snackbar_action);
        if (actionTextView != null) {
            actionTextView.setTextSize(30);
            actionTextView.setBackgroundColor(Color.BLACK);
            actionTextView.setTextColor(ContextCompat.getColor(this, R.color.purple_700));
        }
        if(tv != null) {
            tv.setBackgroundColor(Color.BLACK);
            tv.setTextSize(20);
            tv.setTextColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
        view.setBackgroundColor(Color.BLACK);
        snackbar.show();

    }


    private void swipeToMessageOrCall() {
        SwipeToCall swipeToCall = new SwipeToCall(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Contact contact = Objects.requireNonNull(contactViewModel.getAllContacts().getValue()).get(viewHolder.getAdapterPosition());
                if (i >=8){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                    startActivity(intent);
                    recyclerViewAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                } else {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:" + contact.getPhoneNumber()));
                    sendIntent.putExtra("sms_body", "");
                    startActivity(sendIntent);
                    recyclerViewAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToCall);
        itemTouchhelper.attachToRecyclerView(mainBinding.recyclerView);

    }

}