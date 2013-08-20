/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 * 
 */
package nl.creativeskills.cordovaPlugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import nl.creativeskills.mupdf.*;


@SuppressLint("SetJavaScriptEnabled")
public class MuPDFViewer extends CordovaPlugin {

    protected static final String LOG_TAG = "MuPDFViewer";
    private static String FILE_PREFIX = "file://";
    private static String ASSET = "android_asset";

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action        The action to execute.
     * @param args          JSONArry of arguments for the plugin.
     * @param callbackId    The callback id used when calling back into JavaScript.
     * @return              A PluginResult object with a status and message.
     */
    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) {
    	
        PluginResult.Status status = PluginResult.Status.OK;
        JSONObject params;
        String result = "";
        
        Log.i( LOG_TAG, "Execute called" );

        try {
        	params = data.getJSONObject(0);
        	
            if ( action.equals("openPDF") ) {
            	String fileName = params.getString("fileName");
            	
            	if ( fileName.startsWith( FILE_PREFIX ) )
				{
            		fileName = fileName.replace( FILE_PREFIX, "" );
				}
            	
            	if ( fileName.contains( ASSET ) )
            	{
            		fileName = fileName.replace( "/" + ASSET + "/", "" );
            		
            		result = this.openAsset( fileName );
            	} else {
            		
            		fileName = fileName.startsWith("/") ? fileName : "/" + fileName;
            		fileName = Environment.getExternalStorageDirectory().toString() + fileName;
            		
            		result = this.openPDF( fileName );
				}
            	
                if (result.length() > 0) {
                    status = PluginResult.Status.ERROR;
                }
            }
            else {
                status = PluginResult.Status.INVALID_ACTION;
            }
            
            callbackContext.sendPluginResult( new PluginResult(status, result) );

            if ( status == PluginResult.Status.OK )  {
                return true;
            } else {
                return false;
            }
            
        } catch (JSONException e) {
            callbackContext.sendPluginResult( new PluginResult(PluginResult.Status.JSON_EXCEPTION) );
            return false;
        }
    }
    
    
    /**
     * Display a MuPDF from the specified asset path.
     *
     * @param path           The path to load.
     * @return               "" if ok, or error message.
     */
    
    private String openAsset( String path ) {
    	
    	InputStream input;

    	String filePath = "";
    	String result = "";
    	
        try {
        	String filename = path.substring(path.lastIndexOf("/")+1, path.length());
        	
            input = cordova.getActivity()
					   	   .getApplicationContext()
					   	   .getAssets()
					   	   .open( path );

            // Don't copy the file if it already exists
            File fp = new File(this.cordova.getActivity().getFilesDir() + "/" + filename);
            if (!fp.exists()) {
                this.copy(input, filename);
            }
            
            // change uri to be to the new file in internal storage
            filePath = FILE_PREFIX + this.cordova.getActivity().getFilesDir() + "/" + filename;
            
            result = this.openPDF( filePath );
            
            return result;

        } catch (IOException e) {
        	
            e.printStackTrace();
            Log.e(LOG_TAG, "Error loading asset "+path+": "+ e.toString());
            
            return e.toString();
            
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return e.toString();
		}
	}



	/**
     * Display a MuPDF with the specified URL.
     *
     * @param path           The path to load.
     * @return               "" if ok, or error message.
     */
    public String openPDF(final String path) {
        try {
        	
        	cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Context context = cordova.getActivity().getApplicationContext();
                    Intent intent = new Intent(context, MuPDFActivity.class);
                    intent.setAction( Intent.ACTION_VIEW );
                    String fileName = Environment.getExternalStorageDirectory().toString() + "/" + path;
                    Log.d(LOG_TAG, "load: "+fileName);
                    intent.setData( Uri.parse(fileName) );
                    cordova.getActivity().startActivity( intent );
                }
            });

            return "";
        } catch (android.content.ActivityNotFoundException e) {
        	
            Log.e(LOG_TAG, "Error loading url "+path+":"+ e.toString());
            return e.toString();
        }
    }
    
    private void copy(InputStream in, String fileTo) throws IOException {
        // get file to be copied from assets
        //InputStream in = this.cordova.getActivity().getAssets().open(fileFrom);
        // get file where copied too, in internal storage.
        // must be MODE_WORLD_READABLE or Android can't play it
        FileOutputStream out = this.cordova.getActivity().openFileOutput(fileTo, Context.MODE_WORLD_READABLE);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
        in.close();
        out.close();
    }
}
