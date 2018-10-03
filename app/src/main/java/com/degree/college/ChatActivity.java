package com.degree.college;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.OpenChannel;

public class ChatActivity extends AppCompatActivity {
    String USER_ID = "1", CHANNEL_URL = "computer", MESSAGE = "Hello", DATA = null, CUSTOM_TYPE = "public";
    OpenChannel channel;
    BaseChannel.ChannelType channelType;
    Toolbar toolbar;


    ImageView imageback, imageconnect;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//       getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        toolbar = (Toolbar) findViewById(R.id.toolbar_group_channel);
        setSupportActionBar(toolbar);
        //  if (getSupportActionBar() != null) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        //getSupportActionBar().setTitle("Group Major");
        //  }

        title = (TextView) toolbar.findViewById(R.id.textcen);
        title.setText("Group Major");
        imageback = (ImageView) findViewById(R.id.id);
        //   imageback.setImageResource(R.drawable.ic_back);
//        imageback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        imageconnect = (ImageView) toolbar.findViewById(R.id.toolchat);
        // imageconnect.setImageResource(R.drawable.linearstyle);
        imageconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, GroupChat.class));
                // getSupportFragmentManager().beginTransaction().replace(R.id.chatactivity,new MemberFragment()).addToBackStack(null).commit();
            }
        });
        // getSupportFragmentManager().beginTransaction().replace(R.id.chatactivity,new ChatFragment()).commit();

        // SendBird.init("6F6EC549-6054-493B-8210-E4FECE07716C",getApplicationContext());
//        SendBird.connect(USER_ID, new SendBird.ConnectHandler() {
//            @Override
//            public void onConnected(User user, SendBirdException e) {
//                if (e != null) {
//                    // Error.
//                    Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
//
//                    return;
//                }
//                Toast.makeText(getApplicationContext(), (CharSequence) user,Toast.LENGTH_SHORT).show();
//        }
//        });
//
//        OpenChannel.createChannel(new OpenChannel.OpenChannelCreateHandler() {
//            @Override
//            public void onResult(OpenChannel openChannel, SendBirdException e) {
//                if (e != null) {
//                    // Error.
//                    return;
//                }
//                channel=openChannel;
//            }
//        });
//              OpenChannel.getChannel(CHANNEL_URL, new OpenChannel.OpenChannelGetHandler() {
//            @Override
//            public void onResult(OpenChannel openChannel, SendBirdException e) {
//                if (e != null) {
//                    // Error.
//                    return;
//                }
//
//                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
//                    @Override
//                    public void onResult(SendBirdException e) {
//                        if (e != null) {
//                            // Error.
//                            return;
//                        }
//                    }
//                });
//                channel=openChannel;
//            }
//        });
//        channel.sendUserMessage(MESSAGE, DATA, CUSTOM_TYPE, new BaseChannel.SendUserMessageHandler() {
//            @Override
//            public void onSent(UserMessage userMessage, SendBirdException e) {
//                if (e != null) {
//                    // Error.
//                    return;
//                }
//                Toast.makeText(getApplicationContext(),MESSAGE,Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
