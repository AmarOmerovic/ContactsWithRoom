package com.amaromerovic.contactswithroom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.amaromerovic.contactswithroom.databinding.ActivityNewContactBinding;
import com.amaromerovic.contactswithroom.model.Contact;
import com.amaromerovic.contactswithroom.model.ContactViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;

public class NewContact extends AppCompatActivity {
    private ActivityNewContactBinding binding;
    public static final int GALLERY_PERM_CODE = 101;
    public static final int CAMERA_PERM_CODE = 102;
    public static final String IMAGE_KEY = "image";
    public static final String FIRST_NAME_KEY = "firstName";
    public static final String LAST_NAME_KEY = "lastName";
    public static final String PHONE_NUMBER_KEY = "phoneNumber";
    public static final String OCCUPATION_KEY = "occupation";
    private ContactViewModel contactViewModel;
    private int contactID = 0;
    private boolean isEdited = false;
    private String imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        binding = DataBindingUtil.setContentView(NewContact.this, R.layout.activity_new_contact);


        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(NewContact.this.getApplication()).create(ContactViewModel.class);

        if (getIntent().hasExtra(MainActivity.CONTACT_ID_KEY)) {
            contactID = getIntent().getIntExtra(MainActivity.CONTACT_ID_KEY, 0);

            contactViewModel.getContact(contactID).observe(this, contact -> {
                if (contact != null) {

                    Bitmap bitmap = null;
                    imageUri = contact.getImage();
                    Uri uri = Uri.parse(contact.getImage());
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    binding.imageView.setImageBitmap(bitmap);

                    binding.firstNameEditText.setHint(contact.getFirstName());
                    binding.lastNameEditText.setHint(contact.getLastName());
                    binding.occupationEditText.setHint(contact.getOccupation());
                    binding.phoneNumberEditText.setHint(contact.getPhoneNumber());

                    binding.imageView2.setVisibility(View.GONE);
                }
            });

            isEdited = true;
        }

        if (!isEdited){
            binding.imageView.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }

        binding.imageView.setOnClickListener(view -> showBottomSheetDialog());


        binding.backButton.setOnClickListener(view -> finish());

        binding.saveButton.setOnClickListener(view -> {
            Intent intent = new Intent();


            String firstName = binding.firstNameEditText.getText().toString().trim();
            String lastName = binding.lastNameEditText.getText().toString().trim();
            String occupation = binding.occupationEditText.getText().toString().trim();
            String phoneNumber = binding.phoneNumberEditText.getText().toString().trim();

                if (!isEdited) {
                    if (!firstName.isEmpty() && !lastName.isEmpty() && !occupation.isEmpty() && !phoneNumber.isEmpty() && !imageUri.isEmpty()) {
                        intent.putExtra(IMAGE_KEY, imageUri);
                        intent.putExtra(FIRST_NAME_KEY, firstName);
                        intent.putExtra(LAST_NAME_KEY, lastName);
                        intent.putExtra(OCCUPATION_KEY, occupation);
                        intent.putExtra(PHONE_NUMBER_KEY, phoneNumber);
                        setResult(RESULT_OK, intent);
                    }
                } else {
                    if (!imageUri.isEmpty()){
                        if (firstName.isEmpty()){
                            firstName = binding.firstNameEditText.getHint().toString().trim();
                        }
                        if (lastName.isEmpty()){
                            lastName = binding.lastNameEditText.getHint().toString().trim();
                        }
                        if (occupation.isEmpty()){
                            occupation = binding.occupationEditText.getHint().toString().trim();
                        }
                        if (phoneNumber.isEmpty()){
                            phoneNumber = binding.phoneNumberEditText.getHint().toString().trim();
                        }
                        Contact contact = new Contact();
                        contact.setImage(imageUri);
                        contact.setId(contactID);
                        contact.setFirstName(firstName);
                        contact.setLastName(lastName);
                        contact.setOccupation(occupation);
                        contact.setPhoneNumber(phoneNumber);
                        ContactViewModel.updateContact(contact);
                    }
                }
            finish();
        });


    }


    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        ImageView cancel = bottomSheetDialog.findViewById(R.id.cancel);
        ImageView gallery = bottomSheetDialog.findViewById(R.id.gallery);
        bottomSheetDialog.show();

        assert cancel != null;
        cancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        assert gallery != null;
        gallery.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_PERM_CODE);
        });


    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryResultLauncher.launch(galleryIntent);
    }


    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
        else {
            openGallery();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
            else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ActivityResultLauncher<Intent> galleryResultLauncher = registerForActivityResult (
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    Uri uri = data.getData();
                    imageUri = uri.toString();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    binding.imageView.setColorFilter(null);
                    binding.imageView.setImageBitmap(bitmap);
                }
    });


}