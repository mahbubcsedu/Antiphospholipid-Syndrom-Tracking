/** 
 * <h3>APS application main package</h3> <br><br>
 * <p>The aps pakcage contains all the activities of the android application together with some other helping classes.
 * <br>
 * The main application starts from {@link APSApplication} file which also contains some provisions like when to show the 
 *which screen to the user. This file also contains the MokiManage mobility settings.
 *<br> {@link SplashScreen} is implemented here for keeping the user waiting displaying some eye catching animation or some important information
 *while the others things of the app are loading to the system.
 *<br> {@link MainActivity} is the entry point to the application. While the application is loading for the homepage, MainActivity decides about the login
 *and new entry {@see MainActivity#settingsExist}.
 *<br>
 
 * @since 1.0
 * @author Mahbubur Rahman
 * @version 1.0
 */


package com.emmes.aps;