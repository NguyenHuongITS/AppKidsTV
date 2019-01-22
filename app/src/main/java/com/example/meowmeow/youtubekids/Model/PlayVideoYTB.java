package com.example.meowmeow.youtubekids.Model;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.meowmeow.youtubekids.Adapter.PlayVideoAdapter;
import com.example.meowmeow.youtubekids.CameraDistance.ActivityFaceDetectGray;
import com.example.meowmeow.youtubekids.CameraDistance.CameraErrorCallback;
import com.example.meowmeow.youtubekids.CameraDistance.FaceOverlayView;
import com.example.meowmeow.youtubekids.CameraDistance.FaceResult;
import com.example.meowmeow.youtubekids.CameraDistance.ImagePreviewAdapter;
import com.example.meowmeow.youtubekids.CameraDistance.ImageUtils;
import com.example.meowmeow.youtubekids.CameraDistance.Utils;
import com.example.meowmeow.youtubekids.Interface.PlayVideo;
import com.example.meowmeow.youtubekids.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class PlayVideoYTB extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener, SurfaceHolder.Callback, Camera.PreviewCallback {

    //camera viewer
//khai báo biến cho camerasurface
    // Number of Cameras in device.
    float eyeDistance = 0;
    float results = 0;
    boolean checkFace = true;
    private int numberOfCameras;
    public static final String TAG = ActivityFaceDetectGray.class.getSimpleName();
    private Camera mCamera;
    private int cameraId = 1;
    // Let's keep track of the display rotation and orientation also:
    private int mDisplayRotation;
    private int mDisplayOrientation;
    private int previewWidth;
    private int previewHeight;
    // The surface view for the camera data
    private SurfaceView mView;
    // Draw rectangles and other fancy stuff:
    private FaceOverlayView mFaceView;
    // Log all errors:
    private final CameraErrorCallback mErrorCallback = new CameraErrorCallback();
    private static final int MAX_FACE = 10;
    private boolean isThreadWorking = false;
    private Handler handler;
    private PlayVideoYTB.FaceDetectThread detectThread = null;
    private int prevSettingWidth;
    private int prevSettingHeight;
    private android.media.FaceDetector fdet;
    private byte[] grayBuff;
    private int bufflen;
    private int[] rgbs;
    private FaceResult faces[];
    private FaceResult faces_previous[];
    private int Id = 0;
    private String BUNDLE_CAMERA_ID = "camera";
    //RecylerView face image
    private HashMap<Integer, Integer> facesCount = new HashMap<>();
    private RecyclerView recyclerView;
    private ImagePreviewAdapter imagePreviewAdapter;
    private ArrayList<Bitmap> facesBitmap;
    Dialog dialog;


    //youtube
    String API_KEYVIDEO = "AIzaSyA2e3uG6u3_fWeh_KNIS4UN5bZPD2FiDxM";
    int REQUEST_VIDEO = 123;
    YouTubePlayerView youTubePlayerView;
    //list video
    public RecyclerView recyclerViewvideoplay;
    ArrayList<PlayVideo> playVideoArrayList = new ArrayList<>();
    PlayVideoAdapter playVideoAdapter;
    ImageButton btn_back;
    //Khai báo keyplaylist
    private String API_KEYPLAYLIST ="AIzaSyAI6YiDW8IaP6bVYSLTPyih2uNX0PWNyn0";
    // khai báo keyid
    private String ID_PLAYLIST ="PLyd2TpA1RlFJVYM5k8KnK1BuemoeuqWaf";
    // link lấy danh sách video từ playlist id
    public String urlYTB = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId="+ID_PLAYLIST+"&key="+API_KEYPLAYLIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_ytb);

        AnhXa();
        //gán dữ liệu từ youtube lên recycleview
        GetYTBJson(urlYTB);

     //youtube
        youTubePlayerView = findViewById(R.id.myYTBView);
        youTubePlayerView.initialize(API_KEYVIDEO, this);
        
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

     //camera
        //Tạo hàm để set khoảng cách
        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_unscreened_background);
        //Custom dialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(layoutParams);

        //Create view
        mView = (SurfaceView) findViewById(R.id.surfaceview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Now create the OverlayView:
        mFaceView = new FaceOverlayView(this);
        addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // Create and Start the OrientationListener:

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_camera);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        handler = new Handler();
        faces = new FaceResult[MAX_FACE];
        faces_previous = new FaceResult[MAX_FACE];
        for (int i = 0; i < MAX_FACE; i++) {
            faces[i] = new FaceResult();
            faces_previous[i] = new FaceResult();
        }

