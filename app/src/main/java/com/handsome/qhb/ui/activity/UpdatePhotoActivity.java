package com.handsome.qhb.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.handsome.qhb.bean.User;
import com.handsome.qhb.config.Config;
import com.handsome.qhb.utils.LogUtils;
import com.handsome.qhb.utils.RequestQueueController;
import com.handsome.qhb.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import tab.com.handsome.handsome.R;

/**
 * Created by zhang on 2016/3/19.
 */
public class UpdatePhotoActivity extends BaseActivity {
    private ImageButton ib_back;
    private ImageButton ib_photo_menu;
    // 拍照
    private static final int PHOTO_REQUEST_CAMERA = 1;
    // 从相册中选择
    private static final int PHOTO_REQUEST_GALLERY = 2;
    // 结果
    private static final int PHOTO_REQUEST_CUT = 3;
    //ImageView
    private ImageView iv_user_photo;
    private Bitmap bitmap;
    private String photo;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_photo);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_photo_menu = (ImageButton)findViewById(R.id.ib_photo_menu);
        iv_user_photo = (ImageView)findViewById(R.id.iv_user_photo);
        iv_user_photo.setImageResource(R.mipmap.test_icon);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ib_photo_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdatePhotoActivity.this);
                alertDialog.setItems(new String[]{"照相", "从手机相册中选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            camera();
                        } else if (i == 1) {
                            gallery();
                        }
                    }
                });
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PHOTO_REQUEST_CAMERA){
            if(data==null){
                return ;
            }
            else{
                Bundle extras = data.getExtras();
                if(extras!=null){
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = saveBitmap(bm);
                    crop(uri);
                }
            }
        }else if(requestCode==PHOTO_REQUEST_GALLERY){
            if(data==null){
                return;
            }
            Uri uri ;
            uri = data.getData();
//            Uri fileUri = convertUri(uri);
            crop(uri);
        }else if(requestCode==PHOTO_REQUEST_CUT){
            if(data==null){
                return;
            }
            Bundle extras = data.getExtras();
            Bitmap bm = extras.getParcelable("data");
            iv_user_photo.setImageBitmap(bm);
            sendImage(bm);
        }
    }

    private Uri saveBitmap(Bitmap bm){
        File tmpDir = new File(Environment.getExternalStorageDirectory()+"/photo");
        if(!tmpDir.exists()){
            tmpDir.mkdir();
            LogUtils.e("mkdir","=======>");
        }
        File img = new File(tmpDir,System.currentTimeMillis()+".png");
        try{
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG,85,fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendImage(Bitmap bm){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,60,stream);
        byte[] bytes = stream.toByteArray();
        photo = new String (Base64.encodeToString(bytes,Base64.DEFAULT));
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.BASE_URL+"User/updatePhoto",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            LogUtils.e("response",response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status == "0"){
                               Toast toast = Toast.makeText(UpdatePhotoActivity.this, jsonObject.getString("info"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                                return;
                            }
                            User user =  gson.fromJson(jsonObject.getString("data"),User.class);

                            Toast toast = Toast.makeText(UpdatePhotoActivity.this,jsonObject.getString("info"),Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
//                            SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
//                            editor.putString("user",jsonData.getString("user"));
//                            editor.putLong("date", new Date().getTime());
//                            editor.commit();

                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Toast.makeText(UpdatePhotoActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid",String.valueOf(UserInfo.getInstance().getUid()));
                map.put("photo",photo);
                return map;
            }
        };
        RequestQueueController.getInstance().add(stringRequest);
    }

    private Uri convertUri(Uri uri){
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return saveBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 从相机获取
     */
    public void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//         判断存储卡是否可以用，可用进行存储
//        if (hasSdcard()) {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                    Uri.fromFile(new File(Environment
//                            .getExternalStorageDirectory(),System.currentTimeMillis()+".png")));
//        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    /**
     * 剪切图片
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
//        // 图片格式
//        intent.putExtra("outputFormat", "PNG");
//        // 取消人脸识别
//        intent.putExtra("noFaceDetection", true);
        // true:不返回uri，false：返回uri
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    /**
     *是否插入sd卡
     * @return
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
