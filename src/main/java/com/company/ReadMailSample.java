package com.company;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ReadMailSample {
    Properties properties = null;
    private Session session = null;
    private Store store = null;
    private Folder inbox = null;
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
    static DateFormat fileDateFormat = new SimpleDateFormat("yyyy-MMM-dd-HH-mm-ss");
    static String fileName = "";
    static String startDateAsString = "";
    static String endDateAsString = "";
    static Date startDate =null;
    static Date endDate =null;

    static boolean checkDate = false;

    static String userName = "";
    static String password = "";


    public static void main(String[] args) throws Exception{
        try {
            userName = args[0];
            password = args[1];
            startDateAsString = args[2];
            endDateAsString = args[3];
            /*userName = "ajeetc@dewsolutions.in";
            password = "ankitpavan080901";
            startDateAsString = "08-10-2018";
            endDateAsString = "08-10-2018";*/
            startDate = new SimpleDateFormat("dd-MM-yyyy").parse(startDateAsString);
            endDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDateAsString);
            if(startDate.after(endDate)) {
                System.out.println("End Date should be greater than StartDate.");
                System.exit(0);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("[Please Run the program as ][java -jar ReadMailSample \"userName\" \"password\" \"startDate[dd-mm-yyyy\" \"endDate[dd-mm-yyyy\"");
            System.exit(0);
        }
        IMAPFolder folder = null;
        Store store = null;
        String subject = null;
        Flag flag = null;
        try
        {
            fileName = fileDateFormat.format(System.currentTimeMillis())+".txt";
            File f = new File(fileName);
            fileName = f.getAbsolutePath();
            f.createNewFile();
           // Files.write(Paths.get(fileName), ("Date#API Name#API#Response Time (ms)#Response Time (sec)").getBytes(), StandardOpenOption.APPEND);
            try {


                //Files.write(Paths.get(fileName), ("\n" +text).getBytes(), StandardOpenOption.APPEND);
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
                out.println("Date#API Name#API#Response Time (ms)#Response Time (sec)");
                out.close();
            }
            catch (Exception ex) {ex.printStackTrace();}
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);

            store = session.getStore("imaps");
            store.connect("imap.googlemail.com",userName, password);
            folder = (IMAPFolder) store.getFolder("[Gmail]/Sent Mail"); // This doesn't work for other email account

            if(!folder.isOpen())
                folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            System.out.println("No of Messages : " + folder.getMessageCount());
            System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
            //System.out.println(messages.length);
            int counter = 1;
            for (int i=messages.length-1; i >0; i--)
            {
                Message msg =  messages[i];
                subject = msg.getSubject();
                System.out.println("MESSAGE " + (counter) + "-Total "+messages.length+" | "+ ((((counter)*100)/messages.length))+"% Completed | "+msg.getReceivedDate().toString());
                counter++;
                //System.out.println("Subject: " + subject);
                Date date = msg.getReceivedDate();
                if(date.before(startDate)){
                    break;
                }
                if(date.after(endDate)) {
                    continue;
                }
                if(null != subject && subject.indexOf("API monitoring status report")>-1 ) {
                    try {
                        String strDate = dateFormat.format(date);
                        String text = msg.getContent().toString();
                        String text1 = "";
                        if(text.indexOf("TataCliQ APIs")>-1) {
                            text = text.substring(text.indexOf("TataCliQ APIs"));
                            text = text.substring(text.indexOf("</tr>") + 5);
                            text1 = text.substring(0, text.indexOf("</table>"));
                            text = text.substring(text.indexOf("</table>") + 8);
                        }
                        String text2 = "";
                        if(text.indexOf("Middle Layer APIs")>-1) {
                            text = text.substring(text.indexOf("Middle Layer APIs"));
                            text = text.substring(text.indexOf("</tr>") + 5);
                            text2 = text.substring(0, text.indexOf("</table>"));

                        }
                        String finalText = text1 + text2;
                        finalText = finalText.replaceAll("</tr>", "</tr>\n");
                        finalText = finalText.replaceAll("<tr>", "<tr><td>" + strDate + "</td>");


                        parseText(finalText);
                        //System.out.println(finalText);
                    }catch (Exception ex) {
                        System.out.println("Error in parsing." +msg.getSubject());
                        ex.printStackTrace();
                    }
                }
                //break;
            }
        }
        finally
        {
            if (folder != null && folder.isOpen()) { folder.close(true); }
            if (store != null) { store.close(); }
        }

    }
    static void parseText(String finalText) {
        String dataA [] = finalText.split("</tr>");
        String text = "";
        for(int i=0;i<dataA.length;i++) {
            if(null!= dataA[i] && dataA[i].indexOf("</td>")>-1) {
                String [] dataB = dataA[i].split("</td>");
                text = dataB[0].substring(dataB[0].indexOf("<td>")+4)+"#"+dataB[1].substring(dataB[1].indexOf("<td>")+4)+"#"+(dataB[2].substring(dataB[2].indexOf(">http")+1)).replace("</a>","")+"#"+(dataB[3].substring(dataB[3].lastIndexOf("<b>")+3)).replace("</b>","")+"#"+(dataB[4].substring(dataB[4].lastIndexOf("<b>")+3)).replace("</b>","");
            }
            System.out.println(text);
            try {


                //Files.write(Paths.get(fileName), ("\n" +text).getBytes(), StandardOpenOption.APPEND);
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
                out.println(text);
                out.close();
            }
            catch (Exception ex) {ex.printStackTrace();}
        }

    }
    }
