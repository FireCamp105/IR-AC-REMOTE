package com.fire.boulclim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {


    private int WV = 0 ;
    private int XV = 0 ;
    private int YV = 0 ;
    private int ZV = 0 ;

    private int WM,XM,YM,ZM;
    private boolean OFFCH=false,DRYCH=false,POCH=false,FANCH=false;
    private String SENDEE="";
    private String SPEED,MODETEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//subscribing to messaging topic: news
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        String token = task.getResult();

                        boolean subscribed = getSharedPreferences("fcm", MODE_PRIVATE)
                                .getBoolean("subscribed", false);

                        if (!subscribed) {
                            FirebaseMessaging.getInstance().subscribeToTopic("news");
                            getSharedPreferences("fcm", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("subscribed", true)
                                    .apply();
                        }
                    }
                });





//nothing to see here

        Button TempMax = (Button) findViewById(R.id.TEMPUP);
        Button TempMin = (Button) findViewById(R.id.TEMPDOWN);
        TextView TEMP = (TextView) findViewById(R.id.TEMP);
        TextView ROOMT = (TextView) findViewById(R.id.roomtemp);
        TextView SETTING = (TextView) findViewById(R.id.SETTING);
        Button OFF = (Button) findViewById(R.id.OFF);
        Button ON = (Button) findViewById(R.id.ON);
        Button MODE = (Button) findViewById(R.id.MODE);
        Button FAN = (Button) findViewById(R.id.FAN);
        Button DRY = (Button) findViewById(R.id.DRY);
        Button FANMODE = (Button) findViewById(R.id.FANMODE);
        Button PO = (Button) findViewById(R.id.PO);
        Button LAUNCH = (Button) findViewById(R.id.SEND);
        FirebaseDatabase database = FirebaseDatabase.getInstance("CHANGE ME"); // put your personal Firebase RTDB URL here
        DatabaseReference ROOMTEMP = database.getReference("ROOMTEMP");
        //
        DatabaseReference LAUNCHED = database.getReference("LAUNCH");
        //off  on word el bit eli dima nassinou
        DatabaseReference W = database.getReference("W");
        //mode word 4 bit (off:on): (0:8), (3:B) (4;C)
        DatabaseReference X = database.getReference("X");
        //temp word Y men 3 (18°C) -> F (30°C)
        DatabaseReference Y = database.getReference("Y");
        //fan speed 0 quiet, 2 medium, 4 max, 5 auto
        DatabaseReference Z = database.getReference("Z");

        DatabaseReference rootRef = database.getReference();
        //showing code
        TextView code = (TextView) findViewById(R.id.Code);
        // TODO:  AFFICHE DES PARAMETRES ACTUELS
        if(OFFCH){
            SETTING.setText("OFF");
        } else
            if (POCH) {
            SETTING.setText("Jet Cool Mode");

        } else if (DRYCH) {
            SETTING.setText("DRY MODE");
        } else if (FANCH) {
            switch (ZV) {
                case 0:
                    SPEED= "QUIET";
                    break;
                case 2:
                    SPEED= "MEDIUM";
                    break;
                case 4:
                    SPEED= "MAXIMUM";
                    break;
                case 5:
                    SPEED= "AUTO";
                    break;
            }
            SETTING.setText("FAN MODE:\nSPEED:" + SPEED);

        } else {
            switch (ZV) {
                case 0:
                    SPEED = "QUIET";
                    break;
                case 2:
                    SPEED = "MEDIUM";
                    break;
                case 4:
                    SPEED = "MAXIMUM";
                    break;
                case 5:
                    SPEED = "AUTO";
                    break;

            }
            switch (XV) {
                case 0:
                case 8:
                    MODETEXT = "COOL";
                    break;
                case 3:
                case 11:
                    MODETEXT = "AUTO";
                    break;
                case 4:
                case 12:
                    MODETEXT = "HEAT";
                    break;

            }
            SETTING.setText(MODETEXT +"\n"+ (YV+15) +"°C \nSPEED:" + SPEED);
        }

        //TODO: GREEN BS FOR ATTENTION SEEKING

        // intialisation of UI
        W.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WV = snapshot.getValue(Integer.class);
                code.setText(String.format("Code is: %d,%d,%d,%d",WV,XV,YV,ZV));
                if(OFFCH){
                    SETTING.setText("OFF");
                } else
                if (POCH) {
                    SETTING.setText("Jet Cool Mode");

                } else if (DRYCH) {
                    SETTING.setText("DRY MODE");
                } else if (FANCH) {
                    switch (ZV) {
                        case 0:
                            SPEED= "QUIET";
                            break;
                        case 2:
                            SPEED= "MEDIUM";
                            break;
                        case 4:
                            SPEED= "MAXIMUM";
                            break;
                        case 5:
                            SPEED= "AUTO";
                            break;
                    }
                    SETTING.setText("FAN MODE:\nSPEED:" + SPEED);

                } else {
                    switch (ZV) {
                        case 0:
                            SPEED = "QUIET";
                            break;
                        case 2:
                            SPEED = "MEDIUM";
                            break;
                        case 4:
                            SPEED = "MAXIMUM";
                            break;
                        case 5:
                            SPEED = "AUTO";
                            break;

                    }
                    switch (XV) {
                        case 0:
                        case 8:
                            MODETEXT = "COOL";
                            break;
                        case 3:
                        case 11:
                            MODETEXT = "AUTO";
                            break;
                        case 4:
                        case 12:
                            MODETEXT = "HEAT";
                            break;

                    }
                    SETTING.setText(MODETEXT +"\n"+ (YV+15) +"°C \nSPEED:" + SPEED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        X.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                XV = snapshot.getValue(Integer.class);
                code.setText(String.format("Code is: %d,%d,%d,%d",WV,XV,YV,ZV));
                switch (XV){
                    case 8 :
                    case 0:
                        MODE.setText("COOL");
                        break;
                    case 11:
                    case 3 :
                        MODE.setText("AUTO");
                        break;
                    case 12:
                    case 4:
                        MODE.setText("HEAT");
                        break;
                }
                if(OFFCH){
                    SETTING.setText("OFF");
                } else
                if (POCH) {
                    SETTING.setText("Jet Cool Mode");

                } else if (DRYCH) {
                    SETTING.setText("DRY MODE");
                } else if (FANCH) {
                    switch (ZV) {
                        case 0:
                            SPEED= "QUIET";
                            break;
                        case 2:
                            SPEED= "MEDIUM";
                            break;
                        case 4:
                            SPEED= "MAXIMUM";
                            break;
                        case 5:
                            SPEED= "AUTO";
                            break;
                    }
                    SETTING.setText("FAN MODE:\nSPEED:" + SPEED);

                } else {
                    switch (ZV) {
                        case 0:
                            SPEED = "QUIET";
                            break;
                        case 2:
                            SPEED = "MEDIUM";
                            break;
                        case 4:
                            SPEED = "MAXIMUM";
                            break;
                        case 5:
                            SPEED = "AUTO";
                            break;

                    }
                    switch (XV) {
                        case 0:
                        case 8:
                            MODETEXT = "COOL";
                            break;
                        case 3:
                        case 11:
                            MODETEXT = "AUTO";
                            break;
                        case 4:
                        case 12:
                            MODETEXT = "HEAT";
                            break;

                    }
                    SETTING.setText(MODETEXT +"\n"+ (YV+15) +"°C \nSPEED:" + SPEED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        Y.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                YV = snapshot.getValue(Integer.class);
                TEMP.setText((YV+15 )+ "°C");
                code.setText(String.format("Code is: %d,%d,%d,%d",WV,XV,YV,ZV));
                if(OFFCH){
                    SETTING.setText("OFF");
                } else
                if (POCH) {
                    SETTING.setText("Jet Cool Mode");

                } else if (DRYCH) {
                    SETTING.setText("DRY MODE");
                } else if (FANCH) {
                    switch (ZV) {
                        case 0:
                            SPEED= "QUIET";
                            break;
                        case 2:
                            SPEED= "MEDIUM";
                            break;
                        case 4:
                            SPEED= "MAXIMUM";
                            break;
                        case 5:
                            SPEED= "AUTO";
                            break;
                    }
                    SETTING.setText("FAN MODE:\nSPEED:" + SPEED);

                } else {
                    switch (ZV) {
                        case 0:
                            SPEED = "QUIET";
                            break;
                        case 2:
                            SPEED = "MEDIUM";
                            break;
                        case 4:
                            SPEED = "MAXIMUM";
                            break;
                        case 5:
                            SPEED = "AUTO";
                            break;

                    }
                    switch (XV) {
                        case 0:
                        case 8:
                            MODETEXT = "COOL";
                            break;
                        case 3:
                        case 11:
                            MODETEXT = "AUTO";
                            break;
                        case 4:
                        case 12:
                            MODETEXT = "HEAT";
                            break;

                    }
                    SETTING.setText(MODETEXT +"\n"+ (YV+15) +"°C \nSPEED:" + SPEED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        Z.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ZV = snapshot.getValue(Integer.class);
                code.setText(String.format("Code is: %d,%d,%d,%d",WV,XV,YV,ZV));
                switch (ZV) {
                    case 0:
                        FAN.setText("QUIET");
                        break;
                    case 2:
                        FAN.setText("MEDIUM");
                        break;
                    case 4:
                        FAN.setText("MAXIMUM");
                        break;
                    case 5:
                        FAN.setText("AUTO");
                        break;
                }
                if(OFFCH){
                    SETTING.setText("OFF");
                } else
                    if (POCH) {
                        SETTING.setText("Jet Cool Mode");

                    } else if (DRYCH) {
                        SETTING.setText("DRY MODE");
                    } else if (FANCH) {
                        switch (ZV) {
                            case 0:
                                SPEED= "QUIET";
                                break;
                            case 2:
                                SPEED= "MEDIUM";
                                break;
                            case 4:
                                SPEED= "MAXIMUM";
                                break;
                            case 5:
                                SPEED= "AUTO";
                                break;
                        }
                        SETTING.setText("FAN MODE:\nSPEED:" + SPEED);

                    } else {
                        switch (ZV) {
                            case 0:
                                SPEED = "QUIET";
                                break;
                            case 2:
                                SPEED = "MEDIUM";
                                break;
                            case 4:
                                SPEED = "MAXIMUM";
                                break;
                            case 5:
                                SPEED = "AUTO";
                                break;

                        }
                        switch (XV) {
                            case 0:
                            case 8:
                                MODETEXT = "COOL";
                                break;
                            case 3:
                            case 11:
                                MODETEXT = "AUTO";
                                break;
                            case 4:
                            case 12:
                                MODETEXT = "HEAT";
                                break;

                        }
                        SETTING.setText(MODETEXT +"\n"+ (YV+15) +"°C \nSPEED:" + SPEED);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        rootRef.child("Wmem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WM = snapshot.getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        rootRef.child("Xmem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                XM = snapshot.getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        rootRef.child("Ymem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                YM = snapshot.getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        rootRef.child("Zmem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ZM = snapshot.getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        rootRef.child("OFFCH").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                OFFCH = snapshot.getValue(boolean.class);

                if(OFFCH){
                    SETTING.setText("OFF");
                } else
                if (POCH) {
                    SETTING.setText("Jet Cool Mode");

                } else if (DRYCH) {
                    SETTING.setText("DRY MODE");
                } else if (FANCH) {
                    switch (ZV) {
                        case 0:
                            SPEED= "QUIET";
                            break;
                        case 2:
                            SPEED= "MEDIUM";
                            break;
                        case 4:
                            SPEED= "MAXIMUM";
                            break;
                        case 5:
                            SPEED= "AUTO";
                            break;
                    }
                    SETTING.setText("FAN MODE:\nSPEED:" + SPEED);

                } else {
                    switch (ZV) {
                        case 0:
                            SPEED = "QUIET";
                            break;
                        case 2:
                            SPEED = "MEDIUM";
                            break;
                        case 4:
                            SPEED = "MAXIMUM";
                            break;
                        case 5:
                            SPEED = "AUTO";
                            break;

                    }
                    switch (XV) {
                        case 0:
                        case 8:
                            MODETEXT = "COOL";
                            break;
                        case 3:
                        case 11:
                            MODETEXT = "AUTO";
                            break;
                        case 4:
                        case 12:
                            MODETEXT = "HEAT";
                            break;

                    }
                    SETTING.setText(MODETEXT +"\n"+ (YV+15) +"°C \nSPEED:" + SPEED);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        rootRef.child("DRYCH").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DRYCH = snapshot.getValue(boolean.class);
                if(OFFCH){
                    SETTING.setText("OFF");
                } else
                if (POCH) {
                    SETTING.setText("Jet Cool Mode");

                } else if (DRYCH) {
                    SETTING.setText("DRY MODE");
                } else if (FANCH) {
                    switch (ZV) {
                        case 0:
                            SPEED= "QUIET";
                            break;
                        case 2:
                            SPEED= "MEDIUM";
                            break;
                        case 4:
                            SPEED= "MAXIMUM";
                            break;
                        case 5:
                            SPEED= "AUTO";
                            break;
                    }
                    SETTING.setText("FAN MODE:\nSPEED:" + SPEED);

                } else {
                    switch (ZV) {
                        case 0:
                            SPEED = "QUIET";
                            break;
                        case 2:
                            SPEED = "MEDIUM";
                            break;
                        case 4:
                            SPEED = "MAXIMUM";
                            break;
                        case 5:
                            SPEED = "AUTO";
                            break;

                    }
                    switch (XV) {
                        case 0:
                        case 8:
                            MODETEXT = "COOL";
                            break;
                        case 3:
                        case 11:
                            MODETEXT = "AUTO";
                            break;
                        case 4:
                        case 12:
                            MODETEXT = "HEAT";
                            break;

                    }
                    SETTING.setText(MODETEXT +"\n"+ (YV+15) +"°C \nSPEED:" + SPEED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        rootRef.child("POCH").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                POCH = snapshot.getValue(boolean.class);
                if(OFFCH){
                    SETTING.setText("OFF");
                } else
                if (POCH) {
                    SETTING.setText("Jet Cool Mode");

                } else if (DRYCH) {
                    SETTING.setText("DRY MODE");
                } else if (FANCH) {
                    switch (ZV) {
                        case 0:
                            SPEED= "QUIET";
                            break;
                        case 2:
                            SPEED= "MEDIUM";
                            break;
                        case 4:
                            SPEED= "MAXIMUM";
                            break;
                        case 5:
                            SPEED= "AUTO";
                            break;
                    }
                    SETTING.setText("FAN MODE:\nSPEED:" + SPEED);

                } else {
                    switch (ZV) {
                        case 0:
                            SPEED = "QUIET";
                            break;
                        case 2:
                            SPEED = "MEDIUM";
                            break;
                        case 4:
                            SPEED = "MAXIMUM";
                            break;
                        case 5:
                            SPEED = "AUTO";
                            break;

                    }
                    switch (XV) {
                        case 0:
                        case 8:
                            MODETEXT = "COOL";
                            break;
                        case 3:
                        case 11:
                            MODETEXT = "AUTO";
                            break;
                        case 4:
                        case 12:
                            MODETEXT = "HEAT";
                            break;

                    }
                    SETTING.setText(MODETEXT +"\n"+ (YV+15) +"°C \nSPEED:" + SPEED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        rootRef.child("FANCH").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FANCH = snapshot.getValue(boolean.class);
                if(OFFCH){
                    SETTING.setText("OFF");
                } else
                if (POCH) {
                    SETTING.setText("Jet Cool Mode");

                } else if (DRYCH) {
                    SETTING.setText("DRY MODE");
                } else if (FANCH) {
                    switch (ZV) {
                        case 0:
                            SPEED= "QUIET";
                            break;
                        case 2:
                            SPEED= "MEDIUM";
                            break;
                        case 4:
                            SPEED= "MAXIMUM";
                            break;
                        case 5:
                            SPEED= "AUTO";
                            break;
                    }
                    SETTING.setText("FAN MODE:\nSPEED:" + SPEED);

                } else {
                    switch (ZV) {
                        case 0:
                            SPEED = "QUIET";
                            break;
                        case 2:
                            SPEED = "MEDIUM";
                            break;
                        case 4:
                            SPEED = "MAXIMUM";
                            break;
                        case 5:
                            SPEED = "AUTO";
                            break;

                    }
                    switch (XV) {
                        case 0:
                        case 8:
                            MODETEXT = "COOL";
                            break;
                        case 3:
                        case 11:
                            MODETEXT = "AUTO";
                            break;
                        case 4:
                        case 12:
                            MODETEXT = "HEAT";
                            break;

                    }
                    SETTING.setText(MODETEXT +"\n"+ (YV+15) +"°C \nSPEED:" + SPEED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        ROOMTEMP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                ROOMT.setText(String.format("Room Temperature : %d°C", snapshot.getValue(Integer.class)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        //mode button text on startup



        // + - temp buttons
        // TODO: add check for DRYCH OFFCH FANCH POCH
        TempMax.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!OFFCH && !POCH && !DRYCH && !FANCH){
                    if (YV<15) {
                        Y.setValue(YV + 1);
                        LAUNCHED.setValue("SEND");
                    }
                    else
                    {
                        Y.setValue(15);
                        LAUNCHED.setValue("SEND");
                    }
                }
            }
        });
        TempMin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {if(!OFFCH && !POCH && !DRYCH && !FANCH){
                if (YV>3) {
                    Y.setValue(YV - 1);
                    LAUNCHED.setValue("SEND");
                }
                else
                {
                    Y.setValue(3);
                    LAUNCHED.setValue("SEND");
                }

            }}
        });
        // OFF button
        OFF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!OFFCH){
                rootRef.child("Wmem").setValue(WV);
                switch (XV){
                    case 0:
                    case 8:
                        rootRef.child("Xmem").setValue(8);
                        break;
                        case 3:
                        case 11:
                            rootRef.child("Xmem").setValue(11);
                            break;
                            case 4:
                            case 12:
                                rootRef.child("Xmem").setValue(12);
                                break;
                                    }

                rootRef.child("Ymem").setValue(YV);
                rootRef.child("Zmem").setValue(ZV);
                rootRef.child("OFFCH").setValue(true);
                rootRef.child("DRYCH").setValue(false);
                rootRef.child("FANCH").setValue(false);
                rootRef.child("POCH").setValue(false);

            }
                rootRef.child("W").setValue(12);
                rootRef.child("X").setValue(0);
                rootRef.child("Y").setValue(0);
                rootRef.child("Z").setValue(5);
                rootRef.child("LAUNCH").setValue("SEND");

            }
        });
        //ON button
        ON.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (OFFCH){

                    rootRef.child("W").setValue(0);
                    switch (XM){
                        case 8:
                            rootRef.child("X").setValue(0);
                            break;
                            case 11:
                                rootRef.child("X").setValue(3);
                                break;
                                case 12:
                                    rootRef.child("X").setValue(4);
                                    break;
                    }

                    rootRef.child("Y").setValue(YM);
                    rootRef.child("Z").setValue(ZM);
                    rootRef.child("OFFCH").setValue(false);



                }
                rootRef.child("LAUNCH").setValue("SEND");

            }
        });
        //cool/hot button
        MODE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!OFFCH){
                    if (!POCH && !DRYCH && !FANCH){

                        switch (XV){
                            case 8 :
                            case 0:
                                rootRef.child("X").setValue(11);
                                break;

                            case 11:
                            case 3 :
                                rootRef.child("X").setValue(12);
                                break;
                            case 12:
                            case 4:
                                rootRef.child("X").setValue(8);
                                break;
                        }



                    } else{
                        rootRef.child("X").setValue(XM);
                        rootRef.child("Y").setValue(YM);
                        rootRef.child("Z").setValue(ZM);

                        rootRef.child("FANCH").setValue(false);
                        rootRef.child("POCH").setValue(false);
                        rootRef.child("DRYCH").setValue(false);


                    }

                rootRef.child("LAUNCH").setValue("SEND");

                }



                // TODO: MODE.setText("HOT"); and DRY and PO AND COLD AND AUTO AND FAN


            }

        });
        //fan speed
        FAN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!OFFCH){
                    if (!POCH && !DRYCH ){

                        switch (ZV){
                            case 0:
                                rootRef.child("Z").setValue(2);
                                break;

                            case 2 :
                                rootRef.child("Z").setValue(4);
                                break;

                            case 4:
                                rootRef.child("Z").setValue(5);
                                break;

                            case 5:
                            case 8:
                                rootRef.child("Z").setValue(0);
                                break;
                        }



                    }

                    rootRef.child("LAUNCH").setValue("SEND");

                }



                // TODO: MODE.setText("HOT"); and DRY and PO AND COLD AND AUTO AND FAN


            }

        });
        //DRY mode
        DRY.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!OFFCH && !DRYCH) {
                    if (!FANCH && !POCH) {


                        rootRef.child("Wmem").setValue(WV);
                        rootRef.child("Xmem").setValue(XV);
                        rootRef.child("Ymem").setValue(YV);
                        rootRef.child("Zmem").setValue(ZV);
                    }
                    rootRef.child("DRYCH").setValue(true);
                    rootRef.child("POCH").setValue(false);
                    rootRef.child("FANCH").setValue(false);


                    rootRef.child("W").setValue(0);
                    rootRef.child("X").setValue(9);
                    rootRef.child("Y").setValue(8);
                    rootRef.child("Z").setValue(0);

                }
                rootRef.child("LAUNCH").setValue("SEND");
            }
        });
        //FAN MODE
        FANMODE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!OFFCH && !FANCH) {
                    if(!DRYCH && !POCH){

                    rootRef.child("Wmem").setValue(WV);
                    rootRef.child("Xmem").setValue(XV);
                    rootRef.child("Ymem").setValue(YV);
                    rootRef.child("Zmem").setValue(ZV);
                    rootRef.child("DRYCH").setValue(true);
                }
                    if (DRYCH || POCH){
                        rootRef.child("Z").setValue(ZM);
                    }
                rootRef.child("FANCH").setValue(true);
                rootRef.child("POCH").setValue(false);
                rootRef.child("DRYCH").setValue(false);



                    rootRef.child("W").setValue(0);
                    rootRef.child("X").setValue(10);
                    rootRef.child("Y").setValue(3);

                }
                rootRef.child("LAUNCH").setValue("SEND");
            }
        });
        //PO Jet Cool mode
        PO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!OFFCH && !POCH) {
                    if(!DRYCH && !FANCH){

                        rootRef.child("Wmem").setValue(WV);
                        rootRef.child("Xmem").setValue(XV);
                        rootRef.child("Ymem").setValue(YV);
                        rootRef.child("Zmem").setValue(ZV);
                        rootRef.child("POCH").setValue(true);
                    }
                    if (DRYCH || POCH){

                    }
                    rootRef.child("POCH").setValue(true);
                    rootRef.child("DRYCH").setValue(false);
                    rootRef.child("FANCH").setValue(false);



                    rootRef.child("W").setValue(1);
                    rootRef.child("X").setValue(0);
                    rootRef.child("Y").setValue(0);
                    rootRef.child("Z").setValue(8);

                }
                rootRef.child("LAUNCH").setValue("SEND");
            }
        });

        //SEND button
        LAUNCH.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LAUNCHED.setValue("SEND");
                LAUNCHED.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SENDEE = snapshot.getValue(String.class);
                        if (SENDEE == "SEND") {
                            LAUNCH.setText("LOADING");
                        }else
                        {
                            LAUNCH.setText("REFRESH");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

}

//TODO: TANSECH SEND FI BOUTTONET LKOL BECH YREFRESHI SIGNAL