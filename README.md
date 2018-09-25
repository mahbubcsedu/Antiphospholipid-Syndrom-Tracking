## Antiphospholipid-Syndrom-Tracking (APS)

### Table of Contents

###  Introduction 

* [1.1 Purpose](#purpose) 

* [1.2 Scope](#scope)

* [1.3 Definitions, Acronyms, and Abbreviations](#definition)

* [1.4 References](#reference)

* [1.5 Overview](#overview) 

## [2 System Overview](#systemdesign)

### [3 Design Considerations](#designconsideration)

* [3.1 Assumptions and Dependencies](#assumption)

* [3.2 Constraints](#constraint)

### [4 APS application] 

* [4.1 Starting application] 

* [4.2 First time settings] 

* [4.3 Settings any time] 

* [4.4 Log in to diary] 

* [4.5 PIN Recovery activity] 

* [4.6 Diary activity] 

* [4.6.1 Peak flow] 

* [4.6.2 Symptoms] 

* [4.6.3 Daily activities] 

* [4.6.4 Nitroxide measurement] 

* [4.6.5 Saving diary information] 

* [4.7 Peak flow activity] 

* [4.8 Medication List activity] 

* [4.9 Medication detail activity] 

* [4.10 Supplement List activity] 

* [4.11 Supplement detail activity]

* [4.12 Location recording service] 

* [4.13 Notification system]

* [4.14 Data sync service] 

### [5 Conclusion] 

APS user document

1Introduction
This is user guide for the EMMES APS android application. This is preliminary guide based on the application which is developed at summer 2014

### <a name="purpose"></a> 1.1 Purpose <br/>
The user guide of APS mobile application is for to make ease of use of this application by the user and also help the future developer to upgrade this to solve some other requirement that was not completed or those are not addressed by the time while the document was written and application was deployed.

### 1.2  Scope <a name="scope"></a> 
This is very quick guide of using the mobile application in best possible way by the user and future developer.

1.3 Definitions, Acronyms, and Abbreviations
See Glossary Document.

### 1.4 References<a name="reference"></a> 
APS design documents

### 1.5 Overview
This user guide is organized step by step with screenshot of the application. Some features are missing or not yet completed or may have implemented but not tested. So, it is hoped that this document will be modified later based on the updated version of the application.

### 2 System Overview
APS android mobile application is for collecting data from pregnant women. The APS application collects information of their day to day life with different medication, Medicare and what they feel after or before certain events of their life. The application entry starts from the clinic by the doctor or medical personnel and is run for certain period of time. (Detail will be added later)

 

### 3 Design Considerations
The application fulfills all the requirements for collecting data from the users

### 3.1 Assumptions and Dependencies
This mobile application only store the data temporarily to the local database though right now the status of data only changed but not deleting the data from the mobile application. This application depends on server side configuration and storage for final data to be stored and distributed. Only the servlet has been design so far for data transfer. This also depends on data from server for location tracking and this should be implemented to the server side. The database has been primary created to local pc and all the process of searching for data to server database and then transfer also has been included. So later, the original database and data retrieval procedure should be updated for collecting data from original server. We also assume that the user is used to using android system and know basics of android like pressing back button, understand navigation direction and where some of the system and app related things appear.

### 3.2 Constraints
This application is right now support only English and Spanish will be added later.

### 4. APS application
### 4.1 Starting application
After installing the apk to the system, the user will get to see the icon captioning B-Well-Mom. When the users tap the icon, the application will be started. As the application needs few moments to be installed or loaded data for the first activity, they will see a splash screen welcoming the user. This is now a very simple splash and waits for 5 seconds. The user can interrupt that if they don't want to see.



### Splash screen

<img src="https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/2.png" width="200">


### 4.2 First time settings
If the application starts for the first time, then this will directly moved to the settings page. The settings page contains PIN setup, security question setup, participation id, enrollment code and device data issue which are supposed to fill up first time application running. But PIN setup is mandatory. PIN length should be four and only digit. After completing settings, the user can tap complete button which will take her to the log in page.

<img src="https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/3.png" width="200" alt-text="Settings view"> |
<img src="https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/4.png" width="200" alt-text="Settings view">
### b)PIN entering dialog


### 4.3 Settings any time
After first setup, the user will get the access to diary and they can set all the diary related settings at their own will. They can do it while the app is first started or they can do it later some time. The setting options are listed as follows:

* a)Morning notification time
* b)Evening notification time
* c)Mobility participation ( They will get notified later though)
* d)Incentive threshold
* e)Device date issue
* f)Security questions

a)Device date issue settings
|
![settings view](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/5.png "Splash Screen") |
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/6.png "Splash Screen") 

b)Incentive settings


c)Evening notification time selection


