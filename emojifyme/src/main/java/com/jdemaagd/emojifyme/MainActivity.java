package com.jdemaagd.emojifyme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";

    @BindView(R.id.image_view)
    ImageView mImageView;

    @BindView(R.id.emojify_button)
    Button mEmojifyButton;

    @BindView(R.id.share_button)
    FloatingActionButton mShareFab;

    @BindView(R.id.save_button)
    FloatingActionButton mSaveFab;

    @BindView(R.id.clear_button)
    FloatingActionButton mClearFab;

    @BindView(R.id.title_text_view)
    TextView mTitleTextView;

    private String mTempPhotoPath;
    private Bitmap mResultsBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            processAndSetImage();
        } else {
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    @OnClick(R.id.clear_button)
    public void clearImage() {
        mImageView.setImageResource(0);
        mEmojifyButton.setVisibility(View.VISIBLE);
        mTitleTextView.setVisibility(View.VISIBLE);
        mShareFab.setVisibility(View.GONE);
        mSaveFab.setVisibility(View.GONE);
        mClearFab.setVisibility(View.GONE);

        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
    }

    /**
     * "Emojify Me!" Launches camera app
     */
    @OnClick(R.id.emojify_button)
    public void emojifyMe() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            launchCamera();
        }
    }

    @OnClick(R.id.save_button)
    public void saveMe() {
        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        BitmapUtils.saveImage(this, mResultsBitmap);
    }

    @OnClick(R.id.share_button)
    public void shareMe() {
        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        BitmapUtils.saveImage(this, mResultsBitmap);
        BitmapUtils.shareImage(this, mTempPhotoPath);
    }

    /**
     * Create temporary image file and capture a picture to store in it
     */
    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                mTempPhotoPath = photoFile.getAbsolutePath();

                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY, photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Process captured image and setting it to TextView
     */
    private void processAndSetImage() {
        mEmojifyButton.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
        mSaveFab.setVisibility(View.VISIBLE);
        mShareFab.setVisibility(View.VISIBLE);
        mClearFab.setVisibility(View.VISIBLE);

        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);
        mResultsBitmap = Emojifier.detectFacesAndOverlayEmoji(this, mResultsBitmap);
        mImageView.setImageBitmap(mResultsBitmap);
    }
}