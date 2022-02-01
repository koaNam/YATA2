import de.koanam.yata2.kafka2elastic.Document;
import de.koanam.yata2.kafka2elastic.task.ElasticClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        ElasticClient client = new ElasticClient("localhost:9200", "elastic", "elastic", "documents");
        client.connect();

        Document d = new Document(1L, "title", "publication", "author", LocalDate.now(), "Content", LocalDateTime.now());
        try {
            client.insertDocument("1", d, LocalDateTime.now());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
