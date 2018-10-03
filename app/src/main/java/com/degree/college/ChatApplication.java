package com.degree.college;

import android.app.Application;
import android.content.Context;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

/**
 * Created by USER on 15-03-2018.
 */

public class ChatApplication extends Application {
    Context context;
    String USER_ID="1";
    OpenChannel channel;

    @Override
    public void onCreate() {
        super.onCreate();
        SendBird.init("6F6EC549-6054-493B-8210-E4FECE07716C", context);

        SendBird.connect(USER_ID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
            }
        });

        OpenChannel.createChannel(new OpenChannel.OpenChannelCreateHandler() {
            @Override
            public void onResult(OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    // Error.
                    return;
                }
            }
        });
//        OpenChannel.getChannel(CHANNEL_URL, new OpenChannel.OpenChannelGetHandler() {
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
//            }
//        });
//        channel.sendUserMessage(MESSAGE, DATA, CUSTOM_TYPE, new BaseChannel.SendUserMessageHandler() {
//            @Override
//            public void onSent(UserMessage userMessage, SendBirdException e) {
//                if (e != null) {
//                    // Error.
//                    return;
//                }
//            }
//        });
    }
}
