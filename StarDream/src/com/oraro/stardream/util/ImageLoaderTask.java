package com.oraro.stardream.util;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//已不使用此类：AsyncTask加载过多会造成 java.util.concurrent.RejectedExecutionException
public class ImageLoaderTask {

	public static final String HD = "HD";
	private static final HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	public static String uri2HD(String imageUrl) {
		return HD + imageUrl;
	}

	public static Bitmap loaDrawable(final String imageUrl) {
		Bitmap bmpFromSD = null;
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			Bitmap drawable = softReference.get();
			if (null != drawable) {
				return drawable;
			}
		} else if (null != (bmpFromSD = FileCache.getInstance()
				.getBmp(imageUrl))) {
			return bmpFromSD;
		} else {
			return loadImageFromUrl(imageUrl);
		}
		return null;

	}

	public static Bitmap loadImageFromUrl(String url) {
		URL tempUrl;
		InputStream inputStream = null;
		Bitmap bmp = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = true;
			if (url.startsWith(HD)) {
				options.inSampleSize = 1;
				tempUrl = new URL(url.substring(2));
			} else {
				// options.inSampleSize = 5;
				options.inSampleSize = 1;
				tempUrl = new URL(url);
			}
			options.inJustDecodeBounds = false;
			inputStream = (InputStream) tempUrl.getContent();
			bmp = BitmapFactory.decodeStream(inputStream, null, options);
			FileCache.getInstance().savaBmpData(url, bmp);
		} catch (OutOfMemoryError err) {
			System.out.println("溢出...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}
}