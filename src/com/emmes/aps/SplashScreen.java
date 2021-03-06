package com.emmes.aps;

import com.emmes.aps.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreen extends Activity {
    
    /**
     * The thread to process splash screen events
     */
    private Thread mSplashThread;    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Splash screen view
        setContentView(R.layout.splash);
        
        final SplashScreen sPlashScreen = this;   
        final int splashWaitingtime=Integer.parseInt(this.getResources().getString(R.string.splash_screen_waiting_time_milsecs));
        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @SuppressWarnings("deprecation")
			@Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        wait(splashWaitingtime);
                    }
                }
                catch(InterruptedException ex){                    
                }

                finish();
                
                // Run next activity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, MainActivity.class);
                startActivity(intent);
               // stop(); 
                
            }
        };
        
        mSplashThread.start();        
    }
       
    
    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }    
} 