package com.example.meowmeow.youtubekids.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.meowmeow.youtubekids.Adapter.ExploreVideoAdapter;
import com.example.meowmeow.youtubekids.Adapter.UserAvatarAdapter;
import com.example.meowmeow.youtubekids.Interface.UserAvatar;
import com.example.meowmeow.youtubekids.R;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.spark.submitbutton.SubmitButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnbackedit,btnsubmit;
    EditText edt_username;
    public static CircularImageView imgavatar;

    private List<UserAvatar> userAvatarList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserAvatarAdapter userAvatarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        //ánh xạ đến view để hiển thị
        AnhXa();
        // sự kiện click của button
        ControlButton();
        //lấy dữ liệu từ sharepreferences
        GetPreferences();
        //Gán dữ liệu lên textview
        DataIntent();
        //dữ liệu hình ảnh
        PreDataAvatarUser();
    }

    public void DataIntent(){
        //lấy dữ liệu từ edit username
        Intent intent = getIntent();
        String result = intent.getStringExtra("user");
        edt_username.setText(result);
    }

    private void AnhXa() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_avataruser);
        btnbackedit = findViewById(R.id.btn_BackEditUser);
        imgavatar = findViewById(R.id.img_avataruser);
        edt_username = findViewById(R.id.edt_username);
        btnsubmit = findViewById(R.id.btn_submitedit);
    }

    private void ControlButton() {
        btnbackedit.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Chỉnh sửa dữ liệu người dùng
            case R.id.btn_submitedit:
                //lưu biến vào trong bộ nhớ
                SavingPreferences();
                Intent intent = new Intent(EditUserActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
                break;
            //quay lại trang user
            case R.id.btn_BackEditUser:
                Intent intent1 = new Intent(EditUserActivity.this, UserActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }

    //data image user
    private void PreDataAvatarUser() {

        UserAvatar userAvatar = new UserAvatar(R.drawable.avatar);
        userAvatarList.add(userAvatar);

        userAvatar = new UserAvatar(R.drawable.avatar1);
        userAvatarList.add(userAvatar);

        userAvatar = new UserAvatar(R.drawable.avatar2);
        userAvatarList.add(userAvatar);

        userAvatar = new UserAvatar(R.drawable.avatar7);
        userAvatarList.add(userAvatar);

        userAvatar = new UserAvatar(R.drawable.avatar2);
        userAvatarList.add(userAvatar);

        userAvatar = new UserAvatar(R.drawable.avatar1);
        userAvatarList.add(userAvatar);

        userAvatar = new UserAvatar(R.drawable.avatar7);
        userAvatarList.add(userAvatar);

        userAvatar = new UserAvatar(R.drawable.avatar2);
        userAvatarList.add(userAvatar);

        userAvatar = new UserAvatar(R.drawable.avatar7);
        userAvatarList.add(userAvatar);

        userAvatar = new UserAvatar(R.drawable.avatar1);
        userAvatarList.add(userAvatar);

        userAvatarAdapter = new UserAvatarAdapter(getApplicationContext(),R.layout.item_avatar_user,userAvatarList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userAvatarAdapter);
        userAvatarAdapter.notifyDataSetChanged();
    }

    ///lưu dữ liệu sharepreferences
    public void SavingPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("user_name",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = edt_username.getText().toString();
        //String avt = imgavatar.getResources().toString();
        Bitmap bitmap =((BitmapDrawable) imgavatar.getDrawable()).getBitmap();
        editor.putString("avatar",encodeTobase64(bitmap));

        editor.putString("user",username);

        editor.commit();
    }

    //lấy dữ liệu sharepreferences
    public void GetPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("avatar_user",MODE_PRIVATE);
        String AvataValue = sharedPreferences.getString("avataruser","");
        if(!AvataValue.equals("")){
            imgavatar.setImageBitmap(decodeBase64(AvataValue));
        }
    }

    //hàm chuyển hình ảnh thành bitmap
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    //hàm lấy hình ảnh thành bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }


}
