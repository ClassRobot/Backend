package org.dromara.ai.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.ai.constant.AiMessageRoleConstant;
import org.dromara.ai.constant.AiModelTypeConstant;
import org.dromara.ai.domain.bo.AiAgentChatBo;
import org.dromara.ai.domain.bo.AiAgentChatHistoryBo;
import org.dromara.ai.domain.bo.AiChatMessageBo;
import org.dromara.ai.domain.vo.AiAgentChatHistoryVo;
import org.dromara.ai.domain.vo.AiAgentChatVo;
import org.dromara.ai.domain.vo.AiAgentVo;
import org.dromara.ai.mapper.AiAgentChatHistoryMapper;
import org.dromara.ai.service.IAiAgentChatHistoryService;
import org.dromara.ai.service.IAiAgentChatService;
import org.dromara.ai.service.IAiAgentService;
import org.dromara.ai.service.IChatService;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {

    private final OpenAiChatClient openAiChatClient;
    private final OllamaChatClient ollamaChatClient;

    private final IAiAgentService aiAgentService;
    private final IAiAgentChatService aiAgentChatService;
    private final IAiAgentChatHistoryService aiAgentChatHistoryService;


    @Override
    public TableDataInfo<AiAgentChatVo> listMyChats(AiAgentChatBo bo, PageQuery pageQuery) {
        return aiAgentChatService.queryPageList(bo, pageQuery);
    }

    @Override
    public TableDataInfo<AiAgentChatHistoryVo> getMyChatMessages(Long chatId, PageQuery pageQuery) {
        return aiAgentChatHistoryService
            .queryPageList(AiAgentChatHistoryBo.builder()
                .agentChatId(chatId)
            .build(), pageQuery);
    }

    @Override
    public String sendMessage(AiChatMessageBo message) {

        String content = ollamaChatClient.call(buildPrompt(message)).getResult().getOutput().getContent();

        //插入对话智能体的历史记录
        aiAgentChatHistoryService.insertByBo(AiAgentChatHistoryBo.builder()
            .agentChatId(message.getChatId())
            .message(content)
            .agentId(message.getAgentId())
            .role(AiMessageRoleConstant.ASSISTANT)
            .build());

        return content;
    }

    @Override
    public Flux sendMessageFlux(AiChatMessageBo message) {
        return ollamaChatClient.stream(buildPrompt(message))
            .map((response -> response.getResult().getOutput().getContent()));
    }


    /**
     * 构建提示
     * @param message
     * @return
     */
    private Prompt buildPrompt(AiChatMessageBo message) {

        //查询智能体
//        AiAgentVo aiAgentVo = aiAgentService.queryById(message.getAgentId());

        //插入对话用户的历史记录
        aiAgentChatHistoryService.insertByBo(AiAgentChatHistoryBo.builder()
            .agentChatId(message.getChatId())
            .message(message.getMessage())
            .agentId(message.getAgentId())
            .role(AiMessageRoleConstant.USER)
            .build());

        ChatOptions chatOptions = null;

        if (AiModelTypeConstant.OPENAI.equals(message.getModelType())) {
            chatOptions = OpenAiChatOptions.builder().build();
        }else{
            chatOptions = OllamaOptions.create();
        }

        return new Prompt(buildMessage(message), chatOptions);
    }

    /**
     * 构建消息
     * @param message
     * @return
     */
    private List<Message> buildMessage(AiChatMessageBo message) {

        // 根据智能体id查询智能体全局system消息
        List<AiAgentChatHistoryVo> listSystem = aiAgentChatHistoryService.querySystemOverallList(message.getAgentId());

        // 根据id查询对话历史记录
        List<AiAgentChatHistoryVo> list = aiAgentChatHistoryService
            .queryList(AiAgentChatHistoryBo.builder()
                .agentChatId(message.getChatId())
                .build());

//        list.addAll(list2);

        ArrayList<Message> messages = new ArrayList<>();

        // 根据角色添加消息
        for (AiAgentChatHistoryVo vo : list) {
            if (AiMessageRoleConstant.ASSISTANT.equals(vo.getRole())) {
                messages.add(new AssistantMessage(vo.getMessage()));
            }else if (AiMessageRoleConstant.USER.equals(vo.getRole())){
                messages.add(new UserMessage(vo.getMessage()));
            }else{
                messages.add(new SystemMessage(vo.getMessage()));
            }
        }

        return messages;
    }
}
