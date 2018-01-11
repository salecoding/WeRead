package cn.read.ui.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.base.App;
import cn.read.base.RetrofitManager;
import cn.read.bean.Chat;
import cn.read.bean.ChatLog;
import cn.read.bean.ChatReceive;
import cn.read.bean.NewsSummary;
import cn.read.common.ApiConstants;
import cn.read.common.Constants;
import cn.read.common.HostType;
import cn.read.dao.ChatLogDao;
import cn.read.event.MessageEvent;
import cn.read.event.ScrollToTopEvent;
import cn.read.listener.RequestCallBack;
import cn.read.ui.adapter.ChatAdapter;
import cn.read.utils.MyUtils;
import cn.read.utils.NetUtil;
import cn.read.utils.RxBus;
import cn.read.utils.TransformUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lw on 2017/3/8.
 * 美女陪聊
 */

public class BeautyChatFragment extends BaseFragment implements RequestCallBack<ChatReceive>, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rvChat)
    RecyclerView rvChat;
    @Bind(R.id.etContent)
    EditText etContent;
    @Bind(R.id.btnSend)
    Button btnSend;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    ChatAdapter chatAdapter;
    List<Chat> chats;
    int pageIndex = 0;
    int pageSize = 10;

    public static BeautyChatFragment newInstance() {
        return new BeautyChatFragment();
    }

    @Override
    public void initViews(View view) {
        initRecyclerView();
        initSwipeRefreshLayout();
        registerMessageEvent();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors)
        );
    }

    private void initRecyclerView() {
        chats = new ArrayList<>();
        chats.add(new Chat(ChatAdapter.RECEIVE, getString(R.string.beauty_chat_hello)));
        chatAdapter = new ChatAdapter(getContext(), chats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvChat.setLayoutManager(linearLayoutManager);
        rvChat.setAdapter(chatAdapter);
    }

    private void registerMessageEvent() {
        mSubscription = RxBus.getInstance().toObservable(MessageEvent.class)
                .subscribe(new Action1<MessageEvent>() {
                    @Override
                    public void call(MessageEvent messageEvent) {
                        if (messageEvent.getTag().equals(Constants.CHAR)) {
                            chatAdapter.clearCharLog();
                            rvChat.getLayoutManager().scrollToPosition(0);
                            Snackbar.make(rvChat, R.string.clear_success, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void initInjector() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_beauty_chat;
    }


    @OnClick(R.id.btnSend)
    public void onClick() {
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)) return;
        chatAdapter.sendContent(content);
        rvChat.getLayoutManager().scrollToPosition(chatAdapter.getItemCount() - 1);
        saveChatLog(ChatAdapter.SEND, content, getString(R.string.liuwei));
        etContent.setText("");
        getChatReceive(this, ApiConstants.CHAT_URL + content);
    }

    /**
     * 保存聊天记录
     *
     * @param type
     * @param content
     * @param createBy
     */
    private void saveChatLog(int type, String content, String createBy) {
        ChatLog chatLog = new ChatLog();
        chatLog.setChatLogId(MyUtils.getUUIDStr());
        chatLog.setType(type);
        chatLog.setContent(content);
        chatLog.setCreateBy(createBy);
        chatLog.setCreateDate(new Date());
        App.getChatLogDao().insert(chatLog);
    }

    private void getChatReceive(final RequestCallBack<ChatReceive> listener, String url) {
        RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getChatReceiveObservable(url)
                .compose(TransformUtils.<ChatReceive>defaultSchedulers())
                .subscribe(new Subscriber<ChatReceive>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(ChatReceive s) {
                        listener.success(s);
                    }
                });
    }

    @OnTextChanged(R.id.etContent)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btnSend.setBackgroundResource(TextUtils.isEmpty(s) ? R.drawable.rounded_button_false : R.drawable.rounded_button);
        btnSend.setEnabled(!TextUtils.isEmpty(s));
    }

    @Override
    public void beforeRequest() {

    }

    @Override
    public void success(ChatReceive data) {
        if (data.getResult() == null) return;
        String content = data.getResult().getText();
        chatAdapter.receiveContent(content);
        rvChat.getLayoutManager().scrollToPosition(chatAdapter.getItemCount() - 1);
        saveChatLog(ChatAdapter.RECEIVE, content, getString(R.string.linzhiling));
        MyUtils.playBeepSoundAndVibrate(getActivity());
    }

    @Override
    public void onError(String errorMsg) {

    }

    @Override
    public void onRefresh() {
        pageIndex++;
        List<ChatLog> chatLogs = App.getChatLogDao().queryBuilder()
                .orderAsc(ChatLogDao.Properties.CreateDate)
                .offset((pageIndex - 1) * pageSize).limit(pageSize).list();
        List<Chat> chats = new ArrayList<>();
        for (ChatLog chatLog : chatLogs) {
            chats.add(new Chat(chatLog.getType(), chatLog.getContent()));
        }
        chatAdapter.loadChatLogs(0, chats);
        swipeRefreshLayout.setRefreshing(false);
    }
}
