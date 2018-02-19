package com.nestletime.mediagallery.filesystem;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class CursorManager {
    public static Cursor getCursorForMedia(Context context) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.SIZE
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(
                context,
                queryUri,
                projection,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC" // Sort order.
        );

        return cursorLoader.loadInBackground();
    }


    public static int getVideoDuration(Context context, int fileId) {
        Uri uri = Uri.parse(MediaStore.Video.Media.EXTERNAL_CONTENT_URI + "/" + fileId);
        return getVideoDuration(context, uri);
    }

    public static long getVideoIdFromFilePath(Context context, String filePath) {
        long videoId = -1;
        Uri videosUri = MediaStore.Video.Media.getContentUri("external");
        String[] projection = {MediaStore.Video.VideoColumns._ID};
        Cursor cursor = context.getContentResolver().query(videosUri, projection, MediaStore.Video.VideoColumns.DATA + " LIKE ?", new String[]{filePath}, null);
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(projection[0]);
                videoId = cursor.getLong(columnIndex);
            }
            cursor.close();
        }
        return videoId;
    }

    public static long getImageIdFromFilePath(Context context, String filePath) {
        long videoId = -1;
        Uri videosUri = MediaStore.Images.Media.getContentUri("external");
        String[] projection = {MediaStore.Video.VideoColumns._ID};
        Cursor cursor = context.getContentResolver().query(videosUri, projection, MediaStore.Images.ImageColumns.DATA + " LIKE ?", new String[]{filePath}, null);
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(projection[0]);
                videoId = cursor.getLong(columnIndex);
            }
            cursor.close();
        }
        return videoId;
    }


    public static int getVideoDuration(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Video.VideoColumns.DURATION}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int value = cursor.getInt(0);
            cursor.close();
            return value;
        }

        return -1;
    }
}
