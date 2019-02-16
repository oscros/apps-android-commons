package fr.free.nrw.commons.upload;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.free.nrw.commons.HandlerService;
import fr.free.nrw.commons.R;
import fr.free.nrw.commons.auth.SessionManager;
import fr.free.nrw.commons.contributions.Contribution;
import fr.free.nrw.commons.kvstore.BasicKvStore;
import fr.free.nrw.commons.settings.Prefs;
import fr.free.nrw.commons.utils.ViewUtil;
import timber.log.Timber;

@Singleton
public class UploadController {
    private UploadService uploadService;
    private SessionManager sessionManager;
    private Context context;
    private BasicKvStore defaultKvStore;
    public boolean[] CCN;

    public interface ContributionUploadProgress {
        void onUploadStarted(Contribution contribution);
    }


    @Inject
    public UploadController(SessionManager sessionManager,
                            Context context,
                            BasicKvStore store) {
        this.sessionManager = sessionManager;
        this.context = context;
        this.defaultKvStore = store;
    }

    private boolean isUploadServiceConnected;
    public ServiceConnection uploadServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            uploadService = (UploadService) ((HandlerService.HandlerServiceLocalBinder) binder).getService();
            isUploadServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // this should never happen
            isUploadServiceConnected = false;
            Timber.e(new RuntimeException("UploadService died but the rest of the process did not!"));
        }
    };

    /**
     * Prepares the upload service.
     */
    void prepareService() {
        Intent uploadServiceIntent = new Intent(context, UploadService.class);
        uploadServiceIntent.setAction(UploadService.ACTION_START_SERVICE);
        context.startService(uploadServiceIntent);
        context.bindService(uploadServiceIntent, uploadServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Disconnects the upload service.
     */
    void cleanup() {
        if (isUploadServiceConnected) {
            context.unbindService(uploadServiceConnection);
        }
    }

    /**
     * Starts a new upload task.
     *
     * @param contribution the contribution object
     */
    void startUpload(Contribution contribution) {
        startUpload(contribution, c -> {});
    }

    /**
     * Starts a new upload task.
     *
     * @param contribution the contribution object
     * @param onComplete   the progress tracker
     */
    @SuppressLint("StaticFieldLeak")
    private void startUpload(final Contribution contribution, final ContributionUploadProgress onComplete) {
        //Set creator, desc, and license
        CCN = new boolean[35];
        CCN[0] = true;
        // If author name is enabled and set, use it
        if (defaultKvStore.getBoolean("useAuthorName", false)) {
            CCN[1] = true;
            String authorName = defaultKvStore.getString("authorName", "");
            contribution.setCreator(authorName);
        } else {
            CCN[23] = true;
        }

        if (TextUtils.isEmpty(contribution.getCreator())) {
            CCN[2] = true;
            Account currentAccount = sessionManager.getCurrentAccount();
            if (currentAccount == null) {
                CCN[3] = true;
                Timber.d("Current account is null");
                ViewUtil.showLongToast(context, context.getString(R.string.user_not_logged_in));
                sessionManager.forceLogin(context);
                return;
            }
            else {
                CCN[24] = true;
            }
            contribution.setCreator(sessionManager.getAuthorName());
        }
        else {
            CCN[25] = true;
        }

        if (contribution.getDescription() == null) {
            CCN[4] = true;
            contribution.setDescription("");
        } else {
            CCN[26] = true;
        }

        String license = defaultKvStore.getString(Prefs.DEFAULT_LICENSE, Prefs.Licenses.CC_BY_SA_3);
        contribution.setLicense(license);

        //FIXME: Add permission request here. Only executeAsyncTask if permission has been granted
        new AsyncTask<Void, Void, Contribution>() {

            // Fills up missing information about Contributions
            // Only does things that involve some form of IO
            // Runs in background thread
            @Override
            protected Contribution doInBackground(Void... voids /* stare into you */) {
                long length;
                ContentResolver contentResolver = context.getContentResolver();
                try {
                    CCN[5] = true;
                    if (contribution.getDataLength() <= 0) {
                        CCN[6] = true;
                        Timber.d("UploadController/doInBackground, contribution.getLocalUri():" + contribution.getLocalUri());
                        AssetFileDescriptor assetFileDescriptor = contentResolver
                                .openAssetFileDescriptor(Uri.fromFile(new File(contribution.getLocalUri().getPath())), "r");
                        if (assetFileDescriptor != null) {
                            CCN[7] = true;
                            length = assetFileDescriptor.getLength();
                            if (length == -1) {
                                CCN[8] = true;
                                // Let us find out the long way!
                                length = countBytes(contentResolver
                                        .openInputStream(contribution.getLocalUri()));
                            } else {
                                CCN[27] = true;
                            }
                            contribution.setDataLength(length);
                        } else {
                            CCN[28] = true;
                        }
                    }
                    else {
                        CCN[29] = true;
                    }
                } catch (IOException e) {
                    CCN[34] = true;
                    Timber.e(e, "IO Exception: ");
                } catch (NullPointerException e) {
                    CCN[9] = true;
                    Timber.e(e, "Null Pointer Exception: ");
                } catch (SecurityException e) {
                    CCN[10] = true;
                    Timber.e(e, "Security Exception: ");
                }

                String mimeType = (String) contribution.getTag("mimeType");
                Boolean imagePrefix = false;

                if (mimeType == null || TextUtils.isEmpty(mimeType) || mimeType.endsWith("*")) {
                    CCN[11] = true;
                    CCN[12] = true;
                    CCN[13] = true;
                    mimeType = contentResolver.getType(contribution.getLocalUri());
                }
                else {
                    CCN[30] = true;
                }

                if (mimeType != null) {
                    CCN[14] = true;
                    contribution.setTag("mimeType", mimeType);
                    imagePrefix = mimeType.startsWith("image/");
                    Timber.d("MimeType is: %s", mimeType);
                }else {
                    CCN[31] = true;
                }

                if (imagePrefix && contribution.getDateCreated() == null) {
                    CCN[15] = true;
                    CCN[16] = true;
                    Timber.d("local uri   " + contribution.getLocalUri());
                    Cursor cursor = contentResolver.query(contribution.getLocalUri(),
                            new String[]{MediaStore.Images.ImageColumns.DATE_TAKEN}, null, null, null);
                    if (cursor != null && cursor.getCount() != 0 && cursor.getColumnCount() != 0) {
                        CCN[17] = true;
                        CCN[18] = true;
                        CCN[19] = true;
                        cursor.moveToFirst();
                        Date dateCreated = new Date(cursor.getLong(0));
                        Date epochStart = new Date(0);
                        if (dateCreated.equals(epochStart) || dateCreated.before(epochStart)) {
                            CCN[20] = true;
                            CCN[21] = true;
                            // If date is incorrect (1st second of unix time) then set it to the current date
                            dateCreated = new Date();
                        }else {
                            CCN[32] = true;
                        }
                        contribution.setDateCreated(dateCreated);
                        cursor.close();
                    } else {
                        CCN[22] = true;
                        contribution.setDateCreated(new Date());
                    }
                }else {
                    CCN[33] = true;
                }
                return contribution;
            }

            @Override
            protected void onPostExecute(Contribution contribution) {
                super.onPostExecute(contribution);
                //Starts the upload. If commented out, user can proceed to next Fragment but upload doesn't happen
                uploadService.queue(UploadService.ACTION_UPLOAD_FILE, contribution);
                onComplete.onUploadStarted(contribution);
            }
        }.executeOnExecutor(Executors.newFixedThreadPool(1)); // TODO remove this by using a sensible thread handling strategy
    }


    /**
     * Counts the number of bytes in {@code stream}.
     *
     * @param stream the stream
     * @return the number of bytes in {@code stream}
     * @throws IOException if an I/O error occurs
     */
    private long countBytes(InputStream stream) throws IOException {
        long count = 0;
        BufferedInputStream bis = new BufferedInputStream(stream);
        while (bis.read() != -1) {
            count++;
        }
        return count;
    }
}
