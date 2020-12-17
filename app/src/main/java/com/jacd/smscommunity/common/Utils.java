package com.jacd.smscommunity.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.jacd.smscommunity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {


    private static String CHANNEL_ID = "12";

    public static String StringJoiner(String delimiter, String... args) throws NullPointerException {
        String response = "";
        int i = 0;
        for (String value :
                args) {

            if (args.length > 1)
                response = i <= args.length - 2 ? response + value + delimiter : response + value;

            i++;
        }
        return response;
    }

    public static void customDialogMessage(Context context, @NonNull String title, @NonNull String message) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_general_message);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tv_message = dialog.findViewById(R.id.tv_dialog_message);
        TextView tv_title = dialog.findViewById(R.id.tv_dialog_title);
        tv_message.setText(message);
        tv_title.setText(title);

        Button btn_ok = dialog.findViewById(R.id.btn_dialog_ok);
        btn_ok.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    public static Intent getCustomFileChooserIntent(String ...types){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // Filter to only show results that can be "opened"
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
        return intent;
    }

    public static Dialog customDialogWait(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

   @SuppressLint("ResourceAsColor")
   public static void getMessageSnackBar(@NonNull View view, String msg, @NonNull Context context, @NonNull boolean isMsgError){
       msg = (""+msg).replace("null","No Message");
       Snackbar snackbar = Snackbar.make(view, ""+msg, Snackbar.LENGTH_LONG);
       snackbar.setActionTextColor( (isMsgError) ?context.getResources().getColor(R.color.colorControlNormal) :context.getResources().getColor(R.color.colorDarkPrimary) );
       View sbView = snackbar.getView();
       sbView.setBackgroundColor(ContextCompat.getColor(context, (isMsgError) ?R.color.error_toast :R.color.colorPrimary));
       snackbar.show();
   }

    public static String reformateDate( String strDate, String originFormat, String newFormat) {
        Date date = null;
        String resp = null;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat parseador = new SimpleDateFormat(originFormat);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formateador = new SimpleDateFormat(newFormat);

        try {
            date = parseador.parse(strDate);
            resp = formateador.format(date);
            return resp;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void animateLayout(boolean show_or_hide, LinearLayout thisLayout){
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (show_or_hide){
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            //animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else{    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            //animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        thisLayout.setLayoutAnimation(controller);
        thisLayout.startAnimation(animation);
        thisLayout.setVisibility(show_or_hide?View.VISIBLE:View.GONE);
    }

    private static String urlToB64(String originalUrl){
        return "";//Base64.getUrlEncoder().encodeToString(originalURL.getBytes());
    }

    public interface LoadDialogInterface {
        void onLoadDialog(boolean error);

    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static String bitmapToBase64(Bitmap bitmap,@NonNull int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap scaleBitmap(final Bitmap input, final long maxBytes) {
        final int currentWidth = input.getWidth();
        final int currentHeight = input.getHeight();
        final int currentPixels = currentWidth * currentHeight;
        // Get the amount of max pixels:
        // 1 pixel = 4 bytes (R, G, B, A)
        final long maxPixels = maxBytes / 4; // Floored
        if (currentPixels <= maxPixels) {
            // Already correct size:
            return input;
        }
        // Scaling factor when maintaining aspect ratio is the square root since x and y have a relation:
        final double scaleFactor = Math.sqrt(maxPixels / (double) currentPixels);
        final int newWidthPx = (int) Math.floor(currentWidth * scaleFactor);
        final int newHeightPx = (int) Math.floor(currentHeight * scaleFactor);
        final Bitmap output = Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true);
        return output;
    }


    /**
     * Verify string is numeric
     *
     * @param strNum
     * @return
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String setHTMLMessageNotification(String title, String origin, String destination, String time, String coordinates, String env, String type, String user, String email, String price, String distance, String phone) {

        String line = "<br>";
        String head = " <h1 style=\"background-color:Orange;\">" + title + "</h1> " + line;
        String usr = "<strong>Usuario: </strong>" + user + line;
        String ema = "<strong>Correo Electrónico: </strong>" + email + line;
        String ori = "<strong>Desde: </strong>" + origin + line;
        String des = "<strong>Hasta: </strong>" + destination + line;
        String tim = "<strong>Fecha y Hora: </strong>" + time + line;
        String prc = "<strong>Precio: </strong>" + price + line;
        String dst = "<strong>Distancia: </strong>" + distance + line;
        String telf = "<strong>Teléfono: </strong>" + phone + line;
        String en = "<strong>Entorno: </strong>" + env + line;
        String imMap = "<br><br><img width=\"600\" src=\"https://maps.googleapis.com/maps/api/staticmap?center=" + origin.replaceAll(" ", "+") + "&zoom=13&scale=1&size=600x300&maptype=roadmap&key=AIzaSyAIiXVbt3z9zRyjpAW2-b7eB9JIgWP7PGI&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff965c%7Clabel:O%7C" + origin.replaceAll(" ", "+") + "&markers=size:mid%7Ccolor:0x3f66ff%7Clabel:D%7C" + destination.replaceAll(" ", "+") + "\" alt=\"Google Map VE\">";


        String coord = "https://www.google.com/maps/dir/?api=1&origin=" + origin.replaceAll(" ", "+") + "&destination=" + destination.replaceAll(" ", "+") + "&travelmode=car";
        String coord2 = "<a href=\"" + coord + "\">Ver en Google Maps</a>";
        return head + usr + ema + telf + ori + des + tim + prc + dst + coord2 + imMap;
    }



    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Validate Email
     *
     * @param emailStr
     * @return
     */

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    /**
     * Get Date in format dd-MM-yyyy or format required
     *
     * @param indate
     * @param outputDFormat
     * @return
     */
    public static String getDateDay(String indate, String outputDFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date1 = sdf.parse(indate);
            outputDFormat = outputDFormat == null || outputDFormat.isEmpty() ? "dd-MM-yyyy" : outputDFormat;
            SimpleDateFormat time = new SimpleDateFormat(outputDFormat);
            String time_s = time.format(date1);
            return time_s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date StringToDate(String format, String date) {
        SimpleDateFormat formatter = format!=null
                ?new SimpleDateFormat(format, Locale.US)
                :new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String formatDecimal(double value) {
        //  DecimalFormat df = new DecimalFormat("#,##");
        //  return df.format(value);


        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%(,.2f", value);
        // System.out.println("Amount is - " + sb);

        String parsed_val = sb.toString().replace(".", "&");
        parsed_val = parsed_val.replaceAll(",", ".");
        parsed_val = parsed_val.replaceAll("&", ",");

        return parsed_val;
    }

    public static String epochToDate(long epoch, String formatOut){
        SimpleDateFormat sdf;
        if (formatOut == null)
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        else
            sdf = new SimpleDateFormat(formatOut + "");

        Date dt = null;
        try {

            sdf = new SimpleDateFormat(formatOut);
            return sdf.format(new Date(epoch));


        } catch (Exception e) {
            e.printStackTrace();
            return "N/A";
        }


    }

    public static String ramdomString(@NonNull int max_length){
        max_length = max_length==0 ? 4 : max_length;
        String uuid = UUID.randomUUID().toString();
        uuid =  "" + uuid.replaceAll("-", "");
        return uuid.length()>max_length?uuid.substring(0,max_length):uuid;
    }

    public static ArrayList<String> JSONArrayToArrayString(@NonNull JSONArray jArray) throws Exception{
        ArrayList<String> listdata = new ArrayList<String>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getString(i));
            }
        }
       return listdata;
    }

    @SuppressLint("NewApi")
    public static Date sumaRestarFecha(Date fecha, int sumaresta, String opcion) {
        Calendar calendar = Calendar.getInstance();
        try
        {

            calendar.setTime(fecha);
            switch (opcion.toUpperCase())
            {
                case "DAYS":
                    calendar.add(Calendar.DAY_OF_WEEK, sumaresta);

                    break;

                case "MONTHS":
                    calendar.add(Calendar.MONTH, sumaresta);

                    break;

                case "YEARS":
                    calendar.add(Calendar.YEAR, sumaresta);

                    break;

                default:
                    System.out.println("parametro enviado al Switch no concuerda");
                    break;
            }
        }
        catch(Exception e) {
            System.out.println("Error:\n" + e);
        }
        return calendar.getTime();
    }

    @SuppressLint("NewApi")
    public static Date sumaRestarDias(Date fecha, int sumaresta ) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(fecha);
            calendar.add(Calendar.DAY_OF_WEEK, sumaresta);


        } catch(Exception e) {
            System.out.println("Error:\n" + e);
        }
        return calendar.getTime();
    }

    public static long dateToEpoch(String format, String date) {
        SimpleDateFormat sdf;
        if (format == null)
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        else
            sdf = new SimpleDateFormat(format + "");


        Date dt = null;
        try {
            dt = sdf.parse(date);
            long epoch = dt.getTime();
            int num = (int) (epoch / 1000);
            return epoch;

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Verify if is valid email
     *
     * @param email
     * @return
     */

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /**
     * Convert a text file to String
     *
     * @param is
     * @return
     * @throws IOException
     */
//InputStream is = getResources().getAssets().open("SQLScript.sql");
//String sql= convertStreamToString(is);
    public static String convertStreamToString(InputStream is)
            throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        String text = writer.toString();
        return text;
    }

    public static String getToday(String format) {
        String f = format != null ? format : "yyyy/MM/dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(f);
        Date date = new Date();
        String res = dateFormat.format(date);
        System.out.println(res);
        return res;

    }


    public static  double StringFormatedToDouble(String num){

        try {
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            num= num.replace(".","@");
            num= num.replace(",",".");
            num=  num.replace("@",",");
            Number number = format.parse(num);
            return number.doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Encode image Bitmap to Base64
     *
     * @param bm
     * @return
     */
    public static String encodeImage(Bitmap bm) {
        Log.w("encodeImage", "image bounds: " + bm.getWidth() + ", " + bm.getHeight());

        if (bm.getHeight() <= 400 && bm.getWidth() <= 400) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return (Base64.encodeToString(b, Base64.DEFAULT)).replaceAll("\\n", "");
        }
        int mHeight = 400;
        int mWidth = 400;

        if (bm.getHeight() > bm.getWidth()) {
            float div = (float) bm.getWidth() / ((float) bm.getHeight());
            float auxW = div * 480;
            mHeight = 480;
            mWidth = Math.round(auxW);
            Log.w("encodeImage", "new high: " + mHeight + " width: " + mWidth);
        } else {
            float div = ((float) bm.getHeight()) / (float) bm.getWidth();
            float auxH = div * 480;
            mWidth = 480;
            mHeight = 360;
            mHeight = Math.round(auxH);
            Log.w("encodeImage", "new high: " + mHeight + " width: " + mWidth);
        }

        bm = Bitmap.createScaledBitmap(bm, mWidth, mHeight, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return (Base64.encodeToString(b, Base64.DEFAULT)).replaceAll("\\n", "");
    }


    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        //   dialog.setContentView(R.layout.dialog);

        // TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        //  text.setText(msg);


        dialog.show();

    }


    public static String dateToString(Date d, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format != null ? format + "" : "yyyy-MM-dd HH:mm:ss");
        String stringDate = dateFormat.format(d);
        return stringDate;
    }


    public static boolean isNumberOrDecimal(String string) {
        try {
            float amount = Float.parseFloat(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * return 1 - obj 2 - array  -1 - error
     *
     * @param response
     * @return
     */
    public static int isArrayOrObjectJSON(String response) {
        try {
            String data = response.contains("error 500") ? response.substring(8) : response;
            Object json = new JSONTokener(data).nextValue();
            if (json instanceof JSONObject) {
                JSONObject object = new JSONObject(data);
                return 1;
            } else if (json instanceof JSONArray) {

                JSONArray array = new JSONArray(data);
                return 2;
            }

        } catch (JSONException jsex) {
            jsex.printStackTrace();
            return -1;
        }
        return 0;

    }

    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{11}")) return true;
            //validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{4}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{4}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }



    /**
     * Math round with decimals
     *
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public enum typeValue {
        INTEGER, BOOLEAN, STRING, FLOAT, DOUBLE, LONG
    }

    public static int checkNullInt(int value){
        try{
            return value;
        }catch (NumberFormatException nex){
            return 0;
        }
    }


}


