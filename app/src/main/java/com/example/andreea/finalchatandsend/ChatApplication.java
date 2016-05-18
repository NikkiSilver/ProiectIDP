/*
 * Copyright AllSeen Alliance. All rights reserved.
 *
 *    Permission to use, copy, modify, and/or distribute this software for any
 *    purpose with or without fee is hereby granted, provided that the above
 *    copyright notice and this permission notice appear in all copies.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 *    WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 *    MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 *    ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 *    WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 *    ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 *    OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.example.andreea.finalchatandsend;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ChatApplication extends Application implements ForWatcher {
    private static final String TAG = "chat.ChatApplication";
    public static String PACKAGE_NAME;

    ComponentName service = null;
    public void onCreate() {
        PACKAGE_NAME = getApplicationContext().getPackageName();
        Intent intent = new Intent(this, AllJoynService.class);
        service = startService(intent);

    }



    public void checkin() {

        if (service == null) {
            Intent intent = new Intent(this, AllJoynService.class);
            service = startService(intent);

        }
    }

    public enum HostUseStates{
        HOST_CHANNEL_STATE_CHANGED_EVENT,
        USE_CHANNEL_STATE_CHANGED_EVENT,
        USE_JOIN_CHANNEL_EVENT,
        USE_LEAVE_CHANNEL_EVENT,
        HOST_INIT_CHANNEL_EVENT,
        HOST_START_CHANNEL_EVENT,
        HOST_STOP_CHANNEL_EVENT,
        OUTBOUND_CHANGED_EVENET,
        HISTORY_CHANGED_EVENT,
        QUIT_EVENT;

        public String toString(){

            switch (this)
            {
                case HOST_CHANNEL_STATE_CHANGED_EVENT:
                    return "HOST_CHANNEL_STATE_CHANGED_EVENT";
                case USE_CHANNEL_STATE_CHANGED_EVENT:
                    return "USE_CHANNEL_STATE_CHANGED_EVENT";
                case USE_JOIN_CHANNEL_EVENT:
                    return "USE_JOIN_CHANNEL_EVENT";
                case USE_LEAVE_CHANNEL_EVENT:
                    return "USE_LEAVE_CHANNEL_EVENT";
                case HOST_INIT_CHANNEL_EVENT:
                    return "HOST_INIT_CHANNEL_EVENT";
                case HOST_START_CHANNEL_EVENT:
                    return "HOST_START_CHANNEL_EVENT";
                case HOST_STOP_CHANNEL_EVENT:
                    return "HOST_STOP_CHANNEL_EVENT";
                case OUTBOUND_CHANGED_EVENET:
                    return "OUTBOUND_CHANGED_EVENET";
                case HISTORY_CHANGED_EVENT:
                    return "HISTORY_CHANGED_EVENT";
                case QUIT_EVENT:
                    return "QUIT_EVENT";
            }
            return null;
        }

    }

    HostUseStates state;

    public synchronized void newLocalUserMessage(String message) {
        addInboundItem("Me", message);
        addOutboundItem(message);
    }


    public synchronized void newRemoteUserMessage(String nickname, String message) {
        addInboundItem(nickname, message);
    }


    private List<String> receive_messages = new ArrayList<String>();


    private void addOutboundItem(String message) {
        receive_messages.add("Hey");
        receive_messages.add(message);
        notifyObservers(state.OUTBOUND_CHANGED_EVENET.toString());
    }


    public synchronized String getOutboundItem() {
        if(receive_messages.isEmpty())
            return null;
        return receive_messages.remove(0);
    }




    private void addInboundItem(String nickname, String message) {
        addHistoryItem(nickname, message);
    }


    private List<String> chat_history = new ArrayList<String>();


    private void addHistoryItem(String nickname, String message) {
        chat_history.add(nickname + ":" + message);
        notifyObservers(state.HISTORY_CHANGED_EVENT.toString());
    }


    private void clearHistory() {
        chat_history.clear();
        notifyObservers(state.HISTORY_CHANGED_EVENT.toString());
    }


    public synchronized List<String> getHistory() {
        int size = chat_history.size();
        List<String> get_messages = new ArrayList<>(size);
        for (String s : chat_history)
            get_messages.add(new String(s));
        return get_messages;
    }


    private List<Watcher> myWatchers = new ArrayList<Watcher>();

    public synchronized void addWatcher(Watcher obs) {
        Log.i(TAG, "addObserver(" + obs + ")");
        if (myWatchers.indexOf(obs) < 0) {
            myWatchers.add(obs);
        }
    }


    public synchronized void deleteWatcher(Watcher obs) {
        Log.i(TAG, "deleteObserver(" + obs + ")");
        myWatchers.remove(obs);
    }


    private void notifyObservers(Object arg) {
        for (Watcher obs : myWatchers) {
            obs.update(this, arg);
        }
    }



    private List<String> my_channels = new ArrayList<String>();

    public synchronized void addFoundChannel(String channel) {

        my_channels.add(channel);

    }


    public synchronized void removeFoundChannel(String channel) {
        Iterator<String> i;
        for (i = my_channels.iterator(); i.hasNext(); ) {
            String my_string = i.next();
            if (my_string.equals(channel)) {
                i.remove();
            }

        }
    }


    public synchronized List<String> getFoundChannels() {
        int size = my_channels.size();
        List<String> s = new ArrayList<String>(size);
        for (String str : my_channels)
            s.add(new String(str));
        return s;
    }

    public AllJoynService.BusAttachmentState mBusAttachmentState = AllJoynService.BusAttachmentState.DISCONNECTED;



    private AllJoynService.HostChannelState mychannel_status = AllJoynService.HostChannelState.IDLE;
    public synchronized void hostSetChannelState(AllJoynService.HostChannelState status) {
        mychannel_status = status;
        notifyObservers(state.HOST_CHANNEL_STATE_CHANGED_EVENT.toString());
    }


    public synchronized AllJoynService.HostChannelState hostGetChannelState() {
        return mychannel_status;
    }


    private String my_channel_name =null;
    public synchronized void hostSetChannelName(String name) {
        my_channel_name = name;
        notifyObservers(state.HOST_CHANNEL_STATE_CHANGED_EVENT.toString());
    }

    public synchronized String hostGetChannelName() {
        return my_channel_name;
    }




    private AllJoynService.UseChannelState other_channel_status = AllJoynService.UseChannelState.IDLE;
    public synchronized void useSetChannelState(AllJoynService.UseChannelState status) {
        other_channel_status = status;
        notifyObservers(state.USE_CHANNEL_STATE_CHANGED_EVENT.toString());
    }

    public synchronized AllJoynService.UseChannelState useGetChannelState() {
        return other_channel_status;
    }



    private String other_channel_names = null;
    public synchronized void useSetChannelName(String name) {
        other_channel_names = name;
        notifyObservers(state.USE_CHANNEL_STATE_CHANGED_EVENT.toString());
    }

    public synchronized String useGetChannelName() {
        return other_channel_names;
    }



    public synchronized void useJoinChannel() {
        clearHistory();
        notifyObservers(state.USE_CHANNEL_STATE_CHANGED_EVENT.toString());
        notifyObservers(state.USE_JOIN_CHANNEL_EVENT.toString());
    }


    public synchronized void useLeaveChannel() {
        notifyObservers(state.USE_CHANNEL_STATE_CHANGED_EVENT.toString());
        notifyObservers(state.USE_LEAVE_CHANNEL_EVENT.toString());
    }


    public synchronized void hostInitChannel() {
        notifyObservers(state.HOST_CHANNEL_STATE_CHANGED_EVENT.toString());
        notifyObservers(state.HOST_INIT_CHANNEL_EVENT.toString());
    }


    public synchronized void hostStartChannel() {
        notifyObservers(state.HOST_CHANNEL_STATE_CHANGED_EVENT.toString());
        notifyObservers(state.HOST_START_CHANNEL_EVENT.toString());
    }

    public synchronized void hostStopChannel() {
        notifyObservers(state.HOST_CHANNEL_STATE_CHANGED_EVENT.toString());
        notifyObservers(state.HOST_STOP_CHANNEL_EVENT.toString());
    }





}
