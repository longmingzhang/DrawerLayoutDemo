package com.oraro.stardream.ui.base;

import java.io.File;
import java.text.SimpleDateFormat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.actionbarsherlock.app.SherlockActivity;
import com.oraro.stardream.R;
import com.oraro.stardream.util.FileCache;

public class PhotoUtilActivity extends SherlockActivity {
	public static final int PHONT_SOURCE_CAMERA = 1;
	public static final int PHONT_SOURCE_GALLERY = 2;
	public static final int CANCEL_CHOICE = 3;
	public static final int SMALL = 0;
	public static final int COMMON = 1;
	public static final int COMMON_MAX_WIDTH = 600;
	public String SMALL_PIC = "small_pic.jpg";
	public String COMMON_PIC = "common_pic.jpg";
	private Bitmap photo;
	public String filename = COMMON_PIC;
	private int type;
	private String status;
	private File file;

	private void setPicture(Bitmap b) {
		photo = b;
		getPicBitmap(type);
	}

	private void setPicturePath(String path) {
		Bitmap bmp = BitmapFactory.decodeFile(path);
		photo = bmp;
		getPicBitmap(type);
	}

	/**
	 * 获取图片Bitmap
	 * 
	 * @param type
	 * @return Bitmap
	 */
	public Bitmap getPicBitmap(int type) {
		return photo;
	}

	/**
	 * 获取图片缓存路径
	 * 
	 * @param type
	 * @return String 照片存放路径
	 */
	public String getPicpath(int type) {
		// TODO Auto-generated method stub
		if (type == SMALL) {
			if (new File(FileCache.getInstance().getImagePath() + SMALL_PIC)
					.exists())
				return FileCache.getInstance().getImagePath() + SMALL_PIC;
		} else {
			if (new File(FileCache.getInstance().getImagePath() + COMMON_PIC)
					.exists())
				return FileCache.getInstance().getImagePath() + COMMON_PIC;
		}
		return null;
	}

	/**
	 * 按时间命名重新保存选取的照片到SD卡
	 * 
	 * @param 路径FileCache
	 *            .getInstance().getImagePath() + picName
	 * @return String 照片存放名称
	 */
	public String savePic(Bitmap bm) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String picName = sDateFormat.format(new java.util.Date()) + ".PNG";// 照片名字
		FileCache.getInstance().saveBmpDataByName(picName, bm);// 重新保存
		return picName;
	}

	protected Dialog createDialog(int type) {
		if (type == SMALL) {// 头像
			filename = SMALL_PIC;
		} else if (type == COMMON) {// 图片
			filename = COMMON_PIC + (Math.random() * 100000);
		}
		this.type = type;
		return new AlertDialog.Builder(this)
				.setTitle(R.string.select_dialog_photo)
				.setItems(R.array.select_dialog_items,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									takePicture();
								} else {
									openAlbum();
								}
							}
						}).create();
	};

	public void startheadPhotoBitZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		if (type == SMALL) {
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
		} else if (type == COMMON) {
			int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
			if (screenWidth > COMMON_MAX_WIDTH) {
				screenWidth = COMMON_MAX_WIDTH;
			}
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", screenWidth);
			intent.putExtra("outputY", screenWidth);
		}
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				FileCache.getInstance().getImagePath(), filename)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, CANCEL_CHOICE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PHONT_SOURCE_GALLERY) {
			if (resultCode != 0) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = this.getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				startheadPhotoBitZoom(Uri.fromFile(new File(picturePath)));
				file = new File(picturePath);
			}
		} else if (requestCode == PHONT_SOURCE_CAMERA) {
			if (resultCode != 0) {
				File temp = new File(FileCache.getInstance().getImagePath()
						+ filename);
				startheadPhotoBitZoom(Uri.fromFile(temp));
				file = temp;
			}
		} else if (requestCode == CANCEL_CHOICE) {
			if (resultCode != 0) {
				setPicturePath(FileCache.getInstance().getImagePath()
						+ filename);
			}
		}
	}

	/*
	 * 相册选取的方式
	 */
	public void openAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, PHONT_SOURCE_GALLERY);
	}

	/*
	 * 照相的方式
	 */
	public void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				FileCache.getInstance().getImagePath(), filename)));
		startActivityForResult(intent, PHONT_SOURCE_CAMERA);
	}

}
