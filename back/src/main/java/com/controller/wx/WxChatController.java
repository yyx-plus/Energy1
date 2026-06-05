package com.controller.wx;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ChatEntity;
import com.service.ChatService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 小程序 - 客服留言
 */
@RestController
@RequestMapping("/wx/chat")
public class WxChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 我的留言列表
     * 后台聊天设计：问题(chat_types=1)和回复(chat_types=2)是两条独立记录
     * 这里将它们配对合并后返回给小程序
     */
    @GetMapping("/list")
    public R myList(HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");

        // 查该用户所有记录，按时间正序
        List<ChatEntity> all = chatService.selectList(
                new EntityWrapper<ChatEntity>()
                        .eq("yonghu_id", yonghuId)
                        .orderBy("insert_time", true)
        );

        // 将问题(chat_types=1)和回复(chat_types=2)配对
        // 回复记录的 chat_issue 存的是对应问题的内容（或为null），用顺序配对
        List<Map<String, Object>> result = new ArrayList<>();
        List<ChatEntity> questions = new ArrayList<>();
        List<ChatEntity> replies = new ArrayList<>();

        for (ChatEntity c : all) {
            if (c.getChatTypes() == null || c.getChatTypes() == 1) {
                questions.add(c);
            } else if (c.getChatTypes() == 2) {
                replies.add(c);
            }
        }

        // 按顺序配对：第i个问题对应第i个回复
        for (int i = 0; i < questions.size(); i++) {
            ChatEntity q = questions.get(i);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", q.getId());
            item.put("chatIssue", q.getChatIssue());
            item.put("issueTime", q.getIssueTime());
            item.put("insertTime", q.getInsertTime());
            // 找对应回复
            if (i < replies.size()) {
                ChatEntity r = replies.get(i);
                item.put("chatReply", r.getChatReply());
                item.put("replyTime", r.getReplyTime());
            } else {
                item.put("chatReply", null);
                item.put("replyTime", null);
            }
            result.add(item);
        }

        return R.ok().put("data", result);
    }

    /** 提交留言 */
    @PostMapping("/submit")
    public R submit(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        String issue = String.valueOf(params.get("chatIssue"));
        if (issue == null || issue.trim().isEmpty() || "null".equals(issue))
            return R.error(400, "留言内容不能为空");

        ChatEntity chat = new ChatEntity();
        chat.setYonghuId(yonghuId);
        chat.setChatIssue(issue.trim());
        chat.setIssueTime(new Date());
        chat.setZhuangtaiTypes(1); // 1=未回复
        chat.setChatTypes(1);      // 1=问题
        chat.setInsertTime(new Date());
        chatService.insert(chat);
        return R.ok();
    }
}
