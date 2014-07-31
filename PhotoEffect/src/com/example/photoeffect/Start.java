package com.example.photoeffect;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.appworld.like.photoshopeffects.R;
import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.aviary.android.feather.library.utils.ImageSizes;

public class Start extends Activity {
	private  final int EXTERNAL_STORAGE_UNAVAILABLE = 1;

	private ImageButton m_gallery, m_Camera;
//	private AdView ad1;

	private final int REQUEST_CODE_GALLERY = 100;
	private final int REQUEST_CODE_CAMERA = 101;
	private File mGalleryFolder;
	private static final String FOLDER_NAME = "Like PhotoEffects";
	public static final String LOG_TAG = "Photo Effect Launcher";
	private  final int ACTION_REQUEST_FEATHER = 102;
	private String mOutputFilePath;
	private ImageView mImage;
	private Uri selectedImage;
	private int imageWidth, imageHeight;
	public static String uri_static;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		imageWidth = (int) ( (float) metrics.widthPixels / 1.5 );
		imageHeight = (int) ( (float) metrics.heightPixels / 1.5 );

		m_gallery = (ImageButton) findViewById(R.id.mgallery);
		m_Camera = (ImageButton) findViewById(R.id.mCamera);
		mGalleryFolder = createFolders();
		/*ad1 = (AdView) findViewById(R.id.admain3);
		ad1.loadAd(new AdRequest());*/
		m_gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callGalleryForInputImage(REQUEST_CODE_GALLERY);
			}
		});
		m_Camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cameraImageCapture();
			}
		});

		/*
		 * st.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent start2; Log.e("Manufacturer", Build.MANUFACTURER); if
		 * (Build.MANUFACTURER.equalsIgnoreCase("samsung")) { start2 = new
		 * Intent(start.this, FiltersActivity.class); } else { start2 = new
		 * Intent(start.this, OtherSamsung.class); } startActivity(start2); }
		 * }); }
		 */
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
	 * Auto-generated method stub if (keyCode == KeyEvent.KEYCODE_BACK) { if
	 * (!bool && exitad != null) { exitad.loadAd(); }
	 */
	/*
	 * AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	 * start.this);
	 * 
	 * // set title alertDialogBuilder.setTitle("Thanks For Using This App");
	 * 
	 * // set dialog message alertDialogBuilder
	 * .setMessage("Would You like to try more") .setCancelable(false)
	 * .setPositiveButton("OK", new DialogInterface.OnClickListener() { public
	 * void onClick(DialogInterface dialog, int id) { // if this button is
	 * clicked, close // current activity if (!bool && exitad != null) {
	 * exitad.loadAd();
	 * 
	 * } } }) .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
	 * { public void onClick(DialogInterface dialog, int id) { // if this button
	 * is clicked, just close // the dialog box and do nothing
	 * start.this.finish(); } });
	 * 
	 * // create alert dialog AlertDialog alertDialog =
	 * alertDialogBuilder.create();
	 * 
	 * // show it alertDialog.show(); }
	 */

	private File createFolders() {
		
		File baseDir;

		if ( android.os.Build.VERSION.SDK_INT < 8 ) {
			baseDir = Environment.getExternalStorageDirectory();
		} else {
			baseDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
		}

		if ( baseDir == null ) return Environment.getExternalStorageDirectory();

		//Log.d( LOG_TAG, "Pictures folder: " + baseDir.getAbsolutePath() );
		File aviaryFolder = new File( baseDir, FOLDER_NAME );

		if ( aviaryFolder.exists() ) return aviaryFolder;
		if ( aviaryFolder.mkdirs() ) return aviaryFolder;

		return Environment.getExternalStorageDirectory();
	}

	private void callGalleryForInputImage(int nRequestCode) {
		try {
			Intent galleryIntent;
			galleryIntent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			/*
			 * galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
			 * galleryIntent.setType("image/*");
			 * galleryIntent.setClassName("com.cooliris.media",
			 * "com.cooliris.media.Gallery");
			 */
			startActivityForResult(galleryIntent, nRequestCode);
		} catch (ActivityNotFoundException e) {
			Intent galleryIntent;
			galleryIntent = new Intent();
			galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
			galleryIntent.setType("image/*");
			startActivityForResult(galleryIntent, nRequestCode);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try{
		if (resultCode == RESULT_OK) {
			if (data == null)
				return;
		}
		if(requestCode== ACTION_REQUEST_FEATHER){
			boolean changed = true;
			
			if( null != data ) {
				Bundle extra = data.getExtras();
				if( null != extra ) {
					// image was changed by the user?
					changed = extra.getBoolean( Constants.EXTRA_OUT_BITMAP_CHANGED );
				}
			}
			
			if( !changed ) {
				Log.w( LOG_TAG, "User did not modify the image, but just clicked on 'Done' button" );
			}
			
			// send a notification to the media scanner
			updateMedia( mOutputFilePath );
			try{
			// update the preview with the result
			
			selectedImage = null;
		
			Uri uri = data.getData();
			uri_static = uri.toString();
			
			Intent shareimage = new Intent(Start.this, Main.class);
	
			
			startActivity(shareimage);
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.e("loadsync", e.toString());
			}
			finally{
				
				startActivity(getIntent());
			}
			mOutputFilePath = null;
			
			
			
			
		}
		if (requestCode == REQUEST_CODE_GALLERY) {
			/*
			 * Uri imageFileUri = data.getData(); String strBackgroundImagePath
			 * = AnimationUtils .getRealPathFromURI(this, imageFileUri);
			 * 
			 * // Check Valid Image File if
			 * (!AnimationUtils.isValidImagePath(strBackgroundImagePath)) {
			 * Toast.makeText(this, "Invalid image path or web image",
			 * Toast.LENGTH_LONG).show(); return; }
			 */
			if (data != null) {
			 selectedImage = data.getData();
				Log.e("uri", selectedImage.toString());
				startFeather(selectedImage);
			}
		}

		if (requestCode == REQUEST_CODE_CAMERA) {

			// Describe the columns you'd like to have returned. Selecting from
			// the Thumbnails location gives you both the Thumbnail Image ID, as
			// well as the original image ID
			String[] projection = {
					MediaStore.Images.Thumbnails._ID, // The columns we want
					MediaStore.Images.Thumbnails.IMAGE_ID,
					MediaStore.Images.Thumbnails.KIND,
					MediaStore.Images.Thumbnails.DATA };
			String selection = MediaStore.Images.Thumbnails.KIND + "=" + // Select
																			// only
																			// mini's
					MediaStore.Images.Thumbnails.MINI_KIND;

			String sort = MediaStore.Images.Thumbnails._ID + " DESC";

			// At the moment, this is a bit of a hack, as I'm returning ALL
			// images, and just taking the latest one. There is a better way to
			// narrow this down I think with a WHERE clause which is currently
			// the selection variable
			Cursor myCursor = this.managedQuery(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					projection, selection, null, sort);

			long imageId = 0l;
			long thumbnailImageId = 0l;
			String thumbnailPath = "";

			try {
				myCursor.moveToFirst();
				imageId = myCursor
						.getLong(myCursor
								.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
				thumbnailImageId = myCursor
						.getLong(myCursor
								.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID));
				thumbnailPath = myCursor
						.getString(myCursor
								.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
			} finally {
				myCursor.close();
			}

			// Create new Cursor to obtain the file Path for the large image

			String[] largeFileProjection = {
					MediaStore.Images.ImageColumns._ID,
					MediaStore.Images.ImageColumns.DATA };

			String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
			myCursor = this.managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					largeFileProjection, null, null, largeFileSort);
			String largeImagePath = "";

			try {
				myCursor.moveToFirst();

				// This will actually give yo uthe file path location of the
				// image.
				largeImagePath = myCursor
						.getString(myCursor
								.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
			} finally {
				myCursor.close();
			}
			// These are the two URI's you'll be interested in. They give you a
			// handle to the actual images
			Uri uriLargeImage = Uri.withAppendedPath(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					String.valueOf(imageId));
			Uri uriThumbnailImage = Uri.withAppendedPath(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					String.valueOf(thumbnailImageId));

			// I've left out the remaining code, as all I do is assign the URI's
			// to my own objects anyways...
			startFeather(uriLargeImage);
		
	
		}
		}
		catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), "Unable To Take Photo, Please Try Again", Toast.LENGTH_LONG).show();
		}

	}
	private void loadAsync( final Uri uri ) {
		Log.i( LOG_TAG, "loadAsync: " + uri );

//		Drawable toRecycle = mImage.getDrawable();
//		if ( toRecycle != null && toRecycle instanceof BitmapDrawable ) {
//			if ( ( (BitmapDrawable) mImage.getDrawable() ).getBitmap() != null )
//				( (BitmapDrawable) mImage.getDrawable() ).getBitmap().recycle();
//		}
//		mImage.setImageDrawable( null );
		
	
		
		//DownloadAsync task = new DownloadAsync();
		//task.execute( uri );
	}
	private void updateMedia(String filepath) {
		// TODO Auto-generated method stub
		MediaScannerConnection.scanFile( getApplicationContext(), new String[] { filepath }, null, null );
		
	}

	private void startFeather(Uri mImageUri) {
		// TODO Auto-generated method stub
		try{
		if ( !isExternalStorageAvilable() ) {
		
			showDialog( EXTERNAL_STORAGE_UNAVAILABLE );
			return;
		}
		// create a temporary file where to store the resulting image
		File file = getNextFileName();
		
		
		if ( null != file ) {
			mOutputFilePath = file.getAbsolutePath();
		} else {
			new AlertDialog.Builder( this ).setTitle("Fail" ).setMessage( "Failed to create a new File" )
					.show();
			return;
		}
		// Create the intent needed to start feather
		Intent newIntent = new Intent( this, FeatherActivity.class );

		// === INPUT IMAGE URI (MANDATORY) ===
		// Set the source image uri
		newIntent.setData( mImageUri );
		
		// === API KEY SECRET (MANDATORY) ====
		// You must pass your Aviary key secret
		newIntent.putExtra( Constants.EXTRA_IN_API_KEY_SECRET, "rvdn7ah9v8odj5at" );
		// === OUTPUT (OPTIONAL/RECOMMENDED)====
				// Pass the uri of the destination image file.
				// This will be the same uri you will receive in the onActivityResult
				newIntent.putExtra( Constants.EXTRA_OUTPUT, Uri.parse( "file://" + mOutputFilePath ) );

				// === OUTPUT FORMAT (OPTIONAL) ===
				// Format of the destination image
				newIntent.putExtra( Constants.EXTRA_OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG.name() );

				// === OUTPUT QUALITY (OPTIONAL) ===
				// Output format quality (jpeg only)
				newIntent.putExtra( Constants.EXTRA_OUTPUT_QUALITY, 100 );
				
				
				// === WHITE LABEL (OPTIONAL/PREMIUM ONLY) ===
				// If you want to hide the 'feedback' button and the 'aviary' logo
				// pass this intent-extra
				// Note that you need to have the 'whitelabel' permissions enabled in order
				// to use this extra
			
				
				// == TOOLS LIST ===
				// Optional
				// You can force feather to display only some tools ( see FilterLoaderFactory#Filters )
				// you can omit this if you just want to display the default tools

				
//				 newIntent.putExtra( "tools-list", new String[] { 
//					 FilterLoaderFactory.Filters.ENHANCE.name(),
//					 FilterLoaderFactory.Filters.EFFECTS.name(), 
//					 FilterLoaderFactory.Filters.STICKERS.name(),
//					 FilterLoaderFactory.Filters.CROP.name(), 
//					 FilterLoaderFactory.Filters.TILT_SHIFT.name(),
//					 FilterLoaderFactory.Filters.ADJUST.name(), 
//					 FilterLoaderFactory.Filters.BRIGHTNESS.name(), 
//					 FilterLoaderFactory.Filters.CONTRAST.name(), 
//					 FilterLoaderFactory.Filters.SATURATION.name(), 
//					 FilterLoaderFactory.Filters.COLORTEMP.name(),
//					 FilterLoaderFactory.Filters.SHARPNESS.name(), 
//					 FilterLoaderFactory.Filters.COLOR_SPLASH.name(),
//					 FilterLoaderFactory.Filters.DRAWING.name(), 
//					 FilterLoaderFactory.Filters.TEXT.name(), 
//					 FilterLoaderFactory.Filters.RED_EYE.name(), 
//					 FilterLoaderFactory.Filters.WHITEN.name(), 
//					 FilterLoaderFactory.Filters.BLEMISH.name(),
//					 FilterLoaderFactory.Filters.MEME.name(),
//				 } );
				 

				// === EXIT ALERT (OPTIONAL) ===
				// Uou want to hide the exit alert dialog shown when back is pressed
				// without saving image first
				// newIntent.putExtra( Constants.EXTRA_HIDE_EXIT_UNSAVE_CONFIRMATION, true );

				// === VIBRATION (OPTIONAL) ===
				// Some aviary tools use the device vibration in order to give a better experience
				// to the final user. But if you want to disable this feature, just pass
				// any value with the key "tools-vibration-disabled" in the calling intent.
				// This option has been added to version 2.1.5 of the Aviary SDK
				// newIntent.putExtra( Constants.EXTRA_TOOLS_DISABLE_VIBRATION, true );

				// === MAX SIZE (OPTIONAL) ===
				// you can pass the maximum allowed image size (for the preview), otherwise feather will determine
				// the max size based on the device informations.
				// This will not affect the hi-res image size.
				// Here we're passing the current display size as max image size because after
				// the execution of Aviary we're saving the HI-RES image so we don't need a big
				// image for the preview
				/*final DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics( metrics );
				int max_size = Math.max( metrics.widthPixels, metrics.heightPixels );
				max_size = (int) ( (float) max_size / 1.2f );
				newIntent.putExtra( Constants.EXTRA_MAX_IMAGE_SIZE, max_size );
				newIntent.putExtra( Constants.EXTRA_IN_SAVE_ON_NO_CHANGES, true );*/
				
				// ..and start feather
				startActivityForResult( newIntent, ACTION_REQUEST_FEATHER );
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("exception", e.toString());
		}
	}

	private File getNextFileName() {
		// TODO Auto-generated method stub
		if ( mGalleryFolder != null ) {
			if ( mGalleryFolder.exists() ) {
				File file = new File( mGalleryFolder, "PhotoEffect" + System.currentTimeMillis() + ".jpg" );
				return file;
			}
		}
		return null;
	
	}

	private boolean isExternalStorageAvilable() {
		
		String state = Environment.getExternalStorageState();
		if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
			return true;
		}
		return false;
		
	}

	public Uri setImageUri() {
		// Store image in dcim
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/DCIM/", "image" + new Date().getTime() + ".png");
		Uri imgUri = Uri.fromFile(file);
		return imgUri;
	}

	public void cameraImageCapture() {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);

	}

	public String getPath(Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };

		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	private boolean setImageURI( final Uri uri, final Bitmap bitmap ) {

		Log.d( LOG_TAG, "image size: " + bitmap.getWidth() + "x" + bitmap.getHeight() );
		mImage.setImageBitmap( bitmap );
		mImage.setBackgroundDrawable( null );

		
		selectedImage = uri;
		return true;
	}

