package com.joey.jokedaily.bean;

/**
 * 文本笑话
 */
public class StrJoke {
    //    {
//        "error_code": 0,
//            "reason": "成功",
//            "result": [{
//        "title": "那些广场舞看起来好难啊",
//                "content": "今天和闺蜜一起逛街，路过公园，闺蜜听到了音乐停下脚步望向园内。\r\n　　我：怎么不走啦？\r\n　　二货闺蜜说到：一想到我们也会变老，心里感觉好害怕！\r\n　　我安慰她怕什么，人总会变老死去的啊！\r\n　　她：不是，那些广场舞看起来好难啊！我们以后能学会嘛！\r\n　　好吧好吧，你赢了。",
//                "type": "现代笑话",
//                "updatetime": "2016-04-15 00:47"
//    }]
//    }
    private String title;
    private String content;
    private String type;
    private String updatetime;

    @Override
    public String toString() {
        return "StrJoke{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }

    public StrJoke() {
    }

    public StrJoke(String title, String content, String type, String updatetime) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.updatetime = updatetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
