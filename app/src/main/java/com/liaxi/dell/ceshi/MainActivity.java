package com.liaxi.dell.ceshi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {

    private ImageView show_iv;

    private String mFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        show_iv = (ImageView) findViewById(R.id.imageView);
    }

    public void onClick(View view) {
        Intent intent = new Intent();
         //1. 直接调用系统相机 没有返回值
		 intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
		 startActivity(intent);
        // 2 调用系统相机 有返回值 但是返回值是 缩略图
//		 intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//		 startActivityForResult(intent, 100);
        // 3 .返回原图
//        mFilePath =
//                Environment.getExternalStorageDirectory().getAbsolutePath() +
//                        "/picture.png";
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri uri = Uri.fromFile(new File(mFilePath));
//        //  指定路径
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        startActivityForResult(intent, 300);

        // 4. 打开系统相册
//		intent.setAction(Intent.ACTION_PICK);
//		intent.setType("image/*");
//		startActivityForResult(intent, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //   返回缩略图
        if (requestCode == 100) {
            if (data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    show_iv.setImageBitmap(bitmap);
                }
            }
        }
        // 原图
        if (requestCode == 300) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(mFilePath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    show_iv.setImageBitmap(rotateBitmap(bitmap));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // 相册
        if (requestCode == 500) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                show_iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // 三星机型拍照的时候照片会旋转90度 所以需要转回来
    public Bitmap rotateBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap map = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return map;
    }

}