![settings view](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/7.png "Splash Screen") |
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/8.png "Splash Screen") 
d)Morning notification time selection



e)Security question settings


![settings view](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/9.png "Splash Screen") |
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/10.png "Splash Screen") 
f)Security question answer setup



g)Mobility participation consent
|
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/11.png "Splash Screen")
Fig. Different available settings

### 4.4 Log in to diary
If the application is not starting for the first time, then some settings are already done, at least there is a PIN number of the user. If the user now opens the application, she will directly see PIN entering page for log in to the diary system. There is only one text input box with log in button. If the user forgets the PIN number, she can look at the top corner action bar. If tapped, the menu with " Forget PIN" will open. The user can tap the menu item, to navigate to the PIN recovering page.
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/12.png "Splash Screen")|
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/13.png "Splash Screen")| ![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/14.png "Splash Screen")



### 4.5 PIN Recovery activity
While the user set their preferences, they will get the chance to select their two security questions with the answers provide. If they forget the PIN and tap the Forget PIN from the PIN activity, they will be directed to PIN recovery page. The page contains two security questions answering field. The user has to provide the answers which will take them to the settings. While in settings, the user will be able to change the PIN.

![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/15.png "Splash Screen")

Fig. PIN Recovery

### 4.6 Diary activity
Diary activity is the main activity for daily diary information. The diary activity consists of 4 different types of data to be entered daily.

### 4.6.1 Peak flow
Peak flow information is collected in a separate activity but has only the link from the diary page. This will be displayed to the diary page, if current time is in the range of peak flow time window and if user has not filled 3 measurements yet. If peak flow information is entered properly, the diary page will not display the peak flow option button.

### 4.6.2 Symptoms
These are different symptoms the user faces at their daily life. These questions are mainly yes and no question selection. But two different types of information are needed here. One is the prescribed medication information and supplement information. These are also yes and no question. If user does not take any medication or supplement, there is nothing to do. But if they select yes, then the diary page will take them to the medication activity. We will discuss the medication activity later in this document.

|
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/16.png "Splash Screen")

|
| --- |

Fig. Main diary symptoms view

### 4.6.3 Daily activities
These are the information regarding the user daily life i.e daily exercise and its effects. These questions are mainly selecting different time duration and some dropdown easy questions.



Fig. Diary's daily activities view

Daily activities question has some different view in selection. Some questions are radio with selection of yes or no, some are selection list, the user can select from the list like how many times they smoked. Others questions are like selection of time duration. The default duration set here is 1 hour and the user can select any amount of hours and minutes from the pop up box.

|

![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/17.png "Splash Screen")
|![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/18.png "Splash Screen")
| Fig. Duration selection dialog |

### 4.6.4 Nitroxide measurement
This is not supposed to enter daily but will be defined by the doctors or surveyors. Usually, this will be asked every other day. The user don't need to worry about that, they will see the options if they need to fill up otherwise it will not be displayed. The user will get only one chance to fill up this field. If they fill up once, they will not see the option again for that day. But if user keep it blank it will be displayed if current time window is inside the nitroxide collection time range.

### 4.6.5 Saving diary information
The diary page information will be saved whatever the user entered each time they left the diary page. They will have the option to enter the information all they day if information is solely related to diary. Some information like peak flow and nitro have their own time window and the user has to be entered those within that time range. If the day passed, the diary information will be stored finally and if user login next day, they will see new diary with all blank. They also can complete the diary explicitly by tapping the submit button. If so, all diary information will be stored to the database and all diary related information will be out of display. But the user will get the chance to enter peak flow as this is separate and it depends on time window. If all information including peak flow are entered and submitted, the user will see thank you message. If they login that day again, they will see same message.


![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/19.png "Splash Screen")
Fig. Thank you message after completing diary

### 4.7 Peak flow activity
For entering peak flow measurement, the user can tap peak flow button from diary page which will navigate her to peak flow activity. Peak flow activity has three numeric measurement fields. The user has to fill up two peak flow a day, one is called here AM Peak Flow or morning peak and another one is PM peak flow or evening peak flow. Based on the time, one the mobile device, the peak flow button will guide them to morning or evening peak flow. The user supposed to enter one peak flow measurement to the first field and have to wait few seconds or minutes before entering second one. Tapping add button right to the field will display progress for certain amount of time and the next field will appear.

|
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/20.png "Splash Screen")

a)General peak flow view
|
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/21.png "Splash Screen")

b)Next peak flow waiting progress
|![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/22.png "Splash Screen")
| --- | --- |
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/23.png "Splash Screen")
Fig. Peak flow activity view

The user can come to peak flow activity anytime and can leave. They can field up one and can leave. Again when they come back they will see the previous entered field and can enter the next one. They will get the option until all three fields are field up. If they fill up all three fields, the peak flow button will be disappearing from the diary page and they will not get any chance to change or fill again.

