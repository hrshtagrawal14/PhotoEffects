package com.example.photoeffect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.appworld.like.photoshopeffects.R;
import com.aviary.android.feather.library.utils.DecodeUtils;
import com.aviary.android.feather.library.utils.ImageSizes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Main extends Activity {
	private int imageWidth, imageHeight;
	private Start objStart;
	private ImageButton btnshare, btndontlike;
	private Bitmap mbfilterBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shareimage);
		Toast.makeText(getApplicationContext(), "You Picture is successfully saved to /LikePhotoShopEffect", Toast.LENGTH_LONG).show();
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		imageWidth = (int) ((float) metrics.widthPixels / 1.5);
		imageHeight = (int) ((float) metrics.heightPixels / 1.5);
		// btnsave = (Button)findViewById(R.id.btnSave);
		btnshare = (ImageButton) findViewById(R.id.btnshare);
		btndontlike = (ImageButton) findViewById(R.id.btnDontLike);

	
		ImageSizes sizes = new ImageSizes();

		objStart = new Start();
		mbfilterBitmap = DecodeUtils.decode(Main.this,
				Uri.parse(objStart.uri_static), imageWidth, imageHeight, sizes);
		

		ImageView imageView = (ImageView)findViewById(R.id.imgview);
		imageView.setImageBitmap(mbfilterBitmap);

		
		/*btnsave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String filepath = "Photo Effect" + System.currentTimeMillis();
				saveimage(filepath);
			}
		});*/
		btnshare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mbfilterBitmap != null) {
					String path = Images.Media.insertImage(
							getContentResolver(), mbfilterBitmap,
							"YourPicture", null);
					Uri screenuri = Uri.parse(path);
					Intent emailintent = new Intent(
							android.content.Intent.ACTION_SEND);
					Toast.makeText(getApplicationContext(),
							"Choose The Method", Toast.LENGTH_LONG).show();
					emailintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					emailintent.putExtra(Intent.EXTRA_STREAM, screenuri);
					emailintent.setType("image/png");
					startActivity(emailintent);
				}
			}
		});
		btndontlike.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent dont_like = new Intent(Main.this, Start.class);
				finish();
				startActivity(dont_like);
			}
		});

	}

	public void saveimage(String savefile) {
		String filename = savefile;
		String s = new String(filename + ".jpg");
		try {

			String directory = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/PhotoEffect/";

			File file = new File(directory);
			file.mkdirs();

			FileOutputStream out1 = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/PhotoEffect/" + s);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			mbfilterBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			byte cy[] = out.toByteArray();
			out1.write(cy);
			// ---------------------

			out1.flush();
			out1.close();
			Toast.makeText(getBaseContext(),
					"File saved successfully to /LikePhotoshop/",
					Toast.LENGTH_SHORT).show();

		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

}
