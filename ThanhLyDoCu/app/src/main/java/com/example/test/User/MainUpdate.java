package com.example.test.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainUpdate extends AppCompatActivity {
    Spinner spinner,spinnertinhtrang,khuvuc;
    ArrayList<Hinhanh> mangchinh;
    ImageView imageView;

    public static String tenhinh;
    EditText Tensp,gia,noidung;
    AppCompatButton post;
    FirebaseAuth mAuth;
    private  String id;
    private  String idcustomer;
    private  String id1;
    FirebaseUser mUser;
    DatabaseReference mRef;
    private static final int SELECT_PICTURE = 1;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference Mdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_update);
        anhxa();
        loadData();
        load();
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
    private void anhxa()
    {
        Mdata = FirebaseDatabase.getInstance().getReference().child("sanpham");
        imageView=findViewById(R.id.IMGSP1);
        gia=findViewById(R.id.Giasp11);
        DecimalFormat formatter = new DecimalFormat("###,###,###");


        Tensp=findViewById(R.id.post_liquidation_name1);
        noidung=findViewById(R.id.post_liquidation_desc1);
        post=findViewById(R.id.post_liquidation_btn1);
        khuvuc = findViewById(R.id.khhuvuc1);
        spinnertinhtrang=findViewById(R.id.spinnertinhtrang1);
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
        spinner =findViewById(R.id.spinner1);
        String m[] = {"Nội -Ngoại Thất","Đồ Điện Tử","Xe Cộ","Thời Trang","Mẹ và bé","Giải Trí-Thể Thao","Đồ Văn Phòng","Dịch Vụ"};
        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,m);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_multichoice);
        StorageReference storageRef = storage.getReferenceFromUrl("gs://thanh-l-c.appspot.com");
        spinner.setAdapter(adapter);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tensp.getText().toString().length() != 0 && gia.getText().toString().length() != 0 && noidung.getText().toString().length() != 0) {
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
                                Toast.makeText(MainUpdate.this, "Thành Công", Toast.LENGTH_SHORT).show();
                                //Log.d("AAAA",down+"");
                              //  String tai = idd.getText().toString().trim();
                                Query user = FirebaseDatabase.getInstance().getReference("sanpham").orderByChild("id").equalTo(id);
                                user.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                            tenhinh = snapshot.child(id).child("id").getValue(String.class);
                                            Mdata = FirebaseDatabase.getInstance().getReference().child("sanpham");
                                            Hinhanh hinhanh = new Hinhanh(idcustomer,currentDate,khuvuc.getSelectedItem().toString(),spinnertinhtrang.getSelectedItem().toString(), spinner.getSelectedItem().toString(), tenhinh, Tensp.getText().toString(), gia.getText().toString(), noidung.getText().toString(), String.valueOf(downloadUri),id1 );
                                            Mdata.child(id).setValue(hinhanh, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    if (error != null) {
                                                        Toast.makeText(MainUpdate.this, "k Thành Công", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(MainUpdate.this, "Lưu Thành Công", Toast.LENGTH_SHORT).show();

                                                    }
                                                }

                                            });


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                                // String test = spinner.getSelectedItem().toString().trim();


                                //tạo node trên database


                            } else {
                                // Handle failures
                                // ...
                            }

                        }


                    });
                } else {
                    Toast.makeText(MainUpdate.this, "Vui lòng điền đầy đủ!!!", Toast.LENGTH_SHORT).show();
                }
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
                    String key = snapshot.getKey();
                    String address = snapshot.child("address").getValue(String.class);
                    String fullName = snapshot.child("fullName").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String phoneNo = snapshot.child("phoneNo").getValue(String.class);
                    String profileImg = snapshot.child("profileImg").getValue(String.class);
                    String username = snapshot.child("username").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    id1=phoneNo;
                    idcustomer=key;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainUpdate.this, "Xin lỗi ! Có gì đó lỗi rồi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadData(){
        Intent intent = getIntent();
        Tensp.setText(intent.getStringExtra("ten"));
        gia.setText(intent.getStringExtra(("gia")));
        noidung.setText(intent.getStringExtra("noidung"));
       id = intent.getStringExtra("id");

        String url = intent.getStringExtra("hinh");
        Picasso.get().load(url).into(imageView);
    }
    public void xoasanpham(View view) {


//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainUpdate.this);
//        alertDialogBuilder.setMessage("Bán có muốn xóa sản phẩm này!");
//        alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
                // xóa sp đang nhấn giữ
                Mdata = FirebaseDatabase.getInstance().getReference().child("sanpham").child(id);
                Mdata.removeValue();
                Toast.makeText(MainUpdate.this, "Xóa Thành Công", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        alertDialogBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        alertDialogBuilder.show();

    }
}