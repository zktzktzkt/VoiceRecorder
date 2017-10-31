/**
 * Copyright (C) 2017 Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ilike.voice.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ilike.voice.R;
import com.ilike.voice.utils.EaseUI;

import java.io.File;

/**
 * desc:   voice play click listener
 * author: wangshanhai
 * email: ilikeshatang@gmail.com
 * date: 2017/10/31 15:29
 */
public class EaseChatRowVoicePlayClickListener implements View.OnClickListener {
    private static final String TAG = "VoicePlayClickListener";

    ImageView voiceIconView;

    private AnimationDrawable voiceAnimation = null;
    MediaPlayer mediaPlayer = null;
    ImageView iv_read_status;
    Activity activity;
    private BaseAdapter adapter;

    public static boolean isPlaying = false;
    public static EaseChatRowVoicePlayClickListener currentPlayListener = null;
    public static String playMsgId = "";
    public static String getLocalUrl = "";

    public EaseChatRowVoicePlayClickListener(ImageView v, String micUrl, Activity context, BaseAdapter adapter) {
        this.getLocalUrl = micUrl;
        this.activity = context;
        voiceIconView = v;
        this.adapter = adapter;

    }

    public void stopPlayVoice() {
        voiceAnimation.stop();
        voiceIconView.setImageResource(R.drawable.ease_chatto_voice_playing);

        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
        playMsgId = null;
        adapter.notifyDataSetChanged();
    }

    public void playVoice(String filePath) {

        if (isPlaying) {
            if (playMsgId != null) {
                currentPlayListener.stopPlayVoice();
               // return;
            }else{
                currentPlayListener.stopPlayVoice();
            }

        }

        if (!(new File(filePath).exists())) {
            return;
        }

        playMsgId = filePath;
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = new MediaPlayer();
        if (EaseUI.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(); // stop animation
                }

            });
            isPlaying = true;
            currentPlayListener = this;
            mediaPlayer.start();
            showAnimation();


        } catch (Exception e) {
            System.out.println();
        }
    }

    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation
        voiceIconView.setImageResource(R.drawable.voice_to_icon);
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    @Override
    public void onClick(View v) {
        String st = activity.getResources().getString(R.string.Is_download_voice_click_later);
        if (isPlaying) {
            if (playMsgId != null) {
                currentPlayListener.stopPlayVoice();
                return;
            }
            currentPlayListener.stopPlayVoice();
        }


  /*      if (message.direct() == EMMessage.Direct.SEND) {
            // for sent msg, we will try to play the voice file directly
            playVoice(voiceBody.getLocalUrl());
        } else {*/

        File file = new File(getLocalUrl);
        if (file.exists() && file.isFile()) {
            //  playVoice(getLocalUrl);
        } else
            Log.e(TAG, "file not exist");

           /* } else if (message.status() == EMMessage.Status.INPROGRESS) {
                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
            } else if (message.status() == EMMessage.Status.FAIL) {
                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                     //   EMClient.getInstance().chatManager().downloadAttachment(message);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        adapter.notifyDataSetChanged();
                    }

                }.execute();
*/
    }

    //}
    //}

}