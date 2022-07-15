

# Safety App - Mobile Application Development

  App is intended to send Multiple SMS with the last known Location with a "Emergency message". </br>
 - On Long press on **'Emergency'** button, **send SMS** in SafetyApp is initiated with a **5 Second timer**. </br>
 - App fetches your last know location recorded in you phone, this location will be sent to all the saved contacts in SafetyApp. </br>  
 - A **'Cancel'** button appears to cancel the send SMS to all the saved contacts in case, mistakely send SMS is initiated. </br>
 - By this said, ofcourse there is **Add**, **update** & **delete** options for any saved contacts. </br>

## Software & Languages used
- Android Studio - Java, XML



## Screenshots of SafetyApp

|  |  | |
|:-------------------------:|:-------------------------:|:-------------------------:|
| Home Page | Emergency Initiated|Terminate SMS |
| <img alt="" src="./docs/assets/home_page.png" width="162" height="280"> |<img alt="" src="./docs/assets/send_sms.png" width="162" height="280">|<img alt="" src="./docs/assets/cancel_send_sms.png" width="162" height="280">|
|  Display Contact | Save Contact | Save Contact |
|<img alt="" src="./docs/assets/display_contact.png" width="162" height="280">|<img alt="" src="./docs/assets/add_contact_name.png" width="162" height="280">|<img alt="" src="./docs/assets/add_contact_phone.png" width="162" height="280">|


## Sample SMS Recieved

<img src="./docs/assets/myLastKnownLocation.jpg" alt="MyLastKnownLocation"  width="162" height="280" >

## For Nerd
- App uses **Java Thread** concept to run Timer & send SMS seperately from main thread.

## Work Remaining
- Send Custom message as set in settings page.

### Any new ideas or updates are always welcome !
