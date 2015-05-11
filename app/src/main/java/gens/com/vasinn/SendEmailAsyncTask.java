package gens.com.vasinn;

import android.os.AsyncTask;
import android.util.Log;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
    Mail m = new Mail("vasigens@gmail.com", "luxusKaffi");

    public SendEmailAsyncTask(String sendTo,
                                String subject,
                                String body) {
        if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
        String[] toArr = new String[1];
        toArr[0] = sendTo;
        m.setTo(toArr);
        m.setFrom("vasigens@gmail.com");
        m.setSubject(subject);
        m.setBody(body);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
        try {
            m.send();
            return true;
        } catch (AuthenticationFailedException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            Log.e(SendEmailAsyncTask.class.getName(), m.getTo(null) + "failed");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}