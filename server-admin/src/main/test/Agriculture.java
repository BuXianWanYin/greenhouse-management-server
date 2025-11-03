import com.server.ServerApplication;
import com.server.ai.AiService;
import com.server.ai.tool.AgricultureTools;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.data.message.UserMessage.userMessage;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Agriculture {

    @Autowired
    private AiService aiService;

    @Test
    public void testAIService() {
        aiService.chatStream("Tell me a joke").toStream().forEach(System.out::print);
    }

    @Test
    public void chatService() {
        String prompt = "475695037565 的平方根是多少";
        UserMessage userMessage = userMessage(prompt);
        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(AgricultureTools.class);
        ChatResponse chatResponse = aiService.ollamaChatModel.chat(ChatRequest.builder()
                .messages(userMessage)
                .toolSpecifications(toolSpecifications)
                .build());
        String result = "689706.486532";
        List<ToolExecutionRequest> toolExecutionRequests = chatResponse.aiMessage().toolExecutionRequests();
        ToolExecutionResultMessage toolExecutionResultMessage = ToolExecutionResultMessage.from(toolExecutionRequests.get(0), result);
        ArrayList<ChatMessage> arrayList = new ArrayList<>();

        arrayList.add(userMessage);
        arrayList.add(chatResponse.aiMessage());
        arrayList.add(toolExecutionResultMessage);

        ChatRequest request2 = ChatRequest.builder()
                .messages(arrayList) // Pass the ArrayList
                .toolSpecifications(toolSpecifications)
                .build();
        ChatResponse response2 = aiService.ollamaChatModel.chat(request2);
        System.out.println(response2);
    }
}