### .8 Medication List activity
Medication list activity is for handling the user medication that she takes currently. At the first time user will see empty list. They can add their medication but tapping at the top right corner add action bar button. After adding medication, they will see a list of medication everyday they navigate to here. The list contains medication name, its dosae and frequency and each item has a checkbox at their left. The user will select the medication daily by click or tapping the checkbox only.

If they select only one from the list, they will see at action bar or status bar two option, edit or delete. If they want to delete the medication from the list, they can tap delete button which will ask them for reconfirmation. If users confirm, the medication will be removed from the list. If they want to edit the item, they can tap EDIT button, this will take user to the medication detail activity which will guide them to edit the item from the list.

If user previously select at least one item and come back again, they will see the item selected. If they want to add new item now, first user need to deselect the item/s from the list. Then the ADD button will appear.

|
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/24.png "Splash Screen")

a)List before selecting any item
|
| --- |
|

![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/25.png "Splash Screen")
b)After selecting only one item
|
|

![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/26.png "Splash Screen")
c)After seleting two or more items
|
![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/27.png "Splash Screen")
Fig. Medication list activity view

### 4.9 Medication detail activity
Medication detail activity is for adding new medication with detail information. Here details means information of dosage, name and frequency or how many times user takes these medications. This activity also provides the facility of editing the item detail information, if user navigates to here using edit button from list activity.

|

![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/28.png "Splash Screen")
a)Add medication view
|
| --- |
|

![First time PIN](https://github.com/mahbubcsedu/Antiphospholipid-Syndrom-Tracking/blob/master/docimages/29.png "Splash Screen")
b)Edit medication view
|

Fig. Detail medication/supplement view

While adding, the activity provides facility of auto completion both for medication name and dosage. When they enter first character, the medication list which are in the database, will appear which starts by the typed character. Right now, the character is set to one, later it could be changed if list is too big.

For dosage selection or enter, after medication name entered, if user try to enter dosage amount, they will see auto complete option which are relevant to the selected medication.

For both cases, the user can ignore selecting from auto complete list and can enter any medication name or dosage. There is no restriction on that.

While editing, the user will not get the chance to change the medication name. If so, then can delete the medication using list activity and can add as a new medication. But she will be able to edit dosage and frequency. If user wants to change dosage, she will be provided auto complete list of dosage which are relevant to that selected medication.

### 4.10 Supplement List activity
This is for handling supplement of the user. The functionality of supplement is same as medication list activity. Please see section 4.8

### 4.11 Supplement detail activity
This is to handle detail information add or edit of supplement. The functionality of detail supplement activity is similar to medication detail activity. Please see 4.9

### 4.12 Location recording service
Location recording service is for tracking the user location keeping safety measure in mind. This is optional for user and the system will asked for consent from the user. While the application starts, the system starts the location tracking alarm check which periodically search for location tracking duration rang from database which is set by the surveyor and for the user consent. If tracking time reach and user consent is not given, user will get a notification for their consent. They can go to settings and can agree to mobility service or user can ignore. If there are multiple time range for location tracking, for each range the system will ask for consent if not given. If user has given consent to track and if current time is within rang then location tracking service will start. The user will see a notification icon of tracking location. They will not be able to remove the icon. But if user wants, she can stop tracking anytime from the settings by revoking the consent.

Location tracking system uses GPS location and if GPS is not available then it will use other provider like wifi or passive provider. Location tracking server only store or track information if the location is change. The minimum distance and minimum time of location change to occur is set by the system.

### 4.13 Notification system
Notification is for let the user know about different information to enter and give consent for some information which are automatically detected. There are three types of notification in this app.

|


a) Reminder notification
|


b) Mobility consent notificatin |

Fig. Notification sample view

a) Reminder for daily diary - This notification will be given to user selected time, both in the morning and in the evening. The user can select the time when they want to see the reminder notification.

b) Location tracking consent - If location tracking consent is not set or given, and if new tracking rang takes place, user will be notified about the consent. They can see and can go to settings and change the settings.

c) Location tracking notification - User will get this notification while the location tracking is running. User will only see the icon and if they want can stop the tracking system.

### 4.14 Data sync service

The diary data will be collected for long time and local devices will not be able to keep all the data. The diary data will be sync with the server when the user will try to load the diary page. The diary data which are at least one day old or which the user select as complete will be selected and send to server. There could be network or other problem, the system will first check all the required parameter and if all met, and then it will start transfer data. If it can successfully transfer data, data status will be changed to complete to the local system and if not it will not changed. So, no data will be lost.

### 5 Conclusion
The system implementation is still not completed. Some tweaking needs to be done before deploying and distributing to user for data collection.
