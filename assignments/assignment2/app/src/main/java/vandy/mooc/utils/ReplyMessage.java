package vandy.mooc.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

/**
 * A thin facade around an Android Message that defines the schema of
 * a reply from the Service back to the Activity.
 */
public class ReplyMessage extends RequestReplyMessageBase {
    /**
     * Constructor is private to ensure the makeReplyMessage() factory
     * method is used.
     */
    private ReplyMessage(Message message) {
        super(message);
    }

    /**
     * Convert a Message into a ReplyMessage.
     */
    public static ReplyMessage makeReplyMessage(Message message) {
        // Make a copy of @a message since it may be recycled.
        return new ReplyMessage(Message.obtain(message));
    }

    /**
     * A factory method that creates a reply message to return to the
     * Activity with the pathname of the downloaded image.
     */
    public static ReplyMessage makeReplyMessage(Uri pathToImageFile, Uri url, int requestCode) {
        // Create a ReplyMessage that holds a reference to a Message
        // created via the Message.obtain() factory method.
        final ReplyMessage replyMessage = new ReplyMessage(Message.obtain());

        // Create a new Bundle to handle the result.
        // DONE -- you fill in here.
        final Bundle bundle = new Bundle();

        // Set the Bundle to be the data in the message.
        // DONE -- you fill in here.
        replyMessage.setData(bundle);

        // Put the URL to the image file into the Bundle
        // DONE -- you fill in here.
        replyMessage.setImageURL(url);

        // Put the requestCode into the Bundle
        // DONE -- you fill in here.
        replyMessage.setRequestCode(requestCode);

        // Set the result code to indicate whether the download
        // succeeded or failed.
        // DONE -- you fill in here.
        System.out.println("yokey: " + String.valueOf(pathToImageFile));
        if(pathToImageFile != null) {
            replyMessage.setResultCode(Activity.RESULT_OK);

            // Put the path to the image file into the Bundle
            // only if the download succeeded.
            // DONE -- you fill in here.
            replyMessage.setImagePathname(pathToImageFile.toString());
        } else {
            replyMessage.setResultCode(Activity.RESULT_CANCELED);
        }


        return replyMessage;
    }
}
