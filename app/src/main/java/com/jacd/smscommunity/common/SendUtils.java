package com.jacd.smscommunity.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jacd.smscommunity.model.ContactsModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.android.AndroidCustomFieldScribe;
import ezvcard.android.ContactOperations;
import ezvcard.io.text.VCardReader;
import ezvcard.property.Telephone;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import static ezvcard.util.IOUtils.closeQuietly;

public abstract class SendUtils {

    public static void permissionReadStorage(){

    }

    public static void permissionWriteStorage(){

    }

    public static boolean permissionSEND_SMS(Context context){
        PackageManager pm = context.getPackageManager();

        return (!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) &&
                !pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA));

    }

    public static void sendMessageSMS(Context context, String strPhone,final String strMessage, boolean dialogShow){
        //String strPhone = "XXXXXXXXXXX";
        //String strMessage = "LoremnIpsum";

        SmsManager sms = SmsManager.getDefault();

        if (strMessage.length() < 150){
            sms.sendTextMessage(strPhone, null, strMessage, null, null);
        }else {
            ArrayList messageParts = sms.divideMessage(strMessage);
            sms.sendMultipartTextMessage(strPhone, null, messageParts, null, null);
        }

       if (dialogShow) Toast.makeText(context, "Sent.", Toast.LENGTH_SHORT).show();
    }


    public abstract static class FilesTask {

        public static List<ContactsModel> getContactForExcel(Context context, @NonNull String nameFile, File file, final Uri uri){
            List<ContactsModel> list = new ArrayList<>();

            WorkbookSettings ws = new WorkbookSettings();
            ws.setGCDisabled(true);
            if(file != null){
                //text.setText("Success, DO something with the file.");
                try {
                    Workbook workbook = Workbook.getWorkbook(file, ws);
                    Sheet sheet = workbook.getSheet(0);
                    //Cell[] row = sheet.getRow(1);
                    //text.setText(row[0].getContents());
                    for(int i = 0;i< sheet.getRows();i++){
                        Cell[] row = sheet.getRow(i);

                        ContactsModel cm = new ContactsModel();
                        String storyTitle = row[0].getContents();
                        String storyContent = row[1].getContents().trim();
                       if (storyContent.length()!=0)storyContent = storyContent.substring(0,1).equals("0")?storyContent:"0"+storyContent;

                        cm.setName(storyTitle);
                        cm.setNumber(storyContent);
                        cm.setTopic(nameFile);
                        list.add(cm);
                    }
                } catch (IOException | BiffException e) {
                    e.printStackTrace();
                    list = ExcelUtil.readExcelNew(context, uri, uri.getPath());
                    //Toast.makeText(context, "Ha fallado la lectura del archivo", Toast.LENGTH_SHORT).show();
                }
            }
            return list;
        }

        public static void xxxxx(File file){
            try{
                //File file = new File(Environment.getExternalStorageDirectory()+"/vcards.vcf");
                List<VCard> vcards = Ezvcard.parse(file).all();
                for (VCard vcard : vcards){
                    System.out.println("Name: " + vcard.getFormattedName().getValue());
                    System.out.println("Telephone numbers:");
                    for (Telephone tel : vcard.getTelephoneNumbers()){
                        System.out.println(tel.getTypes() + ": " + tel.getText());
                    }
                }
            }catch(Exception e){e.printStackTrace();}
        }

        public static List<ContactsModel> getListVCF(File file, String nameFile){
            List<ContactsModel> list = new ArrayList<>();
            if (nameFile != null){
                String name="N/A";
                String phone = "";
                try{
                    List<VCard> vcards = Ezvcard.parse(file).all();
                    for (VCard vcard : vcards){
                        name = vcard.getFormattedName().getValue();
                        //System.out.println("Telephone numbers:");
                        for (Telephone tel : vcard.getTelephoneNumbers()) {
                            phone = tel.getText();
                        }
                        ContactsModel cm = new ContactsModel();
                        cm.setName(name.isEmpty()||name.equals("null")?"N/A":name);
                        cm.setNumber(phone);
                        cm.setTopic(nameFile);
                        list.add(cm);

                    }
                }catch(Exception e){e.printStackTrace();}
            }
            return list;
        }

        public static void xxxxx(Context context, String filePath){
            File vcardFile = new File(filePath);
            VCardReader reader = null;
            try {
                reader = new VCardReader(vcardFile);
                reader.registerScribe(new AndroidCustomFieldScribe());

                ContactOperations operations = new ContactOperations(context, null, null);

                //insert contacts with specific account_name and their types. For example:
                //both account_name=null and account_type=null if you want to insert contacts into phone
                //you can also pass other accounts

                VCard vcard = null;
                while ((vcard = reader.readNext()) != null) {
                    operations.insertContact(vcard);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                closeQuietly(reader);
            }
        }

        /*
        public static void xxxxxxxxxxxxx(File filename){
            List sheetData = new ArrayList();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(filename);
                XSSFWorkbook workbook = new XSSFWorkbook(fis);
                XSSFSheet sheet = workbook.getSheetAt(0);
                Iterator rows = sheet.rowIterator();

                while (rows.hasNext()) {
                    XSSFRow row = (XSSFRow) rows.next();
                    Iterator cells = row.cellIterator();
                    List data = new ArrayList();
                    while (cells.hasNext()) {
                        XSSFCell cell = (XSSFCell) cells.next();
                        data.add(cell);
                    }

                    sheetData.add(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    */

    }


}
