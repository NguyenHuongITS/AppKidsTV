package com.example.meowmeow.youtubekids.Model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Rating;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meowmeow.youtubekids.R;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.io.ByteArrayOutputStream;

import angtrim.com.fivestarslibrary.FiveStarsDialog;
import angtrim.com.fivestarslibrary.NegativeReviewListener;
import angtrim.com.fivestarslibrary.ReviewListener;

public class UserActivity extends AppCompatActivity implements View.OnClickListener, NegativeReviewListener, ReviewListener {

    //Khởi tạo các thành phần cần thiết cho ứng dụng
    Button btn_setting, btnBack, btnEdituser, btncancle, btnsettime, btnplus, btnminus, btnsatisfied, btnunsatisfied;
    Button btn_sendfeedbackus, btn_sendfeedbacks;
    TextView txt_username, txtplus, txttimer;
    CircularImageView imageuser;

    //Khởi tạo các thành phần cần thiết cho seekbar
    private SeekBar seekBar;
    public static int DELTA_VALUE = 1;
    private static final String LOGTAG = "Error";
    public static int timecountdown = 0;
    public static CountDownTimer countDownTimer;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //ánh xạ đến view để hiển thị
        AnhXa();
        //sự kiện click của button
        ControlButton();
//        //popup menu trong User
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(UserActivity.this, btn_setting);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.phanhoi:
                                PhanHoiClick();
                                return true;
                            case R.id.gioithieu:
                                GioiThieuClick();
                                return true;
                            case R.id.danhgia:
                                DanhGiaClick();
                                return true;
                            case R.id.hengio:
                                HenGioClick();
                                return true;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        //lấy dữ liệu từ sharepreferences
        GetPreferences();

    }

    private void AnhXa() {
        txt_username = findViewById(R.id.txt_Username);
        imageuser = findViewById(R.id.img_User);
        btnEdituser = findViewById(R.id.btn_EditUser);
        btnBack = findViewById(R.id.btn_BackUser);
        btn_setting = findViewById(R.id.btn_SettingUser);
    }

    private void ControlButton() {
        btnBack.setOnClickListener(this);
        btnEdituser.setOnClickListener(this);
        imageuser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_BackUser:
                Intent intent = new Intent(UserActivity.this, RecommendedMovie.class);
                intent.putExtra("user", txt_username.getText().toString());
                startActivity(intent);
                finish();
                break;

            case R.id.btn_EditUser:
                Intent intent1 = new Intent(UserActivity.this, EditUserActivity.class);
                intent1.putExtra("user", txt_username.getText().toString());
                startActivity(intent1);
                finish();
                break;

            case R.id.img_User:
                Intent intent2 = new Intent(UserActivity.this, RecommendedMovie.class);
                startActivity(intent2);
                break;
        }
    }

    private void DanhGiaClick() {
        FiveStarsDialog fiveStarsDialog = new FiveStarsDialog(this,"huong.cntp97@gmail.com");
        fiveStarsDialog.setRateText("Cho người khác biết cảm nhận của bạn")
                .setTitle("Xếp hạng ứng dụng này")
                .setForceMode(false)
                .setStyle(R.style.DialogTheme) // set theme from styles.xml
                .setUpperBound(2) // Market opened if a rating >= 2 is selected
                .setInternational()
                .setShowOnZeroStars(true) //open market on zero stars after positive button clicked
                .setNegativeReviewListener(this) // OVERRIDE mail intent for negative review
                .setReviewListener(this) // Used to listen for reviews (if you want to track them )
                .showAfter(0);
    }

    //Popup menu hẹn giờ
    private void HenGioClick() {

        //khởi tạo và ánh xạ dialog
        final Dialog dialog2 = new Dialog(this);
        dialog2.setContentView(R.layout.item_shutdown_time);

        //Custom dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog2.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        dialog2.getWindow().setAttributes(layoutParams);


        ///hẹn giờ tắt ứng dụng
        seekBar = dialog2.findViewById(R.id.seekbar_settings);
        txtplus = dialog2.findViewById(R.id.txt_plus);

        txttimer = dialog2.findViewById(R.id.textView7);


        txtplus.setText("" + seekBar.getProgress());

        //thay đổi giá trị trong seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            // Khi giá trị progress thay đổi.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtplus.setText("" + progress);
                Log.i(LOGTAG, "Changing seekbar's progress");
            }

            // Khi người dùng bắt đầu cử chỉ kéo thanh gạt.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(LOGTAG, "Started tracking seekbar");
            }

            // Khi người dùng kết thúc cử chỉ kéo thanh gạt.
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtplus.setText("" + seekBar.getProgress());
                Log.i(LOGTAG, "Stopped tracking seekbar");
            }
        });

        //sự kiện click của dialog hẹn giờ
        btncancle = dialog2.findViewById(R.id.btn_cancle);
        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.cancel();
            }
        });

        //Tăng thời gian trong seekbar
        btnplus = dialog2.findViewById(R.id.btn_plus);
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hàm tăng thời gian seekbar
                decreateProgress();
            }
        });

        //Giảm thời gian trong seekbar
        btnminus = dialog2.findViewById(R.id.btn_minus);
        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hàm giảm thời gian seekbar
                increateProgress();
            }
        });

        btnsettime = dialog2.findViewById(R.id.btn_settime);
        btnsettime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Đếm ngược thời gian tắt ứng dụng
                //lấy thời gian tính bằng phút, sau đó chuyển qua giây
                timecountdown = Integer.parseInt(txtplus.getText().toString()) * 60 * 1000;
                int hienthitime = timecountdown / 60000;

                countDownTimer = new CountDownTimer(timecountdown, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //Đếm ngược theo từng giây
                        if (timecountdown == 0) {
                            Toast.makeText(UserActivity.this, "Ứng dụng sẽ bị đóng!!!", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            txttimer.setText("seconds remaining:" + millisUntilFinished / 1000);
                        }
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public void onFinish() {
                        showAlertDialog();
                        //Toast.makeText(UserActivity.this, "Ứng dụng sẽ bị đóng!!!", Toast.LENGTH_SHORT).show();
                        finishAndRemoveTask();//đóng toàn bộ ứng dụng
                        //finishAffinity();
                    }
                }.start();//bắt đầu đếm ngược
                //Hiển thị thông báo cho người dùng
                Toast.makeText(UserActivity.this, "Ứng dụng sẽ tự tắt trong: " + hienthitime + "phút", Toast.LENGTH_SHORT).show();
                //dialog2.cancel();//đóng dialog
                Intent intent3 = new Intent(UserActivity.this, RecommendedMovie.class);
                startActivity(intent3);
            }
        });

        //Hiển thị dialog
        dialog2.show();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Hết giờ cài đặt sẵn");
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //hàm tăng thời gian seekbar
    private void decreateProgress() {
        int progress = seekBar.getProgress();
        if ((progress + DELTA_VALUE) > seekBar.getMax()) {
            seekBar.setProgress(0);
        } else {
            seekBar.setProgress(progress + DELTA_VALUE);
        }
    }

    //hàm giảm thời gian seekbar
    private void increateProgress() {
        int progress = seekBar.getProgress();
        if ((progress - DELTA_VALUE) < 0) {
            seekBar.setProgress(0);
        } else {
            seekBar.setProgress(progress - DELTA_VALUE);
        }
    }

    //Popup menu giới thiệu
    private void GioiThieuClick() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_introduce_settings);
        //Custom dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
    }

    //Popup menu phản hồi
    public void PhanHoiClick() {

        final Dialog dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.item_feedback_settings);

        //Custom dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog1.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialog1.getWindow().setAttributes(layoutParams);

        //sự kiện click của dialog
        btnsatisfied = dialog1.findViewById(R.id.feedback_satisfied);
        btnsatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.setContentView(R.layout.item_feedback_satisfied);
                btn_sendfeedbacks = dialog1.findViewById(R.id.btn_sendfeedbacks);
                btn_sendfeedbacks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendmailFeedback();
                        dialog1.cancel();
                    }
                });
            }
        });
        //dialog1.setContentView(R.layout.khailong);
        btnunsatisfied = dialog1.findViewById(R.id.feedback_unsatisfied);
        btnunsatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.setContentView(R.layout.item_feedback_unsatisfied);
                btn_sendfeedbackus = dialog1.findViewById(R.id.btn_sendfeedbackus);
                btn_sendfeedbackus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendmailFeedback();
                    }
                });

            }
        });
        dialog1.show();
    }

    private void SendmailFeedback() {
        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "Phản hồi của bạn về ứng dụng KidsTV"+ "&body=" + "" + "&to=" + "huong.cntp97@gmail.com");
        mailIntent.setData(data);
        try {
            startActivity(Intent.createChooser(mailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(UserActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }

    //lấy dữ liệu bằng sharepreferences
    public void GetPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        String UserValue = sharedPreferences.getString("user", "");
        String AvataValue = sharedPreferences.getString("avatar", "");
        txt_username.setText(UserValue);
        if (!AvataValue.equals("")) {
            imageuser.setImageBitmap(decodeBase64(AvataValue));
        }
    }

    ///lưu dữ liệu bằng sharepreferences
    public void SavingPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("avatar_user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Bitmap bitmap = ((BitmapDrawable) imageuser.getDrawable()).getBitmap();
        editor.putString("avataruser", encodeTobase64(bitmap));

        editor.commit();
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

    @Override
    public void onNegativeReview(int i) {

    }

    @Override
    public void onReview(int i) {

    }
}
