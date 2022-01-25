package com.stopgroup.stopcar.captain.activity;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dewarder.holdinglibrary.HoldingButtonLayout;
import com.dewarder.holdinglibrary.HoldingButtonLayoutListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stopgroup.stopcar.captain.activity.chat_libs.AudioRecorder;
import com.stopgroup.stopcar.captain.activity.chat_libs.FilesUploader;
import com.stopgroup.stopcar.captain.activity.chat_libs.Message;
import com.stopgroup.stopcar.captain.activity.chat_libs.PermissionsActivity;
import com.stopgroup.stopcar.captain.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.stopgroup.stopcar.captain.activity.chat_libs.Libs.getCurrentTime;
public abstract class ChatActivity extends PermissionsActivity {


    public void sendImage(View view) {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            new FilesUploader(this, data.getData(), "images", ".jpg", new FilesUploader.OnImageUploadListener() {
                @Override
                public void OnImageUploaded(String file) {
                    sendMessage("image", file);
                    Toast.makeText(getApplicationContext(), "تم الارسال بنجاح", Toast.LENGTH_LONG).show();
                }
                @Override
                public void OnError() {
                    Toast.makeText(getApplicationContext(), "عذرا حدث خطأ ما", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private RecyclerView mChatRecycler;
    String myUid, myImage, hisImage, hisUid, myName, hisName;
    EditText input;
    ImageView mSend;
    public void sendMessage(String kind, String content) {
        String time = getCurrentTime();
        String seen = "UNSEEN";
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("kind", kind);
        data.put("content", content);
        data.put("seen", seen);

        data.put("time", time);

        HashMap<String, String> me = (HashMap) data.clone();
        me.put("who", "me");
        HashMap<String, String> him = (HashMap) data.clone();
        him.put("who", "him");
        String msg_id = FirebaseDatabase.getInstance().getReference().push().getKey();
        FirebaseDatabase.getInstance().getReference().child("Chats").child(hisUid).child(myUid).child(msg_id).setValue(him);
        FirebaseDatabase.getInstance().getReference().child("Chats").child(myUid).child(hisUid).child(msg_id).setValue(me);
    }
    AudioRecorder audioRecorder;
    boolean isViceExpand = false;
    boolean chatStarted = false;
    public void startChat(String myUid , String myName,String myImage,String hisUid , String hisName,String hisImage ) {
        if (chatStarted) {
            return;
        }
        chatStarted = true;
        this.myUid =myUid;
        this.myName =myName;
        this.myImage =myImage;
        this.hisUid =hisUid;
        this.hisName =  hisName;
        this.hisImage =  hisName;
        HoldingButtonLayout voice_message = findViewById(R.id.voice_message);
        voice_message.addListener(new HoldingButtonLayoutListener() {
            @Override
            public void onBeforeExpand() {
                isViceExpand = true;
                requestAppPermissions(new String[]{Manifest.permission.RECORD_AUDIO,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, new OnPermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted() {
                        File tempDir = ContextCompat.getDataDir(getApplicationContext());
                        if (tempDir != null && isViceExpand) {
                            String temp = tempDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".mp3";
                            audioRecorder = new AudioRecorder(temp);
                            audioRecorder.start();
                            Toast.makeText(ChatActivity.this, "قم بالإزاحة للتراجع عن الارسال", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onExpand() {
            }
            @Override
            public void onBeforeCollapse() {
            }
            @Override
            public void onCollapse(boolean cancel) {
                isViceExpand = false;
                if (audioRecorder != null) {
                    audioRecorder.stop();
                    File voice = new File(audioRecorder.path);
                    if (cancel) {
                        voice.delete();
                        Toast.makeText(ChatActivity.this, "تمت ازالة الرسالة بنجاح", Toast.LENGTH_SHORT).show();
                    } else {
                        new FilesUploader(ChatActivity.this, Uri.fromFile(voice), "voice", ".mp3", new FilesUploader.OnImageUploadListener() {
                            @Override
                            public void OnImageUploaded(String file) {
                                sendMessage("voice", file);
                                voice.delete();
                            }
                            @Override
                            public void OnError() {
                                Toast.makeText(ChatActivity.this, "عذرا , تاكد من الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
                                voice.delete();
                            }
                        });
                    }
                    audioRecorder = null;
                }
            }
            @Override
            public void onOffsetChanged(float v, boolean b) {
            }
        });
        input = findViewById(R.id.inputET);
        mSend = findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onClick(View v) {
                String msg = input.getText().toString();
                if (msg.isEmpty()) {
                    return;
                }
                input.setText("");
                input.setHint("Type something......");
                sendMessage("text", msg);
            }
        });
        mChatRecycler = findViewById(R.id.chat_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mChatRecycler.setLayoutManager(layoutManager);
        mChatRecycler.setHasFixedSize(true);
        mChatRecycler.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mChatRecycler.scrollToPosition(positionStart);
                }
            }
        });
        mChatRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mChatRecycler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mChatRecycler.smoothScrollToPosition(mChatRecycler.getAdapter().getItemCount() - 1);
                            } catch (Exception e) {
                            }
                        }
                    }, 100);
                }
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Chats").child(myUid).child(hisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final boolean firstTime = messages.isEmpty();
                messages.clear();
                for (DataSnapshot msg : dataSnapshot.getChildren()) {
                    Message message = msg.getValue(Message.class);
                    if (message != null && message.who != null) {
                        message.id = msg.getKey();
                        messages.add(message);
                    }
                }
                adapter.notifyDataSetChanged();
                mChatRecycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (firstTime) {
                                mChatRecycler.scrollToPosition(mChatRecycler.getAdapter().getItemCount() - 1);
                            } else {
                                mChatRecycler.smoothScrollToPosition(mChatRecycler.getAdapter().getItemCount() - 1);
                            }
                        } catch (Exception e) {
                        }
                    }
                }, 100);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "error");
            }
        });
    }
    ArrayList<Message> messages = new ArrayList<>();
    RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int layout = 0;
            switch (viewType) {
                case 0:
                    layout = R.layout.item_message_received;
                    break;
                case 1:
                    layout = R.layout.item_message_sent;
                    break;
            }
            return new ChatViewHolder(getLayoutInflater().inflate(layout, parent, false));
        }
        @Override
        public int getItemViewType(int position) {
            if (messages.get(position).who.equals("me")) {
                return 1;
            } else {
                return 0;
            }
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ChatViewHolder) holder).LoadMessage(ChatActivity.this, messages.get(position), myImage, hisImage);
        }
        @Override
        public int getItemCount() {
            return messages.size();
        }
    };
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        View view;
        CircleImageView imageView;
        public ChatViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = view.findViewById(R.id.single_image);
        }
        private void setMessage(final Activity context, Message message) {
            TextView text = view.findViewById(R.id.text);
            ImageView image = view.findViewById(R.id.image);
            VoicePlayerView playerView = view.findViewById(R.id.voicePlayerView);
            if (message.kind == null) {
                message.kind = "unknown";
            }
            switch (message.kind) {
                case "text":
                    text.setText(message.content);
                    text.setVisibility(View.VISIBLE);
                    image.setVisibility(View.GONE);
                    playerView.setVisibility(View.GONE);
                    break;
                case "video_call":
                    text.setText("مكالمة فيديو منتهية");
                    break;
                case "voice_call":
                    text.setText("مكالمة صوتية منتهية");
                    break;
                case "image":
                    image.setVisibility(View.VISIBLE);
                    text.setVisibility(View.GONE);
                    playerView.setVisibility(View.GONE);
                    Picasso.get().load(message.content).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(image);
                    break;
                case "voice":
                    image.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);
                    File target = new File(new File(Environment.getExternalStorageDirectory(), "jampo"), message.id + ".mp3");
                    if (target.exists()) {
                        playerView.setAudio(target.getAbsolutePath());
                    } else {
                        playerView.setAudio("none");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    saveUrl(target, message.content);
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            playerView.setAudio(target.getAbsolutePath());
                                        }
                                    });
                                } catch (IOException e) {
                                    target.delete();
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    break;
                default:
                    text.setText("يجب تحديث التطبيق لمشاهدة الرسالة");
                    text.setVisibility(View.VISIBLE);
                    image.setVisibility(View.GONE);
                    playerView.setVisibility(View.GONE);
                    break;
            }
            View.OnLongClickListener showTime = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, message.time, Toast.LENGTH_SHORT).show();
                    return false;
                }
            };
            text.setOnLongClickListener(showTime);
            if (image != null)
                image.setOnLongClickListener(showTime);
        }
        private void setWho(String who, final String myImage, final String hisImage) {
            if (imageView != null) {
                final String image = who.equals("me") ? myImage : hisImage;
                Picasso.get().load(image).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
            }
        }
        public void LoadMessage(Activity context, Message message, String myImage, String hisImage) {
            setMessage(context, message);
            setWho(message.who, myImage, hisImage);
        }
        public void saveUrl(final File filename, final String urlString) throws  IOException {

            filename.getParentFile().mkdirs();
            BufferedInputStream in = null;
            FileOutputStream fout = null;
            try {
                in = new BufferedInputStream(new URL(urlString).openStream());
                fout = new FileOutputStream(filename);
                final byte data[] = new byte[1024];
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    fout.write(data, 0, count);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
            }
        }
    }
}
