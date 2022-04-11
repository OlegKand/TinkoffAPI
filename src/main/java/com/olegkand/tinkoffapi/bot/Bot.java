package com.olegkand.tinkoffapi.bot;

import com.olegkand.tinkoffapi.service.TinkoffInvestAPIService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Component
@Data
@ConfigurationProperties(prefix = "bot")
public class Bot extends TelegramLongPollingBot{

    private String BOT_TOKEN;
    private String BOT_NAME;

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    private final TinkoffInvestAPIService tinkoffInvestAPIService;

    public Bot(TinkoffInvestAPIService tinkoffInvestAPIService){
        this.tinkoffInvestAPIService = tinkoffInvestAPIService;
        initKeyBoard();
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try{
            if(update.hasMessage() && update.getMessage().hasText())
            {

                Message inMess = update.getMessage();
                String chatId = inMess.getChatId().toString();
                String response = parseMessage(inMess.getText());

                SendMessage outMess = new SendMessage();

                outMess.setChatId(chatId);
                outMess.setText(response);
                outMess.setReplyMarkup(replyKeyboardMarkup);

                execute(outMess);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String parseMessage(String textMsg) {
        String response;

        //Сравниваем текст пользователя с нашими командами, на основе этого формируем ответ
        switch (textMsg) {
            case "/start":
                response = "Приветствую. Телеграм бот для получения сведений по портфелю";
                break;
            case "/get":
            case "Результат по портфелю":
                response = "Результат по портфелю - функция пока не работает";
                break;
            case "/get_5_top":
            case "Топ 5 роста":
                response = "Топ 5 роста - функция пока не работает";
                break;
            case "/get_5_low":
            case "Топ 5 падений":
                response = "Топ 5 падений - функция пока не работает";
                break;
            case "Акция по тикеру":
                response = "Введите тикер акции (например YNDX)";
                break;
            default:
                response = tinkoffInvestAPIService.getShareByTicker(textMsg);
                break;
        }
        return response;
    }

    public void initKeyBoard(){

        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        replyKeyboardMarkup.setOneTimeKeyboard(false); //скрываем после использования


        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);

        keyboardRow.add(new KeyboardButton("Результат по портфелю"));
        keyboardRow.add(new KeyboardButton("Топ 5 роста"));
        keyboardRow.add(new KeyboardButton("Топ 5 падений"));

        KeyboardRow keyboardRowTwo = new KeyboardRow();
        keyboardRows.add(keyboardRowTwo);

        keyboardRowTwo.add(new KeyboardButton("Дивиденды"));
        keyboardRowTwo.add(new KeyboardButton("Акция по тикеру"));

        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

}
