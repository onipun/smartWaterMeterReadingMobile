package com.example.meterreading.ui.ui.HouseList;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meterreading.R;
import com.example.meterreading.service.VolleyMultipartRequest;
import com.example.meterreading.service.VolleySingleton;
import com.example.meterreading.service.ExifUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Home extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    TextView houseId, address;
    String identityCard, currentPhotoPath;
    EditText editText;
    CheckBox checkBox;
    ImageView imageView;
    ProgressDialog dialog = null;
    TextView messageText = null;
    String upLoadServerUri = "http://192.168.43.173:8080/upload/img";
    String saveResult = "http://192.168.43.173:8080/upload/save";
    private static final int GALLERY_REQUEST = 1889;
    private static final String TAG = Home.class.getSimpleName();
    ProgressDialog progressDialog;
    Bitmap bitmap;
    ExifUtils ExifUtil = new ExifUtils();
    Uri photoURI;
    boolean checkBoxIsCheck = false;
    String imRead = "", isTrain = "no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        houseId = findViewById(R.id.txtHouseId);
        address = findViewById(R.id.txtAddress);
        imageView = findViewById(R.id.imgMeterView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        messageText = findViewById(R.id.messageText);
        checkBox = findViewById(R.id.checkBox);
        checkBox.setVisibility(View.INVISIBLE);
        editText = findViewById(R.id.meterReadAlt);
        editText.setVisibility(View.INVISIBLE);


        Intent intent = getIntent();
        String houseID = intent.getStringExtra("houseID");
        String addr = intent.getStringExtra("addr");
        identityCard = intent.getStringExtra("identityCard");
        Log.d(TAG, "identityCard: " + identityCard);

        this.houseId.setText(houseID);
        this.address.setText(addr);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxIsCheck = true;
                    editText.setEnabled(false);
                    editText.setVisibility(View.INVISIBLE);
                } else
                    checkBoxIsCheck = false;
                editText.setEnabled(true);
                editText.setVisibility(View.VISIBLE);
            }
        });

        confirmationBox();
    }

    public void getImage(View view) {
        dispatchTakePictureIntent();
    }


    public void getImageFromMedia(View view) {
        dispatchTakeMediaPictureIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            try {
                Uri imageUri = data.getData();
                final String imagePath = getRealPathFromURI(imageUri);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                final Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePath, selectedImage);

                bitmap = selectedImage;
                imageView.setImageURI(imageUri);


                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                messageText.setText("uploading started.....");
                            }
                        });

                        try {
                            uploadImg();
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    messageText.setText("check logcat");
                                    dialog.cancel();
                                }
                            });
                            Log.d(TAG, "run: ", e);
                            dialog.cancel();
                        }

                    }
                }).start();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {
                set_send_Pic();
            } catch (Exception e) {
                Log.e(TAG, "onActivityResult: ", e);
                Toast.makeText(this, "Something went wrong on capture image", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            try {
                Uri imageUri = data.getData();
                final String imagePath = getRealPathFromURI(imageUri);
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePath, selectedImage);

                bitmap = selectedImage;
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bp);

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                messageText.setText("uploading started.....");
                            }
                        });

                        try {
                            //uploadImg();
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    messageText.setText("check logcat");
                                    dialog.cancel();
                                }
                            });
                            Log.d(TAG, "run: ", e);
                            dialog.cancel();
                        }

                    }
                }).start();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "onActivityResult: ", e);
            }


        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadImg() {


        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progressDialog = new ProgressDialog(Home.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.show();
            }
        });

        //resize image taken

        bitmap = resizeImageDimention(bitmap, 1280, 960);

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] imageData = baos.toByteArray();


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, upLoadServerUri, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressDialog.dismiss();
                String resultResponse = new String(response.data);
                messageText.setText("");
                checkBox.setText(resultResponse);
                checkBox.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                Log.d(TAG, "onResponse: " + resultResponse);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e(TAG, "Error Status " + status);
                        Log.e(TAG, "Error Message " + message);


                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("image", new DataPart("file_avatar.jpg", imageData, "image/jpeg"));

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("identityCard", identityCard);
                params.put("isTrain", isTrain);
                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);

    }

    public void saveInfo(View view) {
        Boolean valid = false;

        if (checkBoxIsCheck) {
            imRead = String.valueOf(checkBox.getText());
            valid = true;
        } else {
            if (TextUtils.isEmpty(editText.getText()) || editText.getText().length() > 4 || editText.getText().length() < 4) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("please insert meter number with front 4 digits");
                        messageText.setFocusable(true);
                    }
                });
            } else {
                imRead = String.valueOf(editText.getText());
                valid = true;
            }

        }

        if (valid) {


            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, saveResult,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            messageText.setText(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    messageText.setText("That didn't work! / might be no prev data");
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("imRead", imRead);
                    params.put("identityCard", identityCard);
                    return params;
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        Log.d(TAG, "getRealPathFromURI: " + cursor.getString(idx));
        return cursor.getString(idx);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        try {
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e(TAG, "dispatchTakePictureIntent: " + ex);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(this,
                            "com.example.meterreading.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "dispatchTakePictureIntent: check permission for camera in setting", e);
        }
    }

    private void set_send_Pic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        imageView.setImageBitmap(bitmap);

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("uploading started.....");
                    }
                });

                try {
                    uploadImg();
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            messageText.setText("check logcat");
                            dialog.cancel();
                        }
                    });
                    Log.d(TAG, "run: ", e);
                    dialog.cancel();
                }

            }
        }).start();


    }

    private void dispatchTakeMediaPictureIntent() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageText.setText(messageText.getText().toString() + ".. This is your picture ");
            }
        });
    }

    private Bitmap resizeImageDimention(Bitmap srcImage, int width, int height) {

        Bitmap resized = Bitmap.createScaledBitmap(srcImage, width, height, true);

        return resized;
    }

    public void confirmationBox(){
        new AlertDialog.Builder(Home.this)
                .setTitle("Trainning session")
                .setMessage("do you intent to make this as training session?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        isTrain = "yes";
                        Toast.makeText(Home.this, "please sort image before continue to new session", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isTrain = "no";
                    }
                }).show();
    }

}