//	class DownloadAsync extends AsyncTask<Uri, Void, Bitmap> implements OnCancelListener {
//
//		ProgressDialog mProgress;
//		private Uri mUri;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//
//			mProgress = new ProgressDialog( Start.this );
//			mProgress.setIndeterminate( true );
//			mProgress.setCancelable( true );
//			mProgress.setMessage( "Loading image..." );
//			mProgress.setOnCancelListener( this );
//			mProgress.show();
//		}
//
//		@Override
//		protected Bitmap doInBackground( Uri... params ) {
//			mUri = params[0];
//			Bitmap bitmap = null;
//
//			while ( mImageContainer.getWidth() < 1 ) {
//				try {
//					Thread.sleep( 1 );
//				} catch ( InterruptedException e ) {
//					e.printStackTrace();
//				}
//			}
//
//			final int w = mImageContainer.getWidth();
//			Log.d( LOG_TAG, "width: " + w );
//			ImageSizes sizes = new ImageSizes();
//			bitmap = DecodeUtils.decode( Start.this, mUri, imageWidth, imageHeight, sizes );
//			return bitmap;
//		}
//
//		@Override
//		protected void onPostExecute( Bitmap result ) {
//			super.onPostExecute( result );
//
//			if ( mProgress.getWindow() != null ) {
//				mProgress.dismiss();
//			}
//
//			if ( result != null ) {
//				setImageURI( mUri, result );
//			} else {
//				Toast.makeText( Start.this, "Failed to load image " + mUri, Toast.LENGTH_SHORT ).show();
//			}
//		}
//
//		@Override
//		public void onCancel( DialogInterface dialog ) {
//			Log.i( LOG_TAG, "onProgressCancel" );
//			this.cancel( true );
//		}
//
//		@Override
//		protected void onCancelled() {
//			super.onCancelled();
//			Log.i( LOG_TAG, "onCancelled" );
//		}

	}

