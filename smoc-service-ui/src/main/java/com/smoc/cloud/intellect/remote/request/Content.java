package com.smoc.cloud.intellect.remote.request;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class Content {

    //资源类型 text:表示文本 image:表示图片 video:表示视频 button:表示按钮 followPub:表示华为服务号
    private String type;

    //文本内容 资源类型为 Text 或 Button 时，为必填， 文本长度限制请按智能短信模板版式格 式标准。
    private String content;

    //资源类型 资源类型为 Image 或 Video 时，该项为 必填项。 1:指资源 ID 2:指资源地址
    private Integer srcType;

    //资源地址 资源类型为 Image 或 Video 时，为必填 如上 srcType 为 1，即资源 ID 时，参数 填入素材上传接口返回的资源 ID， 如:Q8jVYBnwN3Lxnc6eIjV 如上 srcType 为 2，即资源地址时，参数 填写资源完整的 URL，最大长度不超过 1000 个字符。
    private String src;

    //视频封面 资源类型为 Video 时，为必填， 如上 srcType 为 1，即资源 ID 时，参数 填入素材上传接口返回的资源 ID， 如:Q8jVYBnwN3Lxnc6eIjV 如上 srcType 为 2，即资源地址时，参数 填写资源完整的 URL，最大长度不超过 1000 个字符。
    private String cover;

    //是否为文本标题 true：是 false：不是 默认指为 false
    private String isTextTitle;

    //action 类型 如果 type 为 Image 和 Button 时，则为 必填项，必须绑定事件。其它 type 类型 则不必填。 OPEN_URL:表示跳转 H5 OPEN_QUICK:表示跳转快应用 OPEN_APP:表示跳转 App DIAL_PHONE:表示拉起拨号盘 OPEN_SMS:表示新建短信息 OPEN_EMAIL:表示打开邮箱 OPEN_SCHEDULE:表示新建日程 OPEN_MAP:表示位置定位 OPEN_BROWSER:表示打开浏览器 OPEN_POPUP:表示弹窗 COPY_PARAMETER:表示复制 VIEW_PIC:表示打开大图
    private String actionType;

    //action 对象 JSON格式 根据 action 类型设置对应的信息，参照 本小节 action 数据结构
    private Map<String, String> action;

    //位置序号 资源在卡片上相对的位置序号，按照优 先从左到右，再从上到下的编排原则， 统一编号。参考如下示例图
    private Integer positionNumber;

    //是否可见 0:隐藏（某些组件可设置隐藏。） 1:可见
    private Integer visible;

    //是否显示货币符号 当模板为电商类时是否显示¥符号，默认 可见0:隐藏 1:可见
    private Integer currencyDisplay;

    //oppo 红包背景 如上 srcType 为 1，即资源 ID 时，参数 填入素材上传接口返回的资源 ID， 如:Q8jVYBnwN3Lxnc6eIjV 如上 srcType 为 2，即资源地址时，参数 填写资源完整的 URL，最大长度不超过 1000 个字符。
    private Integer oppoBackground;

}
