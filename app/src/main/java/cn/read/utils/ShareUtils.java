package cn.read.utils;

import android.text.Html;

import cn.read.base.App;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by lw on 2017-03-14.
 */

public class ShareUtils {

    public static void share(String title, String shareLink, String content, String imageUrl, String videoUrl) {
        ShareSDK.initSDK(App.getContext());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(Html.fromHtml(content).toString());
        if (videoUrl != null) oks.setFilePath(videoUrl);
        oks.setImageUrl(imageUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareLink);
        // 启动分享GUI
        oks.show(App.getContext());
    }
}
