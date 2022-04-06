package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.test.HelperClasses.Hinhanh;
import com.example.test.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainPost extends AppCompatActivity {
    Spinner spinner,spinnertinhtrang,khuvuc;
    ArrayList<Hinhanh> mangchinh;
    ImageView imageView;
    EditText Tensp,gia,noidung;
    AppCompatButton post;
    FirebaseAuth mAuth;
    private  String idcustomer;
    private  String id;
    FirebaseUser mUser;
    DatabaseReference mRef;
    private static final int SELECT_PICTURE = 1;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference Mdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_post);
        Mdata = FirebaseDatabase.getInstance().getReference().child("sanpham");
        imageView=findViewById(R.id.IMGSP);
       gia=findViewById(R.id.Giasp1);
        DecimalFormat formatter = new DecimalFormat("###,###,###");

        load();
        Tensp=findViewById(R.id.post_liquidation_name);
       noidung=findViewById(R.id.post_liquidation_desc);
       post=findViewById(R.id.post_liquidation_btn);
        khuvuc = findViewById(R.id.khhuvuc);
        spinnertinhtrang=findViewById(R.id.spinnertinhtrang);
        String y[] ={"TP,HCM","Vĩnh Long","CÀ Mau","Bến tre","Tiền Giang",
                "Sóc Trăng","Bạc Liêu","An Giang","Kiên Giang","Cần Thơ"};
        ArrayAdapter adapt=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,y);
        adapt.setDropDownViewResource(android.R.layout.select_dialog_multichoice);

        khuvuc.setAdapter(adapt);

        String t[] ={"Mới 100%","Đã khui","Đã qua sử dụng","Đã tân Trang"};
        ArrayAdapter adapte=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,t);
        adapte.setDropDownViewResource(android.R.layout.select_dialog_multichoice);
        spinnertinhtrang.setAdapter(adapte);

        //select =(Button) view.findViewById(R.id.selectig);
        spinner =findViewById(R.id.spinner);
        String m[] = {"Nội -Ngoại Thất","Đồ Điện Tử","Xe Cộ","Thời Trang","Mẹ và bé","Giải Trí-Thể Thao","Đồ Văn Phòng","Dịch Vụ"};
        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,m);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_multichoice);

        spinner.setAdapter(adapter);
        StorageReference storageRef = storage.getReferenceFromUrl("gs://thanh-l-c.appspot.com");
      post.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (Tensp.getText().toString().length()!=0 && noidung.getText().toString().length()!=0 ) {
                  Calendar calendar = Calendar.getInstance();
                  StorageReference mountainsRef = storageRef.child("imgae" + calendar.getTimeInMillis() + ".png");

                  imageView.setDrawingCacheEnabled(true);
                  imageView.buildDrawingCache();
                  Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                  ByteArrayOutputStream baos = new ByteArrayOutputStream();
                  bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                  byte[] data = baos.toByteArray();

                  UploadTask uploadTask = mountainsRef.putBytes(data);
                  Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                      @Override
                      public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                          if (!task.isSuccessful()) {
                              throw task.getException();
                          }

                          // Continue with the task to get the download URL
                          return mountainsRef.getDownloadUrl();
                      }
                  }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                      @Override
                      public void onComplete(@NonNull Task<Uri> task) {
                          if (task.isSuccessful()) {
                              Uri downloadUri = task.getResult();
                              Toast.makeText(getApplicationContext(), "Thành Công", Toast.LENGTH_SHORT).show();
                              //Log.d("AAAA",down+"");
                              String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                              String key = Mdata.push().getKey();
                              Hinhanh hinhanh = new Hinhanh(idcustomer,currentDate,khuvuc.getSelectedItem().toString(),spinnertinhtrang.getSelectedItem().toString(),spinner.getSelectedItem().toString(),key,Tensp.getText().toString(), gia.getText().toString(), noidung.getText().toString(), String.valueOf(downloadUri), id);

                              //tạo node trên database



                              Mdata.child(key).setValue(hinhanh, new DatabaseReference.CompletionListener() {
                                  @Override
                                  public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                      if (error != null) {
                                          Toast.makeText(getApplicationContext(), "k Thành Công", Toast.LENGTH_SHORT).show();
                                      } else {
                                          Toast.makeText(getApplicationContext(), "Lưu Thành Công", Toast.LENGTH_SHORT).show();
                                          //getActivity().finish();
                                      }
                                  }

                              });


                          } else {
                              // Handle failures
                              // ...
                          }

                      }


                  });
              }else {
                  Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ!!!", Toast.LENGTH_SHORT).show();
              }
          }

      });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, SELECT_PICTURE);*/
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , SELECT_PICTURE);
            }
        });


    }
    public void load()
    {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mRef.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String ID=snapshot.getKey();
                    String address = snapshot.child("address").getValue(String.class);
                    String fullName = snapshot.child("fullName").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String phoneNo = snapshot.child("phoneNo").getValue(String.class);
                    String profileImg = snapshot.child("profileImg").getValue(String.class);
                    String username = snapshot.child("username").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                        id=phoneNo;
                        idcustomer=ID;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainPost.this, "Xin lỗi ! Có gì đó lỗi rồi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream =getApplicationContext(). getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
        }
    }
}