//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Face Detect Gray");

        if (savedInstanceState != null)
            cameraId = savedInstanceState.getInt(BUNDLE_CAMERA_ID, 0);
     //end camera
    }

    private void AnhXa() {
        recyclerViewvideoplay = findViewById(R.id.recycleview_playYTB);
        btn_back = findViewById(R.id.img_backplay);
    }

//youtube
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        String results = getIntent().getExtras().getString("videoId");
//        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        youTubePlayer.cueVideo(results);
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(PlayVideoYTB.this, REQUEST_VIDEO );
        }
        else {
            Toast.makeText(this, "Error!!!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_VIDEO){
            youTubePlayerView.initialize(API_KEYVIDEO, PlayVideoYTB.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void GetYTBJson(final String url) {
        Log.d("BBB",url);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonItems = response.getJSONArray("items");
                            String title ="";
                            String urlvideo ="";
                            String idvideo = "";
                            for(int i = 0; i < jsonItems.length();i++)
                            {
                                JSONObject jsonObject = jsonItems.getJSONObject(i);
                                JSONObject jsonSnippet = jsonObject.getJSONObject("snippet");
                                title = jsonSnippet.getString("title");
                                JSONObject jsonThumbnails = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium = jsonThumbnails.getJSONObject("medium");
                                urlvideo = jsonMedium.getString("url");
                                JSONObject jsonResource = jsonSnippet.getJSONObject("resourceId");
                                idvideo = jsonResource.getString("videoId");

                                playVideoArrayList.add(new PlayVideo(title,urlvideo,idvideo));
                            }
                            playVideoAdapter = new PlayVideoAdapter(getApplicationContext(),R.layout.item_custom_video,playVideoArrayList);

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                            recyclerViewvideoplay.setLayoutManager(mLayoutManager);
                            recyclerViewvideoplay.setItemAnimator(new DefaultItemAnimator());
                            recyclerViewvideoplay.setAdapter(playVideoAdapter);
                            playVideoAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(MusicMovie.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlayVideoYTB.this, "Error!!!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
//end youtube

//Camera viewer
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        SurfaceHolder holder = mView.getHolder();
        holder.addCallback(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_camera, menu);
        return true;
    }
    //Restarts the camera.
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        startPreview();
    }
    //Stops the camera.
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }
    //Destroy the camera.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CAMERA_ID, cameraId);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //Find the total number of cameras available
        resetData();

        numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                if (cameraId == 0) cameraId = i;
            }
        }

        mCamera = Camera.open(cameraId);

        Camera.getCameraInfo(cameraId, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mFaceView.setFront(true);
        }

        try {
            mCamera.setPreviewDisplay(mView.getHolder());
        } catch (Exception e) {
            Log.e(TAG, "Could not preview the image.", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // We have no surface, return immediately:
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        // Try to stop the current preview:
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore...
        }

        configureCamera(width, height);
        setDisplayOrientation();
        setErrorCallback();

        // Create media.FaceDetector
        float aspect = (float) previewHeight / (float) previewWidth;
        fdet = new android.media.FaceDetector(prevSettingWidth, (int) (prevSettingWidth * aspect), MAX_FACE);

        bufflen = previewWidth * previewHeight;
        grayBuff = new byte[bufflen];
        rgbs = new int[bufflen];

        // Everything is configured! Finally start the camera preview again:
        startPreview();
    }

    private void setErrorCallback() {
        mCamera.setErrorCallback(mErrorCallback);
    }

    private void setDisplayOrientation() {
        // Now set the display orientation:
        mDisplayRotation = Utils.getDisplayRotation(PlayVideoYTB.this);
        mDisplayOrientation = Utils.getDisplayOrientation(mDisplayRotation, cameraId);

        mCamera.setDisplayOrientation(mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(mDisplayOrientation);
        }
    }

    private void configureCamera(int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        // Set the PreviewSize and AutoFocus:
        setOptimalPreviewSize(parameters, width, height);
        setAutoFocus(parameters);
        // And set the parameters:
        mCamera.setParameters(parameters);
    }

    private void setOptimalPreviewSize(Camera.Parameters cameraParameters, int width, int height) {
        List<Camera.Size> previewSizes = cameraParameters.getSupportedPreviewSizes();
        float targetRatio = (float) width / height;
        Camera.Size previewSize = Utils.getOptimalPreviewSize(this, previewSizes, targetRatio);
        previewWidth = previewSize.width;
        previewHeight = previewSize.height;

        Log.e(TAG, "previewWidth" + previewWidth);
        Log.e(TAG, "previewHeight" + previewHeight);

        /**
         * Calculate size to scale full frame bitmap to smaller bitmap
         * Detect face in scaled bitmap have high performance than full bitmap.
         * The smaller image size -> detect faster, but distance to detect face shorter,
         * so calculate the size follow your purpose
         */
        if (previewWidth / 4 > 360) {
            prevSettingWidth = 360;
            prevSettingHeight = 270;
        } else if (previewWidth / 4 > 320) {
            prevSettingWidth = 320;
            prevSettingHeight = 240;
        } else if (previewWidth / 4 > 240) {
            prevSettingWidth = 240;
            prevSettingHeight = 160;
        } else {
            prevSettingWidth = 160;
            prevSettingHeight = 120;
        }

        cameraParameters.setPreviewSize(previewSize.width, previewSize.height);

        mFaceView.setPreviewWidth(previewWidth);
        mFaceView.setPreviewHeight(previewHeight);
    }

    private void setAutoFocus(Camera.Parameters cameraParameters) {
        List<String> focusModes = cameraParameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    private void startPreview() {
        if (mCamera != null) {
            isThreadWorking = false;
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            counter = 0;
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.setPreviewCallbackWithBuffer(null);
        mCamera.setErrorCallback(null);
        mCamera.release();
        mCamera = null;
    }


    // fps detect face (not FPS of camera)
    long start, end;
    int counter = 0;
    double fps;

    @Override
    public void onPreviewFrame(byte[] _data, Camera _camera) {
        if (!isThreadWorking) {
            if (counter == 0)
                start = System.currentTimeMillis();

            isThreadWorking = true;
            waitForFdetThreadComplete();
            detectThread = new PlayVideoYTB.FaceDetectThread(handler, this);
            detectThread.setData(_data);
            detectThread.start();

        }
    }

    private void waitForFdetThreadComplete() {
        if (detectThread == null) {
            return;
        }

        if (detectThread.isAlive()) {
            try {
                detectThread.join();
                detectThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * Do face detect in thread
     */
    public class FaceDetectThread extends Thread {
        private Handler handler;
        private byte[] data = null;
        private Context ctx;
        private Bitmap faceCroped;
        public float eyesDis;
        public float confidence;
        public float pose;
        int idFace;

        public FaceDetectThread(Handler handler, Context ctx) {
            this.ctx = ctx;
            this.handler = handler;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public void run() {
           Log.i("run", "running");

            float aspect = (float) previewHeight / (float) previewWidth;
            int w = prevSettingWidth;
            int h = (int) (prevSettingWidth * aspect);

            ByteBuffer bbuffer = ByteBuffer.wrap(data);
            bbuffer.get(grayBuff, 0, bufflen);

            gray8toRGB32(grayBuff, previewWidth, previewHeight, rgbs);
            Bitmap bitmap = Bitmap.createBitmap(rgbs, previewWidth, previewHeight, Bitmap.Config.RGB_565);

            Bitmap bmp = Bitmap.createScaledBitmap(bitmap, w, h, false);

            float xScale = (float) previewWidth / (float) prevSettingWidth;
            float yScale = (float) previewHeight / (float) h;

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotate = mDisplayOrientation;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && mDisplayRotation % 180 == 0) {
                if (rotate + 180 > 360) {
                    rotate = rotate - 180;
                } else
                    rotate = rotate + 180;
            }

            switch (rotate) {
                case 90:
                    bmp = ImageUtils.rotate(bmp, 90);
                    xScale = (float) previewHeight / bmp.getWidth();
                    yScale = (float) previewWidth / bmp.getHeight();
                    break;
                case 180:
                    bmp = ImageUtils.rotate(bmp, 180);
                    break;
                case 270:
                    bmp = ImageUtils.rotate(bmp, 270);
                    xScale = (float) previewHeight / (float) h;
                    yScale = (float) previewWidth / (float) prevSettingWidth;
                    break;
            }

            fdet = new android.media.FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACE);

            android.media.FaceDetector.Face[] fullResults = new android.media.FaceDetector.Face[MAX_FACE];
            fdet.findFaces(bmp, fullResults);


            for (int i = 0; i < MAX_FACE; i++) {
                if (fullResults[i] == null) {
                    if(i == 0)
                    {
                        checkFace = false;
                    }
                    faces[i].clear();
                } else {
                    checkFace = true;
                    PointF mid = new PointF();
                    fullResults[i].getMidPoint(mid);
                    mid.x *= xScale;
                    mid.y *= yScale;

                    eyesDis = fullResults[i].eyesDistance() * xScale;
                    results = eyesDis;
                    Log.d("ABC", String.valueOf(eyesDis));
                    confidence = fullResults[i].confidence();
                    pose = fullResults[i].pose(android.media.FaceDetector.Face.EULER_Y);
                    idFace = Id;

                    Rect rect = new Rect(
                            (int) (mid.x - eyesDis * 1.20f),
                            (int) (mid.y - eyesDis * 0.55f),
                            (int) (mid.x + eyesDis * 1.20f),
                            (int) (mid.y + eyesDis * 1.85f));


                    /**
                     * Only detect face size > 100x100
                     */
                    if(rect.height() * rect.width() > 100 * 100) {
                        // Check this face and previous face have same ID?
                        for (int j = 0; j < MAX_FACE; j++) {
                            float eyesDisPre = faces_previous[j].eyesDistance();
                            PointF midPre = new PointF();
                            faces_previous[j].getMidPoint(midPre);

                            RectF rectCheck = new RectF(
                                    (midPre.x - eyesDisPre * 1.5f),
                                    (midPre.y - eyesDisPre * 1.15f),
                                    (midPre.x + eyesDisPre * 1.5f),
                                    (midPre.y + eyesDisPre * 1.85f));

                            if (rectCheck.contains(mid.x, mid.y) && (System.currentTimeMillis() - faces_previous[j].getTime()) < 1000) {
                                idFace = faces_previous[j].getId();
                                break;
                            }
                        }

                        if (idFace == Id) Id++;
                        faces[i].setFace(idFace, mid, eyesDis, confidence, pose, System.currentTimeMillis());
                        faces_previous[i].set(faces[i].getId(), faces[i].getMidEye(), faces[i].eyesDistance(), faces[i].getConfidence(), faces[i].getPose(), faces[i].getTime());
                        //
                        // if focus in a face 5 frame -> take picture face display in RecyclerView
                        // because of some first frame have low quality
                        //
                        if (facesCount.get(idFace) == null) {
                            facesCount.put(idFace, 0);
                        } else {
                            int count = facesCount.get(idFace) + 1;
                            if (count <= 5)
                                facesCount.put(idFace, count);

                            //
                            // Crop Face to display in RecylerView
                            //
                            if (count == 5) {
                                faceCroped = ImageUtils.cropFace(faces[i], bitmap, rotate);
                                if (faceCroped != null) {
                                    handler.post(new Runnable() {
                                        public void run() {
                                            imagePreviewAdapter.add(faceCroped);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }

            handler.post(new Runnable() {
                public void run() {
                    //send face to FaceView to draw rect
                    mFaceView.setFaces(faces);
                    //Calculate FPS (Detect Frame per Second)
                    end = System.currentTimeMillis();
                    counter++;
                    double time = (double) (end - start) / 1000;
                    if (time != 0)
                        fps = counter / time;

                    mFaceView.setFPS(fps);

                    if (counter == (Integer.MAX_VALUE - 1000))
                        counter = 0;
                    isThreadWorking = false;
                    SetBackgroundUnscreen();
                }
            });
        }

        private void gray8toRGB32(byte[] gray8, int width, int height, int[] rgb_32s) {
            final int endPtr = width * height;
            int ptr = 0;
            while (true) {
                if (ptr == endPtr)
                    break;

                final int Y = gray8[ptr] & 0xff;
                rgb_32s[ptr] = 0xff000000 + (Y << 16) + (Y << 8) + Y;
                ptr++;
            }
        }
    }
    private void SetBackgroundUnscreen() {
//        detectThread.eyesDis <= 160 && detectThread.eyesDis >= 90
        //checkFace && eyeDistance <=170
        //eyeDistance <=210 && eyeDistance >=90
        eyeDistance = detectThread.eyesDis;
        if(results < 200){
            dialog.dismiss();
        }else {
            dialog.show();
        }
    }
    /**
     * Release Memory
     */
    private void resetData() {
        if (imagePreviewAdapter == null) {
            facesBitmap = new ArrayList<>();
            imagePreviewAdapter = new ImagePreviewAdapter(PlayVideoYTB.this, facesBitmap, new ImagePreviewAdapter.ViewHolder.OnItemClickListener() {
                @Override
                public void onClick(View v, int position) {
                    imagePreviewAdapter.setCheck(position);
                    imagePreviewAdapter.notifyDataSetChanged();
                }
            });
            recyclerView.setAdapter(imagePreviewAdapter);
        } else {
            imagePreviewAdapter.clearAll();
        }
    }
}
