package cn.read.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.read.R;
import cn.read.bean.Chat;
import cn.read.bean.ChatReceive;

/**
 * Created by lw on 2017/2/18.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    public static final int SEND = 1;
    public static final int RECEIVE = 2;
    Context cxt;
    List<Chat> chats;

    public ChatAdapter(Context cxt, List<Chat> chats) {
        this.cxt = cxt;
        this.chats = chats;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case SEND:
                view = LayoutInflater.from(cxt).inflate(R.layout.item_send, parent, false);
                viewHolder = new SendViewHoulder(view);
                break;
            case RECEIVE:
                view = LayoutInflater.from(cxt).inflate(R.layout.item_receive, parent, false);
                viewHolder = new ReceiveViewHoulder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReceiveViewHoulder) {
            ReceiveViewHoulder receiveViewHoulder = (ReceiveViewHoulder) holder;
            receiveViewHoulder.tvContentReceive.setText(chats.get(position).getContent());
        } else {
            SendViewHoulder sendViewHoulder = (SendViewHoulder) holder;
            sendViewHoulder.tvContentRend.setText(chats.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chats.get(position).getType();
    }


    class SendViewHoulder extends RecyclerView.ViewHolder {
        TextView tvContentRend;

        public SendViewHoulder(View itemView) {
            super(itemView);
            tvContentRend = (TextView) itemView.findViewById(R.id.tvContentSend);
        }
    }

    class ReceiveViewHoulder extends RecyclerView.ViewHolder {
        TextView tvContentReceive;

        public ReceiveViewHoulder(View itemView) {
            super(itemView);
            tvContentReceive = (TextView) itemView.findViewById(R.id.tvContentReceive);
        }
    }

    public void sendContent(String content) {
        chats.add(new Chat(SEND, content));
        this.notifyDataSetChanged();
    }

    public void receiveContent(String content) {
        chats.add(new Chat(RECEIVE, content));
        this.notifyDataSetChanged();
    }

    public void loadChatLogs(int position, List<Chat> chatLogs) {
        chats.addAll(position, chatLogs);
        this.notifyDataSetChanged();
    }

    public void clearCharLog() {
        chats.clear();
        this.notifyDataSetChanged();
    }
}
