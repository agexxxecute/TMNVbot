package com.tgbot.tmnvbot.service;

import com.tgbot.tmnvbot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.api.objects.stickers.StickerSet;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    String actualPromo="";

    public TelegramBot (BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/concert", "Расписание ближайших выступлений"));
        listOfCommands.add(new BotCommand("/clips", "Клипы на наши песни"));
        /*try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e){
            //log.error(e.getMessage());
        }*/
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "Концерты":
                    concertCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "Клипы":
                    videoCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "Промокод":
                    promoCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "Стикерпак":
                    stickerCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "Песни":
                    songCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                default:
                    sendMessage(chatId, "Error");
            }
        }
    }

    private void songCommandReceived(long chatId, String songName) {
        String answer = "Послушай нашу новую песню \"Кума\":\n" +
                "https://bnd.lc/kuma\n\n" +
                "Слушать TMNV на цифровых площадках:\n" +
                "Яндекс Музыка:\n" +
                "https://music.yandex.ru/artist/7796783"+
                "ВК Музыка:\n" +
                "https://vk.com/artist/tmnv"+
                "Apple Music:\n" +
                "https://music.apple.com/ru/artist/tmnv/1474602936" +
                "Spotify:\n" +
                "https://open.spotify.com/artist/0QUIfrHsI7UagmvXBhPI0w?si=WBYEhZ6qTE6ZwJh4wJbRbw";
        sendMessage(chatId, answer);
    }

    private void stickerCommandReceived(long chatId, String name) {
        String answer = "Добавляй к себе наш стикерпак!\n\n" +
                "https://t.me/addstickers/TMNVband_by_fStikBot";
        sendMessage(chatId, answer);
        //log.info("Replied to user " + name);
    }

    private void promoCommandReceived(long chatId, String name){
        String answer;
        if(actualPromo.length()!=0){
            answer = "Актуальный промокод:\n" + actualPromo;
        } else{
            answer = "Сейчас доступных промокодов нет :(\n\n" +
                    "Следите за обновлениями в Телеграм-канале:\n" +
                    "https://t.me/tmnvmusic";
        }
        sendMessage(chatId, answer);
        //log.info("Replied to user " + name);
    }

    private void startCommandReceived(long chatId, String name){
        String answer = "Привет, " + name + "!\n\n" + "Я бот TMNV. Ты можешь воспользоваться одной из команд для получения информации.";
        sendMessage(chatId, answer);
        //log.info("Replied to user " + name);
    }

    private void concertCommandReceived(long chatId, String name){
        String answer = "Актуальная информация и билеты: https://tmnv.band/tmnvtour\n\n" +
                "Сольные концерты:\n" +
                "Санкт-Петербург 30.04, клуб Factory 3\nhttps://tmnv-spb.ticketscloud.org\n" +
                "Москва 01.05, клуб Pravda\nhttps://tmnv-msc.ticketscloud.org\n\n" +
                "Фестивали и клаб-шоу:\n"+
                "\"AURA\" Москва\nhttps://misticizm.timepad.ru/event/2803383/#register\n"+
                "\"Бульвар\" Ульяновск\nhttps://vk.com/bulvar_fest\n" +
                "\"Дикая мята\" Тульская обл.\nhttps://mintmusic.ru/tickets\n" +
                "\"Motherland\" Москва\nhttps://motherlandfestival.ru/\n" +
                "\"ПЛЯЖ\" Челябинск\nhttps://beach-fest.ru\n" +
                "\"Рок Остров\" Краснодарский край\nhttps://vk.com/island_rock\n\n";
        //String picture = "https://sun9-54.userapi.com/impg/dn9gyISRyKWBdSWP5PCj8Chu-L8j6RWqf8tnRA/ZezAJ6CxInE.jpg?size=1280x1280&quality=96&sign=55454c0311d1ff9052c13fb372898c3e&type=album";
        sendMessage(chatId, answer);
        //log.info("Replied to user " + name);
    }

    private void videoCommandReceived(Long chatId, String name){
        String answer = "Клип на песню \"Мне всё\"\n" +
                "https://youtu.be/uuvj8SsGEII?si=bIqvVX9ANfxWyNi1\n\n" +
                "Другие клипы:\n" +
                "https://youtube.com/playlist?list=OLAK5uy_m2GUhh-QsjciJRZah4FjBhbgz8Xm_LbP0&si=lx9J9uH8LLUI0iBk";

        sendMessage(chatId, answer);
        //log.info("Replied to user " + name);
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(replyKeyboardDefault());
        /*ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Концерты");
        row.add("Одежда");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Клипы");
        row.add("Песни");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Промокод");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);*/
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e) {
            //log.error("Error occurred: " + e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    private List<KeyboardRow> replyKeyboardDefault(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Концерты");
        row.add("Стикерпак");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Клипы");
        row.add("Песни");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Промокод");
        keyboardRows.add(row);

        return keyboardRows;
    }
}
