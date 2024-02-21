package ru.fsv67.services;

import com.github.javafaker.Faker;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.fsv67.Issuance;
import ru.fsv67.Reader;
import ru.fsv67.repositories.ReaderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ReaderService {
    private final ReaderRepository readerRepository;
    private final WebClient webClient = WebClient.builder().build();
    private final EurekaClient eurekaClient;

    /**
     * Метод получения адреса зарегистрированного сервиса
     *
     * @return ip адрес и port
     */
    private String getIssuanceServiceIp() {
        Application application = eurekaClient.getApplication("ISSUANCE-SERVICE");
        List<InstanceInfo> instanceInfoList = application.getInstances();
        int indexInstance = ThreadLocalRandom.current().nextInt(instanceInfoList.size());
        InstanceInfo randomInstanceInfo = instanceInfoList.get(indexInstance);
        return randomInstanceInfo.getHomePageUrl();
//        return "http://" + randomInstanceInfo.getIPAddr() + ":" + randomInstanceInfo.getPort();
    }

    /**
     * Первоначальные тестовые данные
     */
    @PostConstruct
    void generateData() {
        Faker faker = new Faker();
        List<Reader> readerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            readerList.add(new Reader(faker.name().firstName(), faker.name().lastName()));
        }
        readerRepository.saveAll(readerList);
    }

    /**
     * Метод обрабатывает получение списка выдачи книг читателю по ID
     *
     * @param id идентификатор читателя
     * @return если список не пуст, то метод возвращает список выдачи книг, иначе исключение
     */
    public List<Issuance> issuanceListByIdReader(long id) {
        List<Issuance> list = null;
        try {
            list = webClient.get()
                    .uri(getIssuanceServiceIp() + "/issuance/reader/" + id)
                    .retrieve()
                    .bodyToFlux(Issuance.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            throw new NoSuchElementException("Читателю с ID = " + id + " книги не выдавались");
        } catch (Exception e) {
            throw new RuntimeException("Соединение с сервером выдачи книг не установлено");
        }
        return list;
    }

    /**
     * Метод обрабатывает получение читателя по ID
     *
     * @param id идентификатор читателя
     * @return если данные полученные не пусты, то метод возвращает читателя по ID, иначе исключение
     */
    public Reader getReaderById(long id) {
        return readerRepository
                .findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("Читатель с ID = " + id + " не найдена")
                );
    }

    /**
     * Метод обрабатывает получение списка читателей
     *
     * @return если список не пуст, то метод возвращает список читателей, иначе исключение
     */
    public List<Reader> getReaderList() {
        List<Reader> readers = readerRepository.findAll();
        if (readers.isEmpty()) {
            throw new NoSuchElementException("Список читателей пуст");
        }
        return readers;
    }

    /**
     * Метод обрабатывает данные читателя, введенные пользователем, для записи
     *
     * @param reader данные полученные от пользователя
     * @return если данные введенные корректно, то метод возвращает информацию о пользователе подлежащей для записи,
     * иначе исключение
     */
    public Reader addNewReader(Reader reader) {
        if (reader.getFirstName().isEmpty() && reader.getLastName().isEmpty()) {
            throw new RuntimeException("Имя читателя не задано");
        }
        return readerRepository.save(reader);
    }

    /**
     * Метод проверяет информацию о читателе перед удалением
     *
     * @param id идентификатор читателя подлежащего удалению
     * @return информацию удаленного читателя
     */
    public Reader deleteReaderById(long id) {
        Reader reader = getReaderById(id);
        readerRepository.deleteById(id);
        if (Objects.isNull(reader)) {
            throw new NoSuchElementException("Читатель с ID = " + id + " не найдена");
        }
        return reader;
    }
}