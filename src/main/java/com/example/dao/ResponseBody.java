package com.example.dao;

import java.util.Map;

/**
 * Created by damei on 18/1/16.
 * 应答包体
 */
public class ResponseBody {
    String versionid; //版本号，回传平台端调用时的版本号，目前默认为1.0
    boolean is_end;   //由开发者服务决定本次会话是否结束，如果标识为结束(true)平台会清除本次会话在平台保持的会话数据，如果标识为不结束（false）平台继续为用户保持当前会话数据。
    String sequence;  //交互流水号，回传平台时传递的值
    Long timestamp;   //开发者服务应答平台的请求的时间，格式为：当前时间的毫秒值
    String need_slot; //需要的槽值：如不为空，开放平台会主动为开发者收集此槽值服务，如用户输入的说法不符合槽值提取规则，则视为未识别重复收集。如为空则表明不需要平台关注槽值的识别，全部透传到第三方应用进行判断
    Directive directive; //开发者需要音箱设备播报的内容，其中可以包含文本播报和流媒体播报。
    Directive repeat_directive; //当音箱未能识别用户说话时，给用户的重新提示语，以引导用户进行后继的对话，其中可以包含文本播报和流媒体播报。如果该字段为空，且音箱发生未识别用户说话时，则音箱会重复播放directive字段的内容
    Card push_to_app; //开发者需要平台推送到音箱设备关联的手机APP展现的内容，其中可以包含：文本、文本+图片、连接等
    Map<String, String> extend; //扩展字段，目前支持的扩展内容是，上一次会话拒识时，要不要让音箱响应用户的下一次拒识的输入， 如果NO_REC（需大写）取值0时，播报响应。取值1时，不播报。默认是0

    public String getVersionid() {
        return versionid;
    }

    public void setVersionid(String versionid) {
        this.versionid = versionid;
    }


    public boolean isIs_end() {
        return is_end;
    }

    public void setIs_end(boolean is_end) {
        this.is_end = is_end;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNeed_slot() {
        return need_slot;
    }

    public void setNeed_slot(String need_slot) {
        this.need_slot = need_slot;
    }

    public Directive getDirective() {
        return directive;
    }

    public void setDirective(Directive directive) {
        this.directive = directive;
    }

    public Directive getRepeat_directive() {
        return repeat_directive;
    }

    public void setRepeat_directive(Directive repeat_directive) {
        this.repeat_directive = repeat_directive;
    }

    public Card getPush_to_app() {
        return push_to_app;
    }

    public void setPush_to_app(Card push_to_app) {
        this.push_to_app = push_to_app;
    }

    public Map<String, String> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, String> extend) {
        this.extend = extend;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "versionid='" + versionid + '\'' +
                ", is_end=" + is_end +
                ", sequence='" + sequence + '\'' +
                ", timestamp=" + timestamp +
                ", need_slot='" + need_slot + '\'' +
                ", directive=" + directive +
                ", repeat_directive=" + repeat_directive +
                ", push_to_app=" + push_to_app +
                ", extend=" + extend +
                '}';
    }
}
