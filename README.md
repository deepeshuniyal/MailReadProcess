# Mail Read From Gmail

1. Setup Gmail
For reading email from gmail we need to set-up the IMAP and extra security should be disable. below are the steps.

  i)
  On your computer, open Gmail.
    In the top right, click Settings Settings.
    Click Settings.
    Click the Forwarding and POP/IMAP tab.
    In the "IMAP Access" section, select Enable IMAP.
    Click Save Changes.
 
 ii) 
 Allow for the less secure apps;-
      https://myaccount.google.com/lesssecureapps
 
 iii) 
 Display Unlock Capta
    https://accounts.google.com/b/0/DisplayUnlockCaptcha

    You can check the details  here https://support.google.com/mail/accounts/answer/78754

    **** IMPORTANT in 3rd step where you unlocking the Captcha its showing account number aftr /b/{accountNumber} this is important if you added multiple accounts. please ensure you are giving the permision to correct account number.
    
    In case of confusion you can do this step from incognito mode.


2. Pull and run the program
git clone  https://github.com/deepeshuniyal/MailReadProcess.git
cd MailReadProcess/target/
java -jar ReadMailSample "userName" "password" "startDate[dd-mm-yyyy" "endDate[dd-mm-yyyy"

Ex for 01 Oct Data:-

java -jar ReadMailSample "userName" "password" "01-10-2018" "02-10-2018"